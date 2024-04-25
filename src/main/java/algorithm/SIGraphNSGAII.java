package algorithm;

import algorithm.crossover.ICrossover;
import algorithm.crossover.SIGraphUniformCrossover;
import algorithm.fitness.IFitnessCalculator;
import algorithm.fitness.SegmentedImageFitness;
import algorithm.fitness.SegmentedImageMOOFitnessCalculator;
import algorithm.fitness.SegmentedImageWSFCalculator;
import algorithm.mutation.IMutation;
import algorithm.mutation.SIGraphRandomResetMutation;
import algorithm.parentselection.IParentSelection;
import algorithm.parentselection.SIFitnessProportionalProbabilityParentSelection;
import algorithm.parentselection.SISelectRandomParents;
import algorithm.selection.ISelection;
import algorithm.selection.NSGASelect;
import algorithm.selection.SISelectTopFitness;
import model.SIGraphDirection;
import model.SIGraphGenome;
import model.SIProblemInstance;
import utils.ImageUtils;
import utils.SegmentImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SIGraphNSGAII implements IGeneticAlgorithm<SIGraphGenome> {

    private final Collection<IMutation<SIGraphGenome>> Mutations;
    private final ICrossover<SIGraphGenome> Crossover;
    private final ISelection<SIGraphGenome> Select;
    private final IParentSelection<SIGraphGenome> ParentSelection;
    private final IFitnessCalculator<SIGraphGenome, SegmentedImageFitness> FitnessCalculator;
    private final SIProblemInstance ProblemInstance;

    private ArrayList<SIGraphGenome> Population;

    private final SegmentImageUtils SegmentImgUtils;

    public SIGraphNSGAII(ArrayList<SIGraphGenome> population, SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;
        this.Population = population;

        Mutations = List.of(
                new SIGraphRandomResetMutation()
        );
        Crossover = new SIGraphUniformCrossover();
        FitnessCalculator = new SegmentedImageMOOFitnessCalculator(ProblemInstance);
        Select = new NSGASelect();
        ParentSelection = new SISelectRandomParents();

        SegmentImgUtils = new SegmentImageUtils(ProblemInstance.ImageWidth, ProblemInstance.ImageHeight, problemInstance);
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

            for (int i = 0; i < nOffspring / 2; i++) {

                ArrayList<SIGraphGenome> offspring = Crossover(parentPool.getFirst(), parentPool.getLast());

                offspring.forEach(o -> Mutate(o, p_m));

                CalculateFitness(offspring);

                Population.addAll(offspring);
            }

            Population = Select.Select(Population, populationSize);

            for(int i = 1; i < Population.size(); i++) {
                Population.get(i).Fitness.SetFitness(i);
            }

            if(e % 1 == 0) {
                System.out.printf("Epoch: %s; Average fitness: %s; \n", e, GetAverageFitnessDescription() );
            }
        }

        // Select optimal solution
        double maxEdgeV = Population.stream().mapToDouble(i -> i.Fitness.EdgeValue).max().getAsDouble();
        double minEdgeV = Population.stream().mapToDouble(i -> i.Fitness.EdgeValue).min().getAsDouble();

        double maxDeviation = Population.stream().mapToDouble(i -> i.Fitness.OverallDeviation).max().getAsDouble();
        double minDeviation = Population.stream().mapToDouble(i -> i.Fitness.OverallDeviation).min().getAsDouble();

        double maxConnectivity = Population.stream().mapToDouble(i -> i.Fitness.ConnectivityMeasure).max().getAsDouble();
        double minConnectivity = Population.stream().mapToDouble(i -> i.Fitness.ConnectivityMeasure).min().getAsDouble();

        double bestKnee = Integer.MAX_VALUE;
        SIGraphGenome optimalSolution = null;
        for(SIGraphGenome i : Population) {
            double i_knee = 0.0f;
            i_knee += (i.Fitness.EdgeValue - minEdgeV) / (maxEdgeV - minEdgeV);
            i_knee += (i.Fitness.OverallDeviation - minDeviation) / (maxDeviation - minDeviation);
            i_knee += (i.Fitness.ConnectivityMeasure - minConnectivity) / (maxConnectivity - minConnectivity);

            if(i_knee < bestKnee) {
                optimalSolution = i;
                bestKnee = i_knee;
            }
        }

        String filename = String.format("%s_Segments%s_EdgeValue_%.0f_Conn_%s_Dev_%s_",
                ProblemInstance.InstanceId,
                optimalSolution.GetSegmentCount(),
                optimalSolution.Fitness.EdgeValue,
                optimalSolution.Fitness.ConnectivityMeasure,
                optimalSolution.Fitness.OverallDeviation);

        ImageUtils.SaveImage(filename + "_OPT_type1.png", SegmentImgUtils.GeneratedSegmentedOverlayImageType1(optimalSolution));
        ImageUtils.SaveImage(filename + "_OPT_type2.png", SegmentImgUtils.GeneratedSegmentedOverlayImageType2(optimalSolution));

        Random r = new Random();
        for(int i = 0; i < 5; i++) {
            SIGraphGenome ind = Population.remove(r.nextInt(Population.size()));
            filename = String.format("%s_Segments%s_EdgeValue_%.0f_Conn_%s_Dev_%s_",
                    ProblemInstance.InstanceId,
                    ind.GetSegmentCount(),
                    ind.Fitness.EdgeValue,
                    ind.Fitness.ConnectivityMeasure,
                    ind.Fitness.OverallDeviation);
            ImageUtils.SaveImage(filename + "_Pareto" + i + "_type1.png", SegmentImgUtils.GeneratedSegmentedOverlayImageType1(optimalSolution));
            ImageUtils.SaveImage(filename + "_Pareto" + i + "_type2.png", SegmentImgUtils.GeneratedSegmentedOverlayImageType2(optimalSolution));

        }
    }

    public void CalculateFitness(List<SIGraphGenome> pool) {
        pool.forEach(this::CalculateFitness);
    }

    public void CalculateFitness(SIGraphGenome individual) {
        individual.Fitness = FitnessCalculator.CalculateFitness(individual);
        //individual.PhenotypeImage = SegmentImgUtils.GeneratedSegmentedOverlayImageType2(individual);
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

    private String GetAverageFitnessDescription() {
        double edgeValue = Population.stream().mapToDouble(i -> i.Fitness.EdgeValue).average().getAsDouble();
        double connectivity = Population.stream().mapToDouble(i -> i.Fitness.ConnectivityMeasure).average().getAsDouble();
        double overallDeviation = Population.stream().mapToDouble(i -> i.Fitness.OverallDeviation).average().getAsDouble();
        double penalty = Population.stream().mapToDouble(i -> i.Fitness.SegmentPenalty).average().getAsDouble();

        return String.format("EdgeValue: %.2f; Connectivity: %.2f; OverallDeviation: %.2f; SegmentPenalty: %.2f;", edgeValue, connectivity, overallDeviation, penalty);
    }

    private String GetMinFitnessDescription() {
        double edgeValue = Population.stream().mapToDouble(i -> i.Fitness.EdgeValue).min().getAsDouble();
        double connectivity = Population.stream().mapToDouble(i -> i.Fitness.ConnectivityMeasure).min().getAsDouble();
        double overallDeviation = Population.stream().mapToDouble(i -> i.Fitness.OverallDeviation).min().getAsDouble();

        return String.format("EdgeValue: %.2f; Connectivity: %.2f; OverallDeviation: %.2f;", edgeValue, connectivity, overallDeviation);
    }
}
