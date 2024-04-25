package utils;

import model.SIGraphGenome;
import model.SIProblemInstance;
import model.SegmentedImageGenome;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SegmentImageUtils {

    private final SIProblemInstance ProblemInstance;
    private final int Width;
    private final int Height;

    public SegmentImageUtils(int width, int height, SIProblemInstance problemInstance) {
        this.Width = width;
        this.Height = height;
        this.ProblemInstance = problemInstance;
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

    /**
     * Original image with green lines showing segments
     * @param genome
     * @return
     */
    public BufferedImage GeneratedSegmentedOverlayImageType1(SIGraphGenome genome) {
        int l = genome.GetGenomeLength();
        int w = genome.Width;
        boolean[][] line = GetSegmentLineMap(genome);
        BufferedImage original = ImageUtils.deepCopy(ProblemInstance.TestImage);

        for(int i = 0; i < l; i++) {
            int x = i % w;
            int y = i / w;

            if(line[x][y]) {
                original.setRGB(x, y, 0x32C823);
            }
        }

        return original;
    }

    /**
     * Generates white background with segment as white line
     * @param genome
     * @return
     */
    public BufferedImage GeneratedSegmentedOverlayImageType2(SIGraphGenome genome) {
        int l = genome.GetGenomeLength();
        int w = genome.Width;
        boolean[][] line = GetSegmentLineMap(genome);

        BufferedImage image = new BufferedImage(genome.Width ,genome.Height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(new Color ( 255, 255, 255 ));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        for(int i = 0; i < l; i++) {
            int x = i % w;
            int y = i / w;

            if(line[x][y]) {
                image.setRGB(x, y, 0x000000);
            }
        }

        return image;
    }

    private boolean[][] GetSegmentLineMap(SIGraphGenome genome) {
        int l = genome.GetGenomeLength();
        int w = genome.Width;
        ArrayList<Integer> p = genome.GetPhenotype();
        boolean[][] segmentLineMap = new boolean[w][genome.Height];

        for(int i = 0; i < w; i++) {
            for(int j = 0; j < genome.Height; j++) {
                segmentLineMap[i][j] = false;
            }
        }

        for(int i = 0; i < l; i++) {
            int x = i % w;
            int y = i / w;

            int seg = p.get(i);
            int[] N = CardinalNeighborIndexes(i , w);
            for(int n : N) {
                if(n < 0 || n >= l) continue;

                int seg_n = p.get(n);

                if(seg_n != seg) {
                    segmentLineMap[x][y] = true;
                }
            }
        }

        return segmentLineMap;
    }

    private int[] CardinalNeighborIndexes(int i, int w) {
        return new int[] {
                i % w != 0 ? i - 1 : -1, // Left
                i - w,
                i % w != (w - 1) ? i + 1 : -1,
                i + w
        };
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
