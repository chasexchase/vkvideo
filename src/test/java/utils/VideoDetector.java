package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class VideoDetector {
    /**
     * проверяет факт воспроизведения видео по изменению изображения на экране
     * делает два последовательных скриншота и сравнивает центральную область,
     * исключая статичные элементы интерфейса.
     *
     * @param driver активный AndroidDriver
     * @return true если обнаружено изменение изображения выше порогового значения
     * @throws Exception если не удалось получить или обработать скриншоты
     */
    public static boolean isVideoPlaying(AndroidDriver driver) throws Exception {

        byte[] screen1 = driver.getScreenshotAs(OutputType.BYTES);
        Thread.sleep(5000);
        byte[] screen2 = driver.getScreenshotAs(OutputType.BYTES);

        BufferedImage img1 = ImageIO.read(new ByteArrayInputStream(screen1));
        BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(screen2));

        int width = img1.getWidth();
        int height = img1.getHeight();
        int cropY = (int) (height * 0.15);
        int cropHeight = (int) (height * 0.70);

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

        return diffPercentage > 0.5;
    }
}
