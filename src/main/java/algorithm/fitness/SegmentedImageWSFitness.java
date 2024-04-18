package algorithm.fitness;

public class SegmentedImageWSFitness implements IFitness {

    public final float EdgeValue;
    public final float ConnectivityMeasure;
    public final float OverallDeviation;

    private final float Fitness;

    public SegmentedImageWSFitness(float edgeValue, float connectivityMeasure, float overallDeviation) {
        EdgeValue = edgeValue;
        ConnectivityMeasure = connectivityMeasure;
        OverallDeviation = overallDeviation;

        Fitness = EdgeValue + ConnectivityMeasure + OverallDeviation;
    }

    @Override
    public float GetFitness() {
        return Fitness;
    }
}
