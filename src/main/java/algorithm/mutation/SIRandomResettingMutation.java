package algorithm.mutation;

import model.SegmentedImageGenome;

import java.util.Random;

public class SIRandomResettingMutation implements IMutation<SegmentedImageGenome> {
    private final Random RandomInstance;

    public SIRandomResettingMutation() {
        RandomInstance = new Random();
    }

    @Override
    public boolean mutate(SegmentedImageGenome individual, float p_m) {
        boolean isMutated = false;

        int max = individual.Genome.stream().mapToInt(i -> i).max().orElse(-1);
        int min = individual.Genome.stream().mapToInt(i -> i).min().orElse(-1);

        int l = individual.GetGenomeLength();

        for(int i = 0; i < l; i ++) {
            if(RandomInstance.nextFloat() < p_m) {
                int resetVal = RandomInstance.nextInt(min, max + 1);
                individual.Genome.set(i, resetVal);
                isMutated = true;
            }
        }

        return isMutated;
    }

}
