package algorithm.crossover;

import model.SIGraphDirection;
import model.SIGraphGenome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SIGraphUniformCrossover implements ICrossover<SIGraphGenome> {

    private final Random RandomInstance;

    public SIGraphUniformCrossover() {
        this.RandomInstance = new Random();
    }

    @Override
    public ArrayList<SIGraphGenome> Crossover(SIGraphGenome parent1, SIGraphGenome parent2) {
        int l = parent1.GetGenomeLength();

        ArrayList<SIGraphDirection> oGenome1 = new ArrayList<>();
        ArrayList<SIGraphDirection> oGenome2 = new ArrayList<>();

        for(int i = 0; i < l; i++) {
            if(RandomInstance.nextBoolean()) {
                oGenome1.add(parent1.Genome.get(i));
                oGenome2.add(parent2.Genome.get(i));
            } else {
                oGenome1.add(parent2.Genome.get(i));
                oGenome2.add(parent1.Genome.get(i));
            }
        }

        return new ArrayList<>(List.of(
                new SIGraphGenome(oGenome1, parent1.Width, parent2.Height),
                new SIGraphGenome(oGenome2, parent1.Width, parent2.Height)
        ));
    }
}
