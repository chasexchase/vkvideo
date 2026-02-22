package screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AndroidActions;

public class FeedScreen extends BaseScreen {

    public FeedScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    /**
     * Открывает первый видеоролик в ленте.
     * Используется как стартовая точка для проверки воспроизведения.
     */
    public void openFirstVideo() {
        AndroidActions.tap(driver, 300, 650);
        //System.out.println("Нажали на видео");
    }
}
