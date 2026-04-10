# Selenium Test Automation Framework

UI automation framework for `https://automationexercise.com` built with Selenium, TestNG, Maven, and Allure.

## Current Structure

- `src/main/java/pages`: page objects and shared page abstractions
- `src/main/java/keywords`: reusable end-to-end business flows
- `src/main/java/utils`: driver, config, wait, and framework utilities
- `src/test/java/base`: shared test lifecycle
- `src/test/java/listeners`: reporting and failure hooks
- `src/test/java/tests`: executable test scenarios
- `src/test/resources/suites`: TestNG suites

## Prerequisites

- Java 17+
- Maven installed and available as `mvn`
- Chrome or Firefox installed locally

## Run Tests

Run the default regression suite:

```bash
mvn clean test
```

Run the smoke suite:

```bash
mvn clean test -Psmoke
```

Run the regression suite explicitly:

```bash
mvn clean test -Pregression
```

## Runtime Overrides

The framework supports system property overrides so credentials and environment data do not need to live in source control.

Example:

```bash
mvn clean test -Psmoke \
  -Dbrowser=chrome \
  -Dheadless=true \
  -Denv=qa \
  -Dstandard.username='your_email@example.com' \
  -Dstandard.password='your_password'
```

Supported overrides:

- `browser`
- `headless`
- `env`
- `baseUrl`
- `<profile>.username`
- `<profile>.password`

## Allure Reporting

Generate the report:

```bash
mvn allure:report
```

Serve the report locally:

```bash
mvn allure:serve
```

Or use the repo script to get a stable URL printed in terminal:

```bash
mvn clean test
mvn allure:report
./scripts/serve-allure-report.sh
```

Default local URL:

```text
http://localhost:8080/index.html
```

Use a different port if needed:

```bash
./scripts/serve-allure-report.sh 9090
```

Allure raw results are written to `target/allure-results`.

## Framework Conventions

- Keep Selenium locators and element actions inside page classes.
- Keep business flows and reusable scenario steps inside keyword classes.
- Keep assertions close to the flow being validated when they represent business outcomes.
- Attach diagnostics through listeners, not ad hoc test code.
- Pass secrets through system properties or CI variables, not committed config.
