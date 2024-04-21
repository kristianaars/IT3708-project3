package algorithm.fitness;

public class SegmentedImageWSFitness implements IFitness {

    public final float EdgeValue;
    public final float ConnectivityMeasure;
    public final float OverallDeviation;
    public final float SegmentPenalty;

    private final float Fitness;

    public SegmentedImageWSFitness(float edgeValue, float connectivityMeasure, float overallDeviation, float segmentPenalty) {
        EdgeValue = edgeValue;
        ConnectivityMeasure = connectivityMeasure;
        OverallDeviation = overallDeviation;
        SegmentPenalty = segmentPenalty;

        Fitness = EdgeValue + ConnectivityMeasure + OverallDeviation + segmentPenalty;
    }

    @Override
    public float GetFitness() {
        return Fitness;
    }
}
