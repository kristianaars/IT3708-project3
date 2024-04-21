package algorithm.selection;
import model.SIGraphGenome;
import model.SegmentedImageGenome;

import java.util.ArrayList;

public class SISelectTopFitness implements ISelection<SIGraphGenome> {

    /***
     * Selects top mu members from population based on who has the highest fitness value
     * @param population Population to select the members from
     * @param mu The amount of members to select
     * @return Top mu members from population
     */
    @Override
    public ArrayList<SIGraphGenome> Select(ArrayList<SIGraphGenome> population, int mu) {
        return new ArrayList<>(population
                .stream()
                .sorted((i, j) ->
                        Float.compare(
                            i.GetFitness().GetFitness(),
                            j.GetFitness().GetFitness()))
                .limit(mu)
                .toList());
    }
}
