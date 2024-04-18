import algorithm.SISimpleGeneticAlgorithm;
import model.SIProblemInstance;

import java.io.IOException;
import java.util.List;

public class MainClass {

    public static void main(String[] args) throws IOException {
        final int maxSegmentCount = 20;

        List<SIProblemInstance> problemInstances = List.of(
                new SIProblemInstance(86016, maxSegmentCount),
                new SIProblemInstance(118035, maxSegmentCount),
                new SIProblemInstance(147091, maxSegmentCount),
                new SIProblemInstance(176035, maxSegmentCount),
                new SIProblemInstance(176039, maxSegmentCount),
                new SIProblemInstance(353013, maxSegmentCount)
        );

        SIProblemInstance problemInstance = problemInstances.getFirst();

        SISimpleGeneticAlgorithm alg = new SISimpleGeneticAlgorithm(problemInstance);

    }
}
