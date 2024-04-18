package algorithm.fitness;

import model.SIProblemInstance;
import model.SegmentedImageGenome;

/***
 * Fitness Calculator implementing fitness methods for Weighted Sum Fitness for Segmented Images
 */
public class SegmentedImageWSFCalculator implements IFitnessCalculator<SegmentedImageGenome> {

    private final SIFitnessHelper helper;

    private final float EDGE_VALUE_WEIGHT = 1.0f;
    private final float CONNECTIVITY_MEASURE_WEIGHT = 0.15f;
    private final float OVERALL_DEVIATION_WEIGHT = 0.15f;

    public SegmentedImageWSFCalculator(SIProblemInstance problemInstance) {
        this.helper = new SIFitnessHelper(problemInstance);
    }

    /***
     * Calculates the Weighted Sum Fitness of the Segmented Image Genome using Edge Value, Connectivity Measure, and Overall Deviation
     * @param genome Segmented Image Genome to calculate fitness for
     * @return The Weighted Sum Fitness of the provided genome
     */
    @Override
    public IFitness CalculateFitness(SegmentedImageGenome genome) {
        float edgeValue = helper.EdgeValue(genome); // Maximize
        float connectivityMeasure = helper.ConnectivityMeasure(genome); // Minimize
        float overallDeviation = helper.ConnectivityMeasure(genome); // Minimize

        return new SegmentedImageWSFitness(
                edgeValue * EDGE_VALUE_WEIGHT,
                (1.0f / connectivityMeasure) * CONNECTIVITY_MEASURE_WEIGHT,
                (1.0f / overallDeviation) * OVERALL_DEVIATION_WEIGHT
        );
    }

}
