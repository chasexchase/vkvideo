import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VkvideoTest {
    private AndroidDriver driver;
    private WebDriverWait wait;

    private boolean isVideoPlaying() throws IOException, InterruptedException {
        // Делаем первый скриншот
        byte[] screen1 = driver.getScreenshotAs(OutputType.BYTES);
        Thread.sleep(5000);
        byte[] screen2 = driver.getScreenshotAs(OutputType.BYTES);

        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(screen1));
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(screen2));

        // Параметры обрезки: игнорируем верхние 15% (статус-бар) и нижние 15% (навигация)
        int width = img1.getWidth();
        int height = img1.getHeight();
        int cropY = (int) (height * 0.15); // Отступ сверху
        int cropHeight = (int) (height * 0.70); // Высота области для проверки (центр)

        // Вырезаем центральную часть, чтобы исключить интерфейс
        BufferedImage sub1 = img1.getSubimage(0, cropY, width, cropHeight);
        BufferedImage sub2 = img2.getSubimage(0, cropY, width, cropHeight);

        long diffPoints = 0;
        for (int y = 0; y < sub1.getHeight(); y++) {
            for (int x = 0; x < sub1.getWidth(); x++) {
                if (sub1.getRGB(x, y) != sub2.getRGB(x, y)) {
                    diffPoints++;
                }
            }
        }

        double totalPixels = sub1.getWidth() * sub1.getHeight();
        double diffPercentage = (diffPoints / totalPixels) * 100;

        System.out.println("Изменение картинки в зоне плеера: " + String.format("%.2f", diffPercentage) + "%");

        return diffPercentage > 0.5; // Для видео > 0.5% изменений
    }

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("platformVersion", "11");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "com.vk.vkvideo");
        //caps.setCapability("appActivity", "com.vk.vkvideo.screens.main.MainActivity");
        caps.setCapability("noReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4724"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.pressKey(new KeyEvent(AndroidKey.HOME));
        driver.activateApp("com.vk.vkvideo");
    }

    @Test
    public void openApp() throws InterruptedException, IOException {
        System.out.println("Запуск теста: openApp");

        // Закрываем окно Google
        try {
            System.out.println("Ожидаем окно Google...");
            // Ждем появления, затем ждем 10 секунд (ваше условие), затем кликаем, когда кнопка станет доступна
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.gms:id/cancel")));

            System.out.println("Окно появилось, ждем 5 секунд перед кликом...");
            Thread.sleep(5000);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.google.android.gms:id/cancel"))).click();
            System.out.println("Закрыли окно Google");

        } catch (Exception e) {
            System.out.println("Окно Google не появилось или возникла ошибка, продолжаем выполнение теста...");
        }

        // Скипаем авторизацию ВК
        WebElement skipAuthVK = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("com.vk.vkvideo:id/fast_login_tertiary_btn"))
            );
        assertTrue(skipAuthVK.isDisplayed(), "Элемент не отображается на экране");
        skipAuthVK.click();
        System.out.println("Скипнули авторизацию ВК");

        Thread.sleep(3000); // Ожидание прогрезки

        // Нажимаем на 1 видео в выдаче
        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var tapPoint = new Point(300, 650);
        var tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y));

        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(50)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));

        System.out.println("Нажали на видео");

        Thread.sleep(15000); // Ожидание прогрезки

        boolean isPlaying = isVideoPlaying();
        Assert.assertTrue("Видео стоит на месте или отображается статичный кадр!", isPlaying);
        System.out.println("Тест пройден: Видео успешно запущено.");

    }

    @After
    public void tearDown() {
        System.out.println("Очищаем список недавних приложений");

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
