package algorithm.fitness;

import utils.ImageUtils;
import model.SIProblemInstance;
import model.SegmentedImageGenome;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

public class SIFitnessHelper {

    private final SIProblemInstance ProblemInstance;

    private final BufferedImage SourceImage;

    private final int SegmentCount;

    public SIFitnessHelper(SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;

        this.SourceImage = problemInstance.TestImage;
        this.SegmentCount = problemInstance.MaxSegmentCount;
    }

    /**
     * Calculates the edge value of a segmented image genome.
     *
     * @param individual The segmented image genome for which the edge value is calculated.
     * @return The edge value of the segmented image genome.
     */
    public float EdgeValue(SegmentedImageGenome individual) {
        // Initialize edge value
        float edgeValue = 0;

        // Get the width of the source image
        int w = SourceImage.getWidth();

        // Get the length of the genome
        int l = individual.GetGenomeLength();

        // Loop through each pixel in the genome
        for(int i = 0; i < l; i++) {
            // Get the neighborhood of the current pixel
            int[] J = F(i);

            // Loop through each neighboring pixel
            for(int j : J) {
                // Skip if neighborhood is outside of image bounds
                if(j < 0 || j >= l) continue;

                // Get the segments of the current and neighboring pixels
                int c_i = individual.genome.get(i);
                int c_j = individual.genome.get(j);

                // TODO: Dobbeltsjekk med TA om antagelsen min her er riktig
                // Check if the segment of the current and neighboring pixels are different
                if(c_i != c_j) {
                    // Get the RGB values of the current and neighboring pixels
                    int[] rgb_i = ImageUtils.GetRGBArray(SourceImage, i % w, i / w);
                    int[] rgb_j = ImageUtils.GetRGBArray(SourceImage, j % w, j / w);

                    // Calculate the RGB distance between the current and neighboring pixels
                    edgeValue += RGBDistance(rgb_i, rgb_j);
                }
            }
        }

        // Return the calculated edge value
        return edgeValue;
    }

    public float ConnectivityMeasure(SegmentedImageGenome individual) {
        float connectivityMeasure = 0;

        int l = individual.GetGenomeLength();

        for(int i = 0; i < l; i++) {

            int[] J = F(i);

            for(int j : J) {
                // Skip if neighborhood is outside of image bounds
                if(j < 0 || j >= l) continue;

                // Get the segments of the current and neighboring pixels
                int c_i = individual.genome.get(i);
                int c_j = individual.genome.get(j);

                if(c_i != c_j) {
                    // Add penalty if neighboring pixels are not in same segment
                    connectivityMeasure += (float) (1.0 / 8);
                }
            }
        }

        return connectivityMeasure;
    }

    public float OverallDeviation(SegmentedImageGenome individual) {
        // Get the width of the source image
        int w = SourceImage.getWidth();

        float deviation = 0;

        for(int k = 0; k < SegmentCount; k++) {
            int finalK = k; // To make k compatible with lambda function

            List<Integer> k_indices = IntStream.range(0, individual.GetGenomeLength())
                    .filter(i -> individual.genome.get(i) == finalK)
                    .boxed()
                    .toList();

            // mu_k will contain the average pixel value for the given segment
            int[] mu_k = new int[3];
            for(int i : k_indices) {
                int[] rgb_i = ImageUtils.GetRGBArray(SourceImage, i % w, i / w);
                mu_k[0] += rgb_i[0];
                mu_k[1] += rgb_i[1];
                mu_k[2] += rgb_i[2];
            }

            mu_k[0] /= k_indices.size(); mu_k[1] /= k_indices.size(); mu_k[2] /= k_indices.size();

            for(int i : k_indices) {
                int[] rgb_i = ImageUtils.GetRGBArray(SourceImage, i % w, i / w);
                deviation += RGBDistance(rgb_i, mu_k);
            }
        }

        return deviation;
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
