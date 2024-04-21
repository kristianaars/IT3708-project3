package algorithm.parentselection;

import model.SIGraphGenome;

import java.util.ArrayList;
import java.util.Random;

public class SISelectRandomParents implements IParentSelection<SIGraphGenome> {

    private final Random RandomInstance;

    public SISelectRandomParents() {
        this.RandomInstance = new Random();
    }


    @Override
    public ArrayList<SIGraphGenome> SelectParents(ArrayList<SIGraphGenome> population, int delta) {
        ArrayList<SIGraphGenome> parents = new ArrayList<>(delta);
        int p_size = population.size();

        for(int i = 0; i < delta; i++) {
            parents.add(population.get(RandomInstance.nextInt(p_size)));
        }


        return parents;
    }
}
