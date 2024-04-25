package algorithm.mutation;

import model.SIGraphDirection;
import model.SIGraphGenome;

import java.util.Random;

public class SIGraphRandomResetMutation implements IMutation<SIGraphGenome> {
    private final Random RandomInstance;

    public SIGraphRandomResetMutation() {
        RandomInstance = new Random();
    }

    @Override
    public boolean mutate(SIGraphGenome individual, float p_m) {
        boolean isMutated = false;

        int l = individual.GetGenomeLength();
        int w = individual.Width;

        /*
        if(RandomInstance.nextFloat() < p_m) {
            int mutate_i = RandomInstance.nextInt(l);

            SIGraphDirection resetVal = null;
            boolean valid = false;

            while (!valid) {
                resetVal = SIGraphDirection.GetRandom();

                switch (resetVal) {
                    case Right -> { valid = mutate_i + 1 < l; break; }
                    case Left -> { valid = mutate_i - 1 > 0; break; }
                    case Up -> {valid = mutate_i - w > 0; break; }
                    case Down -> {valid = mutate_i + w < l; break; }
                    case End -> {valid = true; }
                }
            }

            individual.Genome.set(mutate_i, resetVal);
            isMutated = true;
        }
        */

        for(int i = 0; i < l; i ++) {
            if(RandomInstance.nextFloat() < p_m) {
                SIGraphDirection resetVal = null;
                boolean valid = false;

                while (!valid) {
                    resetVal = SIGraphDirection.GetRandom();

                    switch (resetVal) {
                        case Right -> { valid = i % w != (w - 1); break; }
                        case Left -> { valid = i % w != 0; break; }
                        case Up -> {valid = i - w > 0; break; }
                        case Down -> {valid = i + w < l; break; }
                        case End -> {valid = true; }
                    }
                }

                individual.Genome.set(i, resetVal);
                isMutated = true;
            }
        }


        return isMutated;
    }

}
