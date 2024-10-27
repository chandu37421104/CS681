package umbcs681.color;

import java.util.Objects;

public class Color {
    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRedScale() {
        return red;
    }

    public int getGreenScale() {
        return green;
    }

    public int getBlueScale() {
        return blue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color color = (Color) obj;
        return red == color.red && green == color.green && blue == color.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }

    @Override
    public String toString() {
        return String.format("Color(R: %d, G: %d, B: %d)", red, green, blue);
    }
}
