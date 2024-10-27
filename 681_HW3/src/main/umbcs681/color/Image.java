package umbcs681.color;

public class Image {
    private int height;
    private int width;
    private Color[][] pixels;

    public Image(int height, int width) {
        this.height = height;
        this.width = width;
        this.pixels = new Color[height][width];

        // Initialize pixels with a default color (e.g., black)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = new Color(0, 0, 0);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Color getColor(int x, int y) {
        return pixels[y][x];
    }

    public void setColor(int x, int y, Color color) {
        pixels[y][x] = color;
    }
}
