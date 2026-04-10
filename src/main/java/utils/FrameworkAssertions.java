package utils;

public class FrameworkAssertions {

    private FrameworkAssertions() {
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
