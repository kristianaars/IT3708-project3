package algorithm.selection;

import model.IGenome;

import java.util.ArrayList;

public interface ISelection<I extends IGenome> {

    /***
     * Selects mu members from population
     *
     * @param population Population to select the members from
     * @param mu The amount of members to select
     * @return mu members from population
     */
    public ArrayList<I> Select(ArrayList<I> population, int mu);
}
