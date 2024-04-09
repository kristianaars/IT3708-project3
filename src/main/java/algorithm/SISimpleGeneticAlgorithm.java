package algorithm;

import algorithm.crossover.ICrossover;
import algorithm.crossover.SIOnePointCrossover;
import algorithm.fitness.IFitnessCalculator;
import algorithm.fitness.SegmentedImageWSFCalculator;
import algorithm.mutation.IMutation;
import algorithm.mutation.SICreepMutation;
import algorithm.mutation.SIRandomResettingMutation;
import model.SegmentedImageGenome;

import java.util.Collection;
import java.util.List;

public class SISimpleGeneticAlgorithm implements IGeneticAlgorithm<SegmentedImageGenome> {

    private final Collection<IMutation<SegmentedImageGenome>> Mutations;
    private final ICrossover<SegmentedImageGenome> Crossover;
    private final IFitnessCalculator<SegmentedImageGenome> FitnessCalculator;

    public SISimpleGeneticAlgorithm() {
        Mutations = List.of(
                new SICreepMutation(),
                new SIRandomResettingMutation()
        );
        Crossover = new SIOnePointCrossover();
        FitnessCalculator = new SegmentedImageWSFCalculator();
    }

    @Override
    public void Train(int epochs, float targetFitness, float p_m, int nCrossoverThreads, float overSelectionX) {

    }

    @Override
    public Collection<SegmentedImageGenome> Crossover(SegmentedImageGenome parent1, SegmentedImageGenome parent2) {
        return Crossover.Crossover(parent1, parent2);
    }

    @Override
    public void Mutate(SegmentedImageGenome individual, float p_m) {
        Mutations.forEach(m -> {
            boolean didMutate = m.mutate(individual, p_m);

            if(didMutate) {
                individual.Fitness = FitnessCalculator.CalculateFitness(individual);
            }
        });
    }
}
