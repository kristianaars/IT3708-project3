package algorithm;

import algorithm.crossover.ICrossover;
import algorithm.crossover.SIGraphOPCrossover;
import algorithm.crossover.SIGraphUniformCrossover;
import algorithm.crossover.SIOnePointCrossover;
import algorithm.fitness.IFitnessCalculator;
import algorithm.fitness.SegmentedImageWSFCalculator;
import algorithm.mutation.IMutation;
import algorithm.mutation.SICreepMutation;
import algorithm.mutation.SIGraphRandomResetMutation;
import algorithm.mutation.SIRandomResettingMutation;
import algorithm.parentselection.IParentSelection;
import algorithm.parentselection.SIFitnessProportionalProbabilityParentSelection;
import algorithm.parentselection.SISelectRandomParents;
import algorithm.selection.ISelection;
import algorithm.selection.SISelectTopFitness;
import model.SIGraphDirection;
import model.SIGraphGenome;
import model.SIProblemInstance;
import model.SegmentedImageGenome;
import utils.SegmentImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SIGraphSGA implements IGeneticAlgorithm<SIGraphGenome> {

    private final Collection<IMutation<SIGraphGenome>> Mutations;
    private final ICrossover<SIGraphGenome> Crossover;
    private final ISelection<SIGraphGenome> Select;
    private final IParentSelection<SIGraphGenome> ParentSelection;
    private final IFitnessCalculator<SIGraphGenome> FitnessCalculator;

    private final SIProblemInstance ProblemInstance;

    private ArrayList<SIGraphGenome> Population;

    private final SegmentImageUtils SegmentImgUtils;

    public SIGraphSGA(ArrayList<SIGraphGenome> population, SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;
        this.Population = population;

        Mutations = List.of(
                new SIGraphRandomResetMutation()
        );
        Crossover = new SIGraphUniformCrossover();
        FitnessCalculator = new SegmentedImageWSFCalculator(ProblemInstance);
        Select = new SISelectTopFitness();
        ParentSelection = new SISelectRandomParents();

        SegmentImgUtils = new SegmentImageUtils(ProblemInstance.ImageWidth, ProblemInstance.ImageHeight);
    }

    @Override
    public void Train(int epochs, float p_m) {
        int populationSize = Population.size();
        int nParents = 2;
        int nOffspring = populationSize;

        // Calculate initial fitness before training begins
        CalculateFitness(Population);

        for(int e = 0; e < epochs; e++) {
            ArrayList<SIGraphGenome> parentPool = ParentSelection.SelectParents(Population, nParents);
            //System.out.printf("Min fitness: %.3f; Max fitness: %.3f; Parents: %s;\n", GetMinPopulationFitness(), GetMaxPopulationFitness(), parentPool);

            for (int i = 0; i < nOffspring / 2; i++) {

                ArrayList<SIGraphGenome> offspring = Crossover(parentPool.getFirst(), parentPool.getLast());

                offspring.forEach(o -> Mutate(o, p_m));

                CalculateFitness(offspring);

                Population.addAll(offspring);
            }

            Population = Select.Select(Population, populationSize);

            if(e % 1 == 0) {
                BufferedImage sampleImg = SegmentImgUtils.GenerateSegmentedImage(Population.getFirst());

                int seg = (int) Population.getFirst().Genome.stream().filter(g -> g == SIGraphDirection.End).count();

                System.out.printf("Epoch: %s; Average fitness: %.3f; Min fitness: %.3f; Max fitness: %.3f;s\n",
                        e,
                        GetAveragePopulationFitness(),
                        GetMinPopulationFitness(),
                        GetMaxPopulationFitness());
            }
        }
    }

    @Override
    public ArrayList<SIGraphGenome> Crossover(SIGraphGenome parent1, SIGraphGenome parent2) {
        return Crossover.Crossover(parent1, parent2);
    }

    @Override
    public void Mutate(SIGraphGenome individual, float p_m) {
        Mutations.forEach(m -> {
            m.mutate(individual, p_m);
        });
    }

    public void CalculateFitness(List<SIGraphGenome> pool) {
        pool.forEach(this::CalculateFitness);
    }

    public void CalculateFitness(SIGraphGenome individual) {
        individual.Fitness = FitnessCalculator.CalculateFitness(individual);
    }

    public float GetAveragePopulationFitness() {
        return (float) Population.stream().mapToDouble(i -> i.Fitness.GetFitness()).average().orElse(0);
    }

    public float GetMaxPopulationFitness() {
        return (float) Population.stream().mapToDouble(i -> i.Fitness.GetFitness()).max().orElse(0);
    }

    public float GetMinPopulationFitness() {
        return (float) Population.stream().mapToDouble(i -> i.Fitness.GetFitness()).min().orElse(0);
    }

}
