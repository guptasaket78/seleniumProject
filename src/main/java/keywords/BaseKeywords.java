package keywords;

import io.qameta.allure.Allure;
import utils.FrameworkAssertions;

public abstract class BaseKeywords {

    protected void step(String message, Runnable action) {
        Allure.step(message, action::run);
    }

    protected void verifyTrue(boolean condition, String message) {
        FrameworkAssertions.assertTrue(condition, message);
    }
}
