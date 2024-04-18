package algorithm.parentselection;

import model.IGenome;

import java.util.ArrayList;

public interface IParentSelection<I extends IGenome> {

    ArrayList<I> SelectParents(ArrayList<I> population, int delta);
}
