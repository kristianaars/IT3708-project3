package algorithm.fitness;

import model.SIGraphGenome;
import model.SIProblemInstance;

public class SegmentedImageMOOFitnessCalculator implements IFitnessCalculator<SIGraphGenome, SegmentedImageFitness> {
    private final SIFitnessHelper helper;

    public SegmentedImageMOOFitnessCalculator(SIProblemInstance problemInstance) {
        helper = new SIFitnessHelper(problemInstance);
    }
    private final int MAX_SEGMENT = 40;
    private final int MIN_SEGMENT = 4;

    @Override
    public SegmentedImageFitness CalculateFitness(SIGraphGenome genome) {
        float edgeValue = helper.EdgeValue(genome); // Maximize
        float connectivityMeasure = helper.ConnectivityMeasure(genome); // Minimize
        float overallDeviation = helper.OverallDeviation(genome); // Minimize
        float segmentPenalty = 0;

        if(genome.GetSegmentCount() > MAX_SEGMENT) {
            segmentPenalty += (genome.GetSegmentCount() - MAX_SEGMENT) * 50000.0f;
        } else if(genome.GetSegmentCount() < MIN_SEGMENT) {
            segmentPenalty += (MIN_SEGMENT - genome.GetSegmentCount()) * 50000.0f;
        }

        segmentPenalty += genome.SmallSegmentCount * 10000.0f;

        SegmentedImageFitness fitness = new SegmentedImageFitness(
                (edgeValue * -1) + segmentPenalty,
                connectivityMeasure + segmentPenalty,
                overallDeviation  + segmentPenalty,
                segmentPenalty,
                -1
        );

        return fitness;
    }
}
