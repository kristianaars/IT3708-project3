package algorithm.crossover;

import model.SegmentedImageGenome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SIOnePointCrossover implements ICrossover<SegmentedImageGenome> {

    private final Random RandomInstance;

    public SIOnePointCrossover() {
        RandomInstance = new Random();
    }

    @Override
    public ArrayList<SegmentedImageGenome> Crossover(SegmentedImageGenome parent1, SegmentedImageGenome parent2) {
        int l = parent1.GetGenomeLength();
        int crossoverPoint = RandomInstance.nextInt(l);

        ArrayList<Integer> oGenome1 = new ArrayList<>();
        ArrayList<Integer> oGenome2 = new ArrayList<>();

        oGenome1.addAll(new ArrayList<>(parent1.Genome.subList(0, crossoverPoint)));
        oGenome1.addAll(new ArrayList<>(parent2.Genome.subList(crossoverPoint, l)));

        oGenome2.addAll(new ArrayList<>(parent2.Genome.subList(0, crossoverPoint)));
        oGenome2.addAll(new ArrayList<>(parent1.Genome.subList(crossoverPoint, l)));

        return new ArrayList<>(List.of(
                new SegmentedImageGenome(oGenome1),
                new SegmentedImageGenome(oGenome2)
        ));
    }

}
