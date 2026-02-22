package tests;

import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

public abstract class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() throws Exception {
        driver = DriverFactory.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.pressKey(new KeyEvent(AndroidKey.HOME));
        driver.activateApp("com.vk.vkvideo");
        System.out.println("Запуск теста");
    }

    @AfterEach
    public void tearDown() {
        //System.out.println("Очищаем список недавних приложений");

        try {
            driver.pressKey(new KeyEvent(AndroidKey.APP_SWITCH));
            Thread.sleep(2000);

            // Создаем свайп вверх с помощью W3C Actions
            int centerX = 540; // ширина экрана 1080 / 2
            int startY = 1500;  // нижняя часть
            int endY = 500;     // верхняя часть

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                    PointerInput.Origin.viewport(), centerX, startY));

            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

            swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                    PointerInput.Origin.viewport(), centerX, endY));

            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(swipe));
            Thread.sleep(1000);

            Runtime.getRuntime().exec("adb shell am force-stop com.vk.vkvideo");
            Thread.sleep(1000);

            driver.pressKey(new KeyEvent(AndroidKey.HOME));

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            driver.quit();
            System.out.println("Тест завершён");

        }
    }
}