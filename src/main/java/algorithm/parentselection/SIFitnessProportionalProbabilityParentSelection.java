package algorithm.parentselection;

import model.SegmentedImageGenome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SIFitnessProportionalProbabilityParentSelection implements IParentSelection<SegmentedImageGenome> {

    private final Random RandomInstance;

    public SIFitnessProportionalProbabilityParentSelection() {
        this.RandomInstance = new Random();
    }

    @Override
    public ArrayList<SegmentedImageGenome> SelectParents(ArrayList<SegmentedImageGenome> population, int delta) {
        double[] P = FitnessProportionalProbability(population);
        return StochasticUniversalSampling(population, P, delta);
    }

    private double[] FitnessProportionalProbability(ArrayList<SegmentedImageGenome> pool) {
        double beta_t = pool.stream().mapToDouble(i -> i.Fitness.GetFitness()).min().orElse(0);
        double sumFitness = 1.0 / pool.stream().mapToDouble(i -> (i.Fitness.GetFitness() - beta_t)).sum();
        double[] P = pool.stream().mapToDouble(i -> (1.0 - (i.Fitness.GetFitness() - beta_t) / sumFitness)).toArray();

        double p_sum = 0;
        for(double p : P) {
            p_sum += p;
        }

        double alpha = 1.0 / p_sum;

        for (int i = 0; i < P.length; i++) {
            P[i] = P[i] * alpha;
        }

        return P;
    }

    private ArrayList<SegmentedImageGenome> StochasticUniversalSampling(ArrayList<SegmentedImageGenome> selectionPool, double[] selectionP, int delta) {
        double r = RandomInstance.nextDouble(1.0 / delta);
        double[] a = ToCumulative(selectionP);

        int i = 0;
        int current_member = 0;
        SegmentedImageGenome[] samplePool = new SegmentedImageGenome[delta];

        while (current_member < delta) {
            while (r <= a[i]) {
                samplePool[current_member] = selectionPool.get(i);
                r += (1.0 / delta);
                current_member++;
            }
            i++;
        }

        return new ArrayList<>(List.of(samplePool));
    }

    private double[] ToCumulative(double[] p) {
        double[] cP = new double[p.length];

        double currCumP = 0.0;
        for (int i = 0; i < p.length; i++) {
            currCumP += p[i];
            cP[i] = currCumP;
        }

        return cP;
    }
}
