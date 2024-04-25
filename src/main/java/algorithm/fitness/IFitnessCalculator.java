package algorithm.fitness;

import model.IGenome;

public interface IFitnessCalculator<I extends IGenome, J extends IFitness> {

    public J CalculateFitness(I genome);

}
