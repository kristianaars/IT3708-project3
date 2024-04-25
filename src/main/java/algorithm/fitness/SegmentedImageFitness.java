package algorithm.fitness;

public class SegmentedImageFitness implements IFitness {

    public final float EdgeValue;
    public final float ConnectivityMeasure;
    public final float OverallDeviation;
    public final float SegmentPenalty;

    private float Fitness;

    public SegmentedImageFitness(float edgeValue, float connectivityMeasure, float overallDeviation, float segmentPenalty, float fitness) {
        EdgeValue = edgeValue;
        ConnectivityMeasure = connectivityMeasure;
        OverallDeviation = overallDeviation;
        SegmentPenalty = segmentPenalty;

        Fitness = fitness;
    }

    @Override
    public float GetFitness() {
        return Fitness;
    }

    public void SetFitness(float f) {
        Fitness = f;
    }

    @Override
    public String toString() {
        return "SegmentedImageFitness{" +
                "EdgeValue=" + EdgeValue +
                ", ConnectivityMeasure=" + ConnectivityMeasure +
                ", OverallDeviation=" + OverallDeviation +
                '}';
    }
}
