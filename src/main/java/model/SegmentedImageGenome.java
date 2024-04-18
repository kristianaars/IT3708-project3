package model;

import algorithm.fitness.IFitness;

import java.util.ArrayList;

public class SegmentedImageGenome implements IGenome {

    public IFitness Fitness;

    public ArrayList<Integer> genome;

    public SegmentedImageGenome(ArrayList<Integer> genome) {
        this.genome = genome;
    }

    @Override
    public IFitness GetFitness() {
        return Fitness;
    }

    @Override
    public int GetGenomeLength() {
        return genome.size();
    }
}
