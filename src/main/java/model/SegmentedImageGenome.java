package model;

import algorithm.fitness.IFitness;

import java.util.ArrayList;

public class SegmentedImageGenome implements IGenome {

    public IFitness Fitness;

    public ArrayList<Integer> Genome;

    public SegmentedImageGenome(ArrayList<Integer> genome) {
        this.Genome = genome;
    }

    @Override
    public IFitness GetFitness() {
        return Fitness;
    }

    @Override
    public int GetGenomeLength() {
        return Genome.size();
    }

}
