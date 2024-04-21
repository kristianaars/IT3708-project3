package algorithm.fitness;

import model.SIGraphGenome;
import model.SIProblemInstance;
import model.SegmentedImageGenome;

/***
 * Fitness Calculator implementing fitness methods for Weighted Sum Fitness for Segmented Images
 */
public class SegmentedImageWSFCalculator implements IFitnessCalculator<SIGraphGenome> {

    private final SIFitnessHelper helper;

    private final float EDGE_VALUE_WEIGHT = 100000000.0f;
    private final float CONNECTIVITY_MEASURE_WEIGHT = 0.075f;
    private final float OVERALL_DEVIATION_WEIGHT = 0.00175f;

    private final int MAX_SEGMENT = 25;
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
    public IFitness CalculateFitness(SIGraphGenome genome) {
        float edgeValue = helper.EdgeValue(genome); // Maximize
        float connectivityMeasure = helper.ConnectivityMeasure(genome); // Minimize
        float overallDeviation = helper.OverallDeviation(genome); // Minimize

        float segmentPenalty = 0f;
        if(genome.GetSegmentCount() > MAX_SEGMENT) {
            segmentPenalty += (genome.GetSegmentCount() - MAX_SEGMENT) * 50.0f;
        } else if(genome.GetSegmentCount() < MIN_SEGMENT) {
            segmentPenalty += (MIN_SEGMENT - genome.GetSegmentCount()) * 10.0f;
        }


        SegmentedImageWSFitness fitness = new SegmentedImageWSFitness(
                (100.0f / edgeValue) * EDGE_VALUE_WEIGHT,
                connectivityMeasure * CONNECTIVITY_MEASURE_WEIGHT,
                overallDeviation * OVERALL_DEVIATION_WEIGHT,
                segmentPenalty
        );

        return fitness;
    }

}
