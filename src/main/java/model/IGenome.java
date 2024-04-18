package model;

import algorithm.fitness.IFitness;

public interface IGenome {

    IFitness GetFitness();

    int GetGenomeLength();
}
