import algorithm.IGeneticAlgorithm;
import algorithm.SIGraphNSGAII;
import algorithm.SIGraphSGA;
import model.SIGraphGenome;
import model.SIProblemInstance;
import utils.PopulationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) throws IOException {
        final int maxSegmentCount = 5;

        List<SIProblemInstance> problemInstances = List.of(
                new SIProblemInstance(1, maxSegmentCount),
                new SIProblemInstance(5, maxSegmentCount),
                new SIProblemInstance(86016, maxSegmentCount),
                new SIProblemInstance(118035, maxSegmentCount),
                new SIProblemInstance(147091, maxSegmentCount),
                new SIProblemInstance(176035, maxSegmentCount),
                new SIProblemInstance(176039, maxSegmentCount),
                new SIProblemInstance(353013, maxSegmentCount)
        );

        SIProblemInstance problemInstance = problemInstances.get(3);

        PopulationUtils populationUtils = new PopulationUtils(problemInstance);

        ArrayList<SIGraphGenome> population = populationUtils.GeneratePopulation(50, 2, 100);

        IGeneticAlgorithm<SIGraphGenome> alg = new SIGraphSGA(population, problemInstance);

        alg.Train(350, 0.00015f);
    }
}
