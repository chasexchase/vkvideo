package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

public class AndroidActions {
    /**
     * тап по координатам экрана через W3C Actions
     *
     * @param driver AndroidDriver
     * @param x координата X
     * @param y координата Y
     */
    public static void tap(AndroidDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                x, y
        ));

        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        Pause pause = new Pause(finger, Duration.ofMillis(50));
        tap.addAction(pause);

        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(tap));
    }
}
