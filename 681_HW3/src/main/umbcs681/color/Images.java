package umbcs681.color;

import java.util.function.Function;
import java.util.stream.IntStream;

public class Images {
    public static Image transform(Image image, Function<Color, Color> adjuster) {
        int width = image.getWidth();
        int height = image.getHeight();

        Image adjusted = new Image(height, width);

        IntStream.range(0, height).boxed()
            .flatMap(y -> IntStream.range(0, width)
                .mapToObj(x -> new int[]{x, y}))
            .forEach(coord -> {
                int x = coord[0];
                int y = coord[1];
                Color originalColor = image.getColor(x, y);
                Color adjustedColor = adjuster.apply(originalColor);
                adjusted.setColor(x, y, adjustedColor);
            });

        return adjusted;
    }
}