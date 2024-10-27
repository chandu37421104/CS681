package umbcs681.color;

import java.util.function.Function;

public interface ColorAdjuster extends Function<Color, Color> {
    @Override
    Color apply(Color color);
}