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

        welcome.closeGooglePopup();
        welcome.skipVKAuth();

        feed.openFirstVideo();

        Thread.sleep(10000); // ждем, пока видео загрузится

        boolean isPlaying = player.isVideoPlaying();

        if (isPlaying) {
            System.out.println("Видео успешно воспроизводится");
        } else {
            System.out.println("Видео не воспроизводится");
        }

        assertTrue(isPlaying,
                "Видео стоит на месте или отображается статичный кадр");
    }
}
