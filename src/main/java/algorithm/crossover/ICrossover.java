package algorithm.crossover;

import model.IGenome;

import java.util.List;

public interface ICrossover<I extends IGenome> {

    List<I> Crossover(I parent1, I parent2);
}
