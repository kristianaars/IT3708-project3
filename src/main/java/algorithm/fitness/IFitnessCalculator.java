package algorithm.fitness;

import model.IGenome;

public interface IFitnessCalculator<I extends IGenome> {

    public float CalculateFitness(I genome);

}
