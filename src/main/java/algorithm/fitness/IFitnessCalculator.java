package algorithm.fitness;

import model.IGenome;

public interface IFitnessCalculator<I extends IGenome> {

    public IFitness CalculateFitness(I genome);

}
