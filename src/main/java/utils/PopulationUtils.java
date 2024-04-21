package utils;

import model.SIGraphDirection;
import model.SIGraphGenome;
import model.SIProblemInstance;
import model.SegmentedImageGenome;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class PopulationUtils {

    private final SIProblemInstance ProblemInstance;

    public PopulationUtils(SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;
    }

    public ArrayList<SIGraphGenome> GeneratePopulation(int populationSize) {
        ArrayList<SIGraphGenome> population = new ArrayList<>(populationSize);
        for(int i = 0; i < populationSize; i++) {
            population.add(GenerateIndividual());
        }

        return population;
    }

    private SIGraphGenome GenerateIndividual() {
        int width = ProblemInstance.ImageWidth;
        int height = ProblemInstance.ImageHeight;
        int n = width * height;

        Graph<Integer, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add all nodes
        for(int i = 0; i < n; i++) {
            graph.addVertex(i);
        }

        // Add edged between nodes
        for(int i = 0; i < n; i++) {
            int left_i = i - 1;
            int right_i = i + 1;
            int up_i = i - width;
            int down_i = i + width;

            if(left_i > 0) { CalculateAndAddEdge(i, left_i, graph); }
            if(right_i < n) { CalculateAndAddEdge(i, right_i, graph); }
            if(up_i > 0) { CalculateAndAddEdge(i, up_i, graph); }
            if(down_i < n) { CalculateAndAddEdge(i, down_i, graph); }
        }

        // Apply Prim's algorithm to find the minimum spanning tree
        SpanningTreeAlgorithm.SpanningTree<DefaultWeightedEdge> mst = new PrimMinimumSpanningTree<>(graph).getSpanningTree();

        ArrayList<SIGraphDirection> genome = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            //genome.add(SIGraphDirection.End);

            if(i - 1 > 0) {
                genome.add(SIGraphDirection.Left);
            } else {
                genome.add(SIGraphDirection.End);
            }
        }

        for(DefaultWeightedEdge edge : mst.getEdges()) {
            int s = graph.getEdgeSource(edge);
            int t = graph.getEdgeTarget(edge);

            int d = t - s;

            if(d == 1 && s - 1 > 0) { genome.set(s, SIGraphDirection.Left); }
            else if(d == -1 && s + 1 < n) { genome.set(s, SIGraphDirection.Right); }
            else if(d == width && s - width > 0) { genome.set(s, SIGraphDirection.Up); }
            else if(d == -width && s + width < n) { genome.set(s, SIGraphDirection.Down); }
        }

        return new SIGraphGenome(genome, width, height);
    }

    private void CalculateAndAddEdge(int source, int target, Graph<Integer, DefaultWeightedEdge> graph) {
        DefaultWeightedEdge e = graph.addEdge(source, target);
        if(e!=null) {
            // This means edge has already been added the other way
            graph.setEdgeWeight(e, CalculateWeight(source, target));
        }
    }

    private float CalculateWeight(int pixel_i1, int pixel_i2) {
        int width = ProblemInstance.ImageWidth;
        BufferedImage image = ProblemInstance.TestImage;

        int x1 = pixel_i1 % width;
        int y1 = pixel_i1 / width;

        int x2 = pixel_i2 % width;
        int y2 = pixel_i2 / width;

        int[] rgb1 = ImageUtils.GetRGBArray(image, x1, y1);
        int[] rgb2 = ImageUtils.GetRGBArray(image, x2, y2);

        return ImageUtils.RGBDistance(rgb1, rgb2);
    }


    public static ArrayList<SegmentedImageGenome> GeneratePopulation(int populationSize, int genomeLength, int maxSegments) {
        ArrayList<SegmentedImageGenome> population = new ArrayList<>(genomeLength);
        for(int i = 0; i < populationSize; i++) {
            population.add(GenerateIndividual(genomeLength, maxSegments));
        }

        return population;
    }

    public static SegmentedImageGenome GenerateIndividual(int genomeLength, int maxSegments) {
        ArrayList<Integer> genome = new ArrayList<>(genomeLength);

        Random random = new Random();

        for(int i = 0; i < genomeLength; i++) {
            genome.add(random.nextInt(maxSegments + 1));
        }

        return new SegmentedImageGenome(genome);
    }

}
