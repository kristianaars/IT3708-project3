package model;

import java.util.ArrayList;
import java.util.Collection;

public class SegmentedImageGenome implements IGenome {

    public float Fitness;

    private ArrayList<Integer> genome;

    public SegmentedImageGenome(Collection<Integer> genome) {
        this.genome = new ArrayList<>(genome);
        Fitness = -1;
    }


}
