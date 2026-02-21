package tests;

import org.junit.jupiter.api.Test;
import screens.FeedScreen;
import screens.PlayerScreen;
import screens.WelcomeScreen;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VkVideoTest extends BaseTest {

    @Test
    void videoShouldPlay() throws Exception {

        WelcomeScreen welcome = new WelcomeScreen(driver, wait);
        FeedScreen feed = new FeedScreen(driver, wait);
        PlayerScreen player = new PlayerScreen(driver, wait);

        welcome.closeGooglePopupIfPresent();
        welcome.skipAuth();

        Thread.sleep(2000);

        feed.openFirstVideo();

        Thread.sleep(8000);

        boolean isPlaying = player.isVideoPlaying();

        assertTrue(isPlaying,
                "Видео стоит на месте или отображается статичный кадр!");

        System.out.println("Тест пройден: Видео успешно запущено.");
    }
}
