package umbcs681.color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorTest {

    private Image testImage;
    private ColorAdjuster grayScaleAdjuster;

    @BeforeEach
    public void setup() {
        testImage = new Image(2, 2);
        testImage.setColor(0, 0, new Color(255, 0, 0)); // Red
        testImage.setColor(0, 1, new Color(0, 255, 0)); // Green
        testImage.setColor(1, 0, new Color(0, 0, 255)); // Blue
        testImage.setColor(1, 1, new Color(255, 255, 0)); // Yellow

        grayScaleAdjuster = new GrayScaleAdjuster();
    }

    @Test
    public void redGrayScaleAdjusterTest() {
        assertEquals(new Color(85, 85, 85), grayScaleAdjuster.apply(new Color(255, 0, 0)));
    }

    @Test
    public void blueGrayScaleAdjusterTest() {
        assertEquals(new Color(85, 85, 85), grayScaleAdjuster.apply(new Color(0, 0, 255)));
    }

    @Test
    public void greenGrayScaleAdjusterTest() {
        assertEquals(new Color(85, 85, 85), grayScaleAdjuster.apply(new Color(0, 255, 0)));
    }

    @Test
    public void rgbGrayScaleAdjusterTest() {
        assertEquals(new Color(170, 170, 170), grayScaleAdjuster.apply(new Color(255, 255, 0)));
    }

    @Test
    public void redImageTransformationTest() {
        Image transformedImage = Images.transform(testImage, grayScaleAdjuster::apply);
        Color expectedGrayRed = grayScaleAdjuster.apply(testImage.getColor(0, 0));
        assertEquals(expectedGrayRed, transformedImage.getColor(0, 0));
    }

    @Test
    public void blueImageTransformationTest() {
        Image transformedImage = Images.transform(testImage, grayScaleAdjuster::apply);
        Color expectedGrayBlue = grayScaleAdjuster.apply(testImage.getColor(1, 0));
        assertEquals(expectedGrayBlue, transformedImage.getColor(1, 0));
    }

    @Test
    public void greenImageTransformationTest() {
        Image transformedImage = Images.transform(testImage, grayScaleAdjuster::apply);
        Color expectedGrayGreen = grayScaleAdjuster.apply(testImage.getColor(0, 1));
        assertEquals(expectedGrayGreen, transformedImage.getColor(0, 1));
    }

    @Test
    public void yellowImageTransformationTest() {
        Image transformedImage = Images.transform(testImage, grayScaleAdjuster::apply);
        Color expectedGrayYellow = grayScaleAdjuster.apply(testImage.getColor(1, 1));
        assertEquals(expectedGrayYellow, transformedImage.getColor(1, 1));
    }
}