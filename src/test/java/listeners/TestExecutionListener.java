package listeners;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.DriverManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TestExecutionListener implements ITestListener, IExecutionListener {

    private static final Path PROJECT_ROOT = Path.of("").toAbsolutePath().normalize();
    private static final Path ALLURE_RESULTS_DIR = PROJECT_ROOT.resolve("target/allure-results");
    private static final Path ALLURE_REPORT_INDEX = PROJECT_ROOT.resolve("target/site/allure-maven-plugin/index.html");
    private static final Path ALLURE_REPORT_DIR = PROJECT_ROOT.resolve("target/site/allure-maven-plugin");
    private static final Path ALLURE_SERVER_LOG = PROJECT_ROOT.resolve("target/allure-server.log");
    private static final List<Path> MAVEN_FALLBACKS = List.of(
            PROJECT_ROOT.resolve("mvnw"),
            Path.of("/opt/homebrew/bin/mvn"),
            Path.of("/usr/local/bin/mvn"),
            Path.of("/usr/bin/mvn")
    );
    private static final List<Path> PYTHON_FALLBACKS = List.of(
            Path.of("/usr/bin/python3"),
            Path.of("/opt/homebrew/bin/python3"),
            Path.of("/usr/local/bin/python3")
    );

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            WebDriver driver = DriverManager.getDriver();
            attachScreenshot(driver);
            attachPageSource(driver);
            attachCurrentUrl(driver);
        } catch (IllegalStateException ignored) {
            // Driver was not created for this test.
        }
    }

    @Override
    public void onExecutionFinish() {
        System.out.println();
        System.out.println("==== Allure Report ====");
        System.out.println("Allure results: " + ALLURE_RESULTS_DIR.toAbsolutePath());

        if (hasAllureResults() && ensureAllureReportGenerated()) {
            Optional<String> serverUrl = startAllureReportServer();
            if (serverUrl.isPresent()) {
                System.out.println("Allure report: " + serverUrl.get());
                return;
            }

            System.out.println("Allure report path: " + ALLURE_REPORT_INDEX.toAbsolutePath());
            return;
        }

        System.out.println("Allure report not generated for this run.");
        System.out.println("Generate report: mvn allure:report");
        System.out.println("Serve report: ./scripts/serve-allure-report.sh");
    }

    private boolean hasAllureResults() {
        if (!Files.isDirectory(ALLURE_RESULTS_DIR)) {
            return false;
        }

        try (var files = Files.list(ALLURE_RESULTS_DIR)) {
            return files.findAny().isPresent();
        } catch (IOException ignored) {
            return false;
        }
    }

    private boolean ensureAllureReportGenerated() {
        Optional<Path> mavenExecutable = resolveMavenExecutable();
        if (mavenExecutable.isEmpty()) {
            System.out.println("Unable to locate Maven executable for Allure report generation.");
            return false;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(List.of(mavenExecutable.get().toString(), "-q", "allure:report"));
        processBuilder.directory(PROJECT_ROOT.toFile());
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            boolean finished = process.waitFor(2, TimeUnit.MINUTES);

            if (!finished) {
                process.destroyForcibly();
                System.out.println("Allure report generation timed out after 2 minutes.");
                return false;
            }

            if (process.exitValue() != 0) {
                System.out.println("Allure report generation failed with exit code " + process.exitValue() + ".");
                return false;
            }

            return Files.exists(ALLURE_REPORT_INDEX);
        } catch (IOException e) {
            System.out.println("Unable to start Maven for Allure report generation: " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Allure report generation was interrupted.");
            return false;
        }
    }

    private Optional<String> startAllureReportServer() {
        Optional<Path> pythonExecutable = resolveExecutable("python3", PYTHON_FALLBACKS);
        if (pythonExecutable.isEmpty()) {
            System.out.println("Unable to locate python3 to serve the Allure report.");
            return Optional.empty();
        }

        int port = findFreePort();
        if (port < 0) {
            System.out.println("Unable to find a free local port for the Allure report server.");
            return Optional.empty();
        }

        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExecutable.get().toString(),
                "-m",
                "http.server",
                String.valueOf(port),
                "--bind",
                "127.0.0.1"
        );
        processBuilder.directory(ALLURE_REPORT_DIR.toFile());
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(ALLURE_SERVER_LOG.toFile()));

        try {
            Process process = processBuilder.start();
            Thread.sleep(750);

            if (!process.isAlive()) {
                System.out.println("Allure report server exited immediately. Check " + ALLURE_SERVER_LOG.toAbsolutePath());
                return Optional.empty();
            }

            return Optional.of("http://127.0.0.1:" + port + "/index.html");
        } catch (IOException e) {
            System.out.println("Unable to start local server for Allure report: " + e.getMessage());
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Allure report server startup was interrupted.");
            return Optional.empty();
        }
    }

    private Optional<Path> resolveMavenExecutable() {
        return resolveExecutable("mvn", MAVEN_FALLBACKS);
    }

    private Optional<Path> resolveExecutable(String executableName, List<Path> fallbacks) {
        List<Path> candidates = new ArrayList<>();

        String mavenHome = System.getenv("MAVEN_HOME");
        if ("mvn".equals(executableName) && mavenHome != null && !mavenHome.isBlank()) {
            candidates.add(Path.of(mavenHome, "bin", executableName));
        }

        String pathValue = System.getenv("PATH");
        if (pathValue != null && !pathValue.isBlank()) {
            for (String entry : pathValue.split(":")) {
                if (!entry.isBlank()) {
                    candidates.add(Path.of(entry, executableName));
                }
            }
        }

        candidates.addAll(fallbacks);

        return candidates.stream()
                .map(Path::toAbsolutePath)
                .filter(Files::isRegularFile)
                .filter(Files::isExecutable)
                .findFirst();
    }

    private int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0, 0, InetAddress.getByName("127.0.0.1"))) {
            return socket.getLocalPort();
        } catch (IOException ignored) {
            return -1;
        }
    }

    private void attachScreenshot(WebDriver driver) {
        if (driver instanceof TakesScreenshot screenshotDriver) {
            byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
        }
    }

    private void attachPageSource(WebDriver driver) {
        byte[] source = driver.getPageSource().getBytes(StandardCharsets.UTF_8);
        Allure.addAttachment("Page Source", "text/html", new ByteArrayInputStream(source), ".html");
    }

    private void attachCurrentUrl(WebDriver driver) {
        byte[] currentUrl = driver.getCurrentUrl().getBytes(StandardCharsets.UTF_8);
        Allure.addAttachment("Current URL", "text/plain", new ByteArrayInputStream(currentUrl), ".txt");
    }
}
