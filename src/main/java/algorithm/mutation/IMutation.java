package algorithm.mutation;

import model.IGenome;

public interface IMutation<I extends IGenome> {
    boolean mutate(I individual, float p_m);
}
