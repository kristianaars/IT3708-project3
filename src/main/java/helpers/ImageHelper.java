package helpers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {

    public static BufferedImage LoadImage(String resourcePath) throws IOException {
        return ImageIO.read(ImageHelper.class.getResource(resourcePath));
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
