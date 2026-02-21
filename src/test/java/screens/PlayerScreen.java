package screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.VideoDetector;

public class PlayerScreen extends BaseScreen {

    public PlayerScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isVideoPlaying() throws Exception {
        return VideoDetector.isVideoPlaying(driver);
    }
}
