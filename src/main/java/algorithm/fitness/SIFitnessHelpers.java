package algorithm.fitness;

import model.SegmentedImageGenome;

public class SIFitnessHelpers {

    public float EdgeValue(SegmentedImageGenome individual) {
        float edgeValue = 0;

        int N = individual.GetGenomeLength();
        for(int i = 0; i < N; i++) {
            int[] J = F(i);

            for(int j : J) {
                // TODO: Add the if statement based on segment here
                edgeValue += d();

                // TODO til neste gang: Importer bilder
            }
        }
    }

    /**
     * Finds the Moore Neighborhood of pixel with index i.
     * @param i Pixel index to find the moore neighborhood to
     * @return Array of indexes to the neighboring pixels
     */
    private int[] F(int i) {
        return new int[8];
    }

    /**
     * Calculates the Euclidean distance for two pixes in the RGB space.
     * @param i First pixel value array (RGB)
     * @param j Second pixel value array (RGB)
     * @return The Euclidean distance value of pixels i and j in the RGB space
     */
    private float d(int[] i, int[] j) {
        return (float) Math.sqrt(
                ( (i[0] - j[0]) ^ 2 ) +
                ( (i[1] - j[1]) ^ 2 ) +
                ( (i[2] - j[2]) ^ 2 )
        );
    }

}
