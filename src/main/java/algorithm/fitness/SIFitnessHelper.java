package algorithm.fitness;

import model.SIGraphGenome;
import utils.ImageUtils;
import model.SIProblemInstance;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SIFitnessHelper {

    private final SIProblemInstance ProblemInstance;

    private final BufferedImage SourceImage;

    public SIFitnessHelper(SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;

        this.SourceImage = problemInstance.TestImage;
    }

    /**
     * Calculates the edge value of a segmented image genome.
     *
     * @param individual The segmented image genome for which the edge value is calculated.
     * @return The edge value of the segmented image genome.
     */
    public float EdgeValue(SIGraphGenome individual) {
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
                int c_i = individual.GetPhenotype().get(i);
                int c_j = individual.GetPhenotype().get(j);

                // TODO: Dobbeltsjekk med TA om antagelsen min her er riktig
                // Check if the segment of the current and neighboring pixels are different
                if(c_i != c_j) {
                    // Get the RGB values of the current and neighboring pixels
                    int[] rgb_i = ImageUtils.GetRGBArray(SourceImage, i % w, i / w);
                    int[] rgb_j = ImageUtils.GetRGBArray(SourceImage, j % w, j / w);

                    // Calculate the RGB distance between the current and neighboring pixels
                    edgeValue += ImageUtils.RGBDistance(rgb_i, rgb_j);
                }
            }
        }

        // Return the calculated edge value
        return edgeValue;
    }

    public float ConnectivityMeasure(SIGraphGenome individual) {
        float connectivityMeasure = 0;

        int l = individual.GetGenomeLength();

        for(int i = 0; i < l; i++) {

            for(int j : F(i)) {
                // Skip if neighborhood is outside of image bounds
                if(j < 0 || j >= l) continue;

                // Get the segments of the current and neighboring pixels
                int c_i = individual.GetPhenotype().get(i);
                int c_j = individual.GetPhenotype().get(j);

                if(c_i != c_j) {
                    // Add penalty if neighboring pixels are not in same segment
                    connectivityMeasure += (1.0f / 8.0f);
                }
            }
        }

        return connectivityMeasure;
    }

    public float OverallDeviation(SIGraphGenome individual) {
        // Get the width of the source image
        int w = SourceImage.getWidth();
        int segmentCount = individual.GetSegmentCount();
        int l = individual.GetGenomeLength();
        float deviation = 0;

        ArrayList<ArrayList<Integer>> segmentIndices = new ArrayList<>(segmentCount);

        for(int i = 0; i < segmentCount; i++) {
            segmentIndices.add(new ArrayList<>());
        }

        for(int i = 0; i < l; i++) {
            int segment = individual.GetPhenotype().get(i);
            segmentIndices.get(segment).add(i);
        }

        for(int k = 0; k < segmentCount; k++) {
            List<Integer> k_indices = segmentIndices.get(k);

            // mu_k will contain the average pixel value for the given segment
            int[] mu_k = new int[3];
            for(int i : k_indices) {
                int[] rgb_i = ImageUtils.GetRGBArray(SourceImage, i % w, i / w);
                mu_k[0] += rgb_i[0];
                mu_k[1] += rgb_i[1];
                mu_k[2] += rgb_i[2];
            }

            mu_k[0] = (int) (mu_k[0] / k_indices.size());
            mu_k[1] = (int) (mu_k[1] / k_indices.size());
            mu_k[2] = (int) (mu_k[2] / k_indices.size());

            for(int i : k_indices) {
                int[] rgb_i = ImageUtils.GetRGBArray(SourceImage, i % w, i / w);
                deviation += ImageUtils.RGBDistance(rgb_i, mu_k);
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
        int[] F = new int[] {
                i-w-1, // Top left
                i-w, // Top Middle
                i-w+1, // Top Right
                i-1, // Left
                i+1, // Right
                i+w-1, // Bottom left
                i+w, // Bottom middle
                i+w+1 // Bottom right
        };

        // Validate
        // The pixel is on the left of the image, set all left-neighbors to -1 to disable
        if(i % w == 0) F[0] = F[3] = F[5] = -1;

        // Check if center pixel is on the right of the image, set all right-neighbors to -1 to disable
        else if(i % w == (w - 1)) F[2] = F[4] = F[7] = -1;

        return F;
    }


}
