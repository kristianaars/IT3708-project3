package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static BufferedImage LoadImage(String resourcePath) throws IOException {
        return ImageIO.read(ImageUtils.class.getResource(resourcePath));
    }

    public static void SaveImage(String name, BufferedImage image) {
        try {
            // retrieve image
            File outputfile = new File(name);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {

        }
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static int[] GetRGBArray(BufferedImage bufferedImage, int x, int y) {
        int rgb = bufferedImage.getRGB(x, y);
        int[] rgbArray = new int[3];
        rgbArray[0] = (rgb >> 16) & 0xFF; // Red component
        rgbArray[1] = (rgb >> 8) & 0xFF;  // Green component
        rgbArray[2] = rgb & 0xFF;         // Blue component
        return rgbArray;
    }

    /**
     * Calculates the Euclidean distance for two pixes in the RGB space.
     * @param i First pixel value array (RGB)
     * @param j Second pixel value array (RGB)
     * @return The Euclidean distance value of pixels i and j in the RGB space
     */
    public static float RGBDistance(int[] i, int[] j) {
        return (float) Math.sqrt(
                Math.pow(i[0] - j[0], 2) +
                Math.pow(i[1] - j[1], 2) +
                Math.pow(i[2] - j[2], 2)
        );
    }


}
