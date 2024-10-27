package umbcs681.color;

public class GrayScaleAdjuster implements ColorAdjuster {
    @Override
    public Color apply(Color color) {
        int r = color.getRedScale();
        int g = color.getGreenScale();
        int b = color.getBlueScale();
        int avg = (r + g + b) / 3;
        return new Color(avg, avg, avg);
    }
}