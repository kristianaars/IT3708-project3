package algorithm.fitness;

import model.SIGraphGenome;
import model.SIProblemInstance;

/***
 * Fitness Calculator implementing fitness methods for Weighted Sum Fitness for Segmented Images
 */
public class SegmentedImageWSFCalculator implements IFitnessCalculator<SIGraphGenome, SegmentedImageFitness> {

    private final SIFitnessHelper helper;

    private final float EDGE_VALUE_WEIGHT = -0.0655f;
    private final float CONNECTIVITY_MEASURE_WEIGHT = 5.95f;
    private final float OVERALL_DEVIATION_WEIGHT = 0.00015f;

    private final int MAX_SEGMENT = 40;
    private final int MIN_SEGMENT = 4;

    public SegmentedImageWSFCalculator(SIProblemInstance problemInstance) {
        this.helper = new SIFitnessHelper(problemInstance);
    }

    /***
     * Calculates the Weighted Sum Fitness of the Segmented Image Genome using Edge Value, Connectivity Measure, and Overall Deviation
     * @param genome Segmented Image Genome to calculate fitness for
     * @return The Weighted Sum Fitness of the provided genome
     */
    @Override
    public SegmentedImageFitness CalculateFitness(SIGraphGenome genome) {
        float edgeValue = helper.EdgeValue(genome) * EDGE_VALUE_WEIGHT; // Maximize
        float connectivityMeasure = helper.ConnectivityMeasure(genome)* CONNECTIVITY_MEASURE_WEIGHT; // Minimize
        float overallDeviation = helper.OverallDeviation(genome)* OVERALL_DEVIATION_WEIGHT; // Minimize

        float segmentPenalty = 0f;
        if(genome.GetSegmentCount() > MAX_SEGMENT) {
            segmentPenalty += (genome.GetSegmentCount() - MAX_SEGMENT) * 5000.0f;
        } else if(genome.GetSegmentCount() < MIN_SEGMENT) {
            segmentPenalty += (MIN_SEGMENT - genome.GetSegmentCount()) * 5000.0f;
        }

        segmentPenalty += genome.SmallSegmentCount * 10000.0f;

        SegmentedImageFitness fitness = new SegmentedImageFitness(
                edgeValue,
                connectivityMeasure,
                overallDeviation,
                segmentPenalty,
                edgeValue + connectivityMeasure + overallDeviation + segmentPenalty
        );

        return fitness;
    }

}
