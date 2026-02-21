package screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WelcomeScreen extends BaseScreen {

    private By skipAuth = By.id("com.vk.vkvideo:id/fast_login_tertiary_btn");
    private By googleCancel = By.id("com.google.android.gms:id/cancel");

    public WelcomeScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void closeGooglePopupIfPresent() {
        // Закрываем окно Google
        try {
            System.out.println("Ожидаем окно Google...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(googleCancel));
            System.out.println("Окно появилось, ждем 5 секунд перед кликом...");
            Thread.sleep(5000);
            wait.until(ExpectedConditions.elementToBeClickable(googleCancel)).click();
            System.out.println("Закрыли окно Google");
        } catch (Exception ignored) {}
    }

    public void skipAuth() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(skipAuth)).click();
        System.out.println("Скипнули авторизацию ВК");
    }
}
