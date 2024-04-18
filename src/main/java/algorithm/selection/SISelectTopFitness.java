package algorithm.selection;
import model.SegmentedImageGenome;

import java.util.ArrayList;

public class SISelectTopFitness implements ISelection<SegmentedImageGenome> {

    /***
     * Selects top mu members from population based on who has the highest fitness value
     * @param population Population to select the members from
     * @param mu The amount of members to select
     * @return Top mu members from population
     */
    @Override
    public ArrayList<SegmentedImageGenome> Select(ArrayList<SegmentedImageGenome> population, int mu) {
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
