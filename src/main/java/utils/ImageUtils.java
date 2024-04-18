package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {

    public static BufferedImage LoadImage(String resourcePath) throws IOException {
        return ImageIO.read(ImageUtils.class.getResource(resourcePath));
    }

    public static int[] GetRGBArray(BufferedImage bufferedImage, int x, int y) {
        int rgb = bufferedImage.getRGB(x, y);
        int[] rgbArray = new int[3];
        rgbArray[0] = (rgb >> 16) & 0xFF; // Red component
        rgbArray[1] = (rgb >> 8) & 0xFF;  // Green component
        rgbArray[2] = rgb & 0xFF;         // Blue component
        return rgbArray;
    }

}
