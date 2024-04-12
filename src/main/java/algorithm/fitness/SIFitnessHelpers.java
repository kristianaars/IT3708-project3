package algorithm.fitness;

import helpers.ImageHelper;
import model.SegmentedImageGenome;

import java.awt.image.BufferedImage;

public class SIFitnessHelpers {

    public final BufferedImage SourceImage;

    public SIFitnessHelpers(BufferedImage sourceImage) {
        this.SourceImage = sourceImage;
    }

    public float EdgeValue(SegmentedImageGenome individual) {
        float edgeValue = 0;
        int w = SourceImage.getWidth(); // Image width
        int l = individual.GetGenomeLength();

        int N = individual.GetGenomeLength();
        for(int i = 0; i < N; i++) {
            int[] J = F(i);

            for(int j : J) {
                if(j < 0 || j > l) continue; // Skip if neighborhood is outside of image bounds

                int c_i = individual.genome.get(i);
                int c_j = individual.genome.get(j);

                // TODO: Sjekk med TA om det er riktig antagelse av logikken
                if(c_i != c_j) {
                    int[] rgb_i = ImageHelper.GetRGBArray(SourceImage, i % w, i / w);
                    int[] rgb_j = ImageHelper.GetRGBArray(SourceImage, j % w, j / w);

                    edgeValue += RGBDistance(rgb_i, rgb_j);
                }

            }
        }

        return edgeValue;
    }

    /**
     * Finds the Moore Neighborhood of pixel with index i.
     * @param i Pixel index to find the moore neighborhood to
     * @return Array of indexes to the neighboring pixels
     */
    private int[] F(int i) {
        int w = SourceImage.getWidth();
        return new int[] {
                i-w-1, i-w, i-w+1,
                i-1, i+1,
                i+w-1, i+w , i+w+1
        };
    }

    /**
     * Calculates the Euclidean distance for two pixes in the RGB space.
     * @param i First pixel value array (RGB)
     * @param j Second pixel value array (RGB)
     * @return The Euclidean distance value of pixels i and j in the RGB space
     */
    private float RGBDistance(int[] i, int[] j) {
        return (float) Math.sqrt(
                ( (i[0] - j[0]) ^ 2 ) +
                ( (i[1] - j[1]) ^ 2 ) +
                ( (i[2] - j[2]) ^ 2 )
        );
    }

}
