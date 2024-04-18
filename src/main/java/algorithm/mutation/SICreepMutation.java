package algorithm.mutation;

import model.SIProblemInstance;
import model.SegmentedImageGenome;

import java.util.Random;

public class SICreepMutation implements IMutation<SegmentedImageGenome> {

    private final Random RandomInstance;
    private final SIProblemInstance ProblemInstance;

    private final int MAX_STEP_SIZE = 4;

    public SICreepMutation(SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;
        RandomInstance = new Random();
    }

    @Override
    public boolean mutate(SegmentedImageGenome individual, float p_m) {
        boolean isMutated = false;
        int l = individual.GetGenomeLength();

        int maxVal = ProblemInstance.MaxSegmentCount;
        //TODO: Behøver jeg en maks-verdi her?

        for(int i = 0; i < l; i ++) {
            if(RandomInstance.nextFloat() < p_m) {
                int incWeight = RandomInstance.nextInt(1, MAX_STEP_SIZE + 1);
                int inc = (RandomInstance.nextBoolean() ? 1 : -1) * incWeight;

                int existingVal = individual.genome.get(i);
                int newVal = existingVal + inc;

                if(newVal > 0 && newVal <= maxVal) {
                    individual.genome.set(i, newVal);
                    isMutated = true;
                }
            }
        }

        return isMutated;
    }

}
