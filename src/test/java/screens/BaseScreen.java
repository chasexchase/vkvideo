package screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BaseScreen {

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    public BaseScreen(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
}
