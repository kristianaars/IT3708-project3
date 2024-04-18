package algorithm.crossover;

import model.IGenome;

import java.util.ArrayList;

public interface ICrossover<I extends IGenome> {

    ArrayList<I> Crossover(I parent1, I parent2);
}
