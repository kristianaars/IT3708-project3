package utils;

import model.SIGraphGenome;
import model.SegmentedImageGenome;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class SegmentImageUtils {

    private final int Width;
    private final int Height;

    public SegmentImageUtils(int width, int height) {
        this.Width = width;
        this.Height = height;
    }

    public BufferedImage GenerateSegmentedImage(SIGraphGenome genome) {
        ArrayList<Color> colors = generateRandomColors(genome.GetSegmentCount());
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);

        int l = genome.GetGenomeLength();

        for(int i = 0; i < l; i++) {
            int seg = genome.GetPhenotype().get(i);
            Color col = colors.get(seg);
            int x = i % Width;
            int y = i / Width;

            img.setRGB(x, y, col.getRGB());
        }

        return img;
    }

    public static ArrayList<Color> generateRandomColors(int count) {
        ArrayList<Color> colors = new ArrayList<>();

        Random rand = new Random();
        for (int i = 0; i < count + 1; i++) {
            int red = rand.nextInt(256); // Random red component
            int green = rand.nextInt(256); // Random green component
            int blue = rand.nextInt(256); // Random blue component
            Color color = new Color(red, green, blue);
            colors.add(color);
        }
        return colors;
    }

}
