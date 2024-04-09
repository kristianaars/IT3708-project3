package model;

import java.util.ArrayList;
import java.util.Collection;

public class SegmentedImageGenome implements IGenome {

    public float Fitness;

    public ArrayList<Integer> genome;

    public SegmentedImageGenome(ArrayList<Integer> genome) {
        this.genome = genome;
        Fitness = -1;
    }

    @Override
    public int GetGenomeLength() {
        return genome.size();
    }
}
