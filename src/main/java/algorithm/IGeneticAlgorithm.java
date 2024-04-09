package algorithm;

import model.IGenome;

import java.util.Collection;

public interface IGeneticAlgorithm<I extends IGenome> {

    void Train(int epochs,
               float targetFitness,
               float p_m,
               int nCrossoverThreads,
               float overSelectionX);

    Collection<I> Crossover(I parent1, I parent2);

    void Mutate(I individual, float p_m);
}
