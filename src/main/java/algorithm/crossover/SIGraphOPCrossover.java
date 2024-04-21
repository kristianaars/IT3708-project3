package algorithm.crossover;

import model.SIGraphDirection;
import model.SIGraphGenome;
import model.SegmentedImageGenome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SIGraphOPCrossover implements ICrossover<SIGraphGenome> {

    private final Random RandomInstance;

    public SIGraphOPCrossover() {
        RandomInstance = new Random();
    }

    @Override
    public ArrayList<SIGraphGenome> Crossover(SIGraphGenome parent1, SIGraphGenome parent2) {
        int l = parent1.GetGenomeLength();
        int crossoverPoint = RandomInstance.nextInt(l);

        ArrayList<SIGraphDirection> oGenome1 = new ArrayList<>();
        ArrayList<SIGraphDirection> oGenome2 = new ArrayList<>();

        oGenome1.addAll(new ArrayList<>(parent1.Genome.subList(0, crossoverPoint)));
        oGenome1.addAll(new ArrayList<>(parent2.Genome.subList(crossoverPoint, l)));

        oGenome2.addAll(new ArrayList<>(parent2.Genome.subList(0, crossoverPoint)));
        oGenome2.addAll(new ArrayList<>(parent1.Genome.subList(crossoverPoint, l)));

        return new ArrayList<>(List.of(
                new SIGraphGenome(oGenome1, parent1.Width, parent2.Height),
                new SIGraphGenome(oGenome2, parent1.Width, parent2.Height)
        ));
    }
}
