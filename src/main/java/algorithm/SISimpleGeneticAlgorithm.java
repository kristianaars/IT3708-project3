package algorithm;

import algorithm.crossover.ICrossover;
import algorithm.crossover.SIOnePointCrossover;
import algorithm.fitness.IFitnessCalculator;
import algorithm.fitness.SegmentedImageWSFCalculator;
import algorithm.mutation.IMutation;
import algorithm.mutation.SICreepMutation;
import algorithm.mutation.SIRandomResettingMutation;
import algorithm.parentselection.IParentSelection;
import algorithm.parentselection.SIFitnessProportionalProbabilityParentSelection;
import algorithm.selection.ISelection;
import algorithm.selection.SISelectTopFitness;
import model.SIProblemInstance;
import model.SegmentedImageGenome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SISimpleGeneticAlgorithm implements IGeneticAlgorithm<SegmentedImageGenome> {

    private final Collection<IMutation<SegmentedImageGenome>> Mutations;
    private final ICrossover<SegmentedImageGenome> Crossover;
    private final ISelection<SegmentedImageGenome> Select;
    private final IParentSelection<SegmentedImageGenome> ParentSelection;
    private final IFitnessCalculator<SegmentedImageGenome> FitnessCalculator;

    private final SIProblemInstance ProblemInstance;

    private ArrayList<SegmentedImageGenome> Population;

    public SISimpleGeneticAlgorithm(ArrayList<SegmentedImageGenome> population, SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;
        this.Population = population;

        Mutations = List.of(
                new SICreepMutation(ProblemInstance),
                new SIRandomResettingMutation()
        );
        Crossover = new SIOnePointCrossover();
        FitnessCalculator = new SegmentedImageWSFCalculator(ProblemInstance);
        Select = new SISelectTopFitness();
        ParentSelection = new SIFitnessProportionalProbabilityParentSelection();
    }

    @Override
    public void Train(int epochs, float p_m) {
        int populationSize = Population.size();
        int nParents = 2;
        int nOffspring = populationSize;

        for(int e = 0; e < epochs; e++) {
            ArrayList<SegmentedImageGenome> parentPool = ParentSelection.SelectParents(Population, nParents);
            //System.out.printf("Min fitness: %.3f; Max fitness: %.3f; Parents: %s;\n", GetMinPopulationFitness(), GetMaxPopulationFitness(), parentPool);

            for (int i = 0; i < nOffspring / 2; i++) {

                ArrayList<SegmentedImageGenome> offspring = Crossover(parentPool.getFirst(), parentPool.getLast());

                offspring.forEach(o -> Mutate(o, p_m));

                CalculateFitness(offspring);

                Population.addAll(offspring);
            }

            Population = Select.Select(Population, populationSize);

            /*if(e % 2500 == 0) {
                System.out.printf("Epoch: %s; Average fitness: %.3f; Min fitness: %.3f; Max fitness: %.3f; Best Solution: %s\n",
                        e,
                        GetAveragePopulationFitness(),
                        GetMinPopulationFitness(),
                        GetMaxPopulationFitness(),
                        GetBestSolution());
            }*/
        }
    }

    @Override
    public ArrayList<SegmentedImageGenome> Crossover(SegmentedImageGenome parent1, SegmentedImageGenome parent2) {
        return Crossover.Crossover(parent1, parent2);
    }

    @Override
    public void Mutate(SegmentedImageGenome individual, float p_m) {
        Mutations.forEach(m -> {
            m.mutate(individual, p_m);
        });
    }

    public void CalculateFitness(List<SegmentedImageGenome> pool) {
        pool.forEach(this::CalculateFitness);
    }

    public void CalculateFitness(SegmentedImageGenome individual) {
        individual.Fitness = FitnessCalculator.CalculateFitness(individual);
    }

}
