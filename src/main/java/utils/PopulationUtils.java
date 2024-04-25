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
import java.util.*;
import java.util.stream.Collectors;

public class PopulationUtils {

    private final Random RandomInstance;

    private final SIProblemInstance ProblemInstance;

    public PopulationUtils(SIProblemInstance problemInstance) {
        this.ProblemInstance = problemInstance;
        this.RandomInstance = new Random();
    }

    public ArrayList<SIGraphGenome> GeneratePopulation(int populationSize, int minSegmentCount, int maxSegmentCount) {
        ArrayList<SIGraphGenome> population = new ArrayList<>(populationSize);
        for(int i = 0; i < populationSize; i++) {
            population.add(GenerateIndividual(minSegmentCount, maxSegmentCount));
        }

        return population;
    }

    private SIGraphGenome GenerateIndividual(int minSegmentCount, int maxSegmentCount) {
        int width = ProblemInstance.ImageWidth;
        int height = ProblemInstance.ImageHeight;
        int n = width * height;

        Graph<Integer, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

        int segmentCount = RandomInstance.nextInt(minSegmentCount, maxSegmentCount + 1);
        int nodeOffset = RandomInstance.nextInt(n);

        // Add all nodes
        for(int i = 0; i < n; i++) {
            graph.addVertex((i + nodeOffset) % n);
        }

        // Add edged between nodes
        for(int i = 0; i < n; i++) {
            int left_i = i - 1;
            int right_i = i + 1;
            int up_i = i - width;
            int down_i = i + width;

            if(i % width != 0) { CalculateAndAddEdge(i, left_i, graph); }
            if(i % width != (width - 1)) { CalculateAndAddEdge(i, right_i, graph); }
            if(up_i > 0) { CalculateAndAddEdge(i, up_i, graph); }
            if(down_i < n) { CalculateAndAddEdge(i, down_i, graph); }
        }

        // Apply Prim's algorithm to find the minimum spanning tree
        SpanningTreeAlgorithm.SpanningTree<DefaultWeightedEdge> mst = new PrimMinimumSpanningTree<>(graph).getSpanningTree();

        ArrayList<SIGraphDirection> genome = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            genome.add(SIGraphDirection.End);
        }

        ArrayList<DefaultWeightedEdge> mstTreeEdges = new ArrayList<>(
                mst.getEdges()
                .stream()
                .sorted(Comparator.comparingDouble(graph::getEdgeWeight))
                .limit(n - (segmentCount))
                .toList());

        Graph<Integer, DefaultWeightedEdge> mstGraph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for(Integer v : graph.vertexSet()) {
            mstGraph.addVertex(v);
        }

        for(DefaultWeightedEdge e : mstTreeEdges) {
            mstGraph.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e));
        }

        List<Integer> leafNodes = mstGraph.vertexSet().stream()
                .filter(key -> mstGraph.outgoingEdgesOf(key).size() == 1).toList();

        // First, specify all leaf nodes
        for(Integer v : leafNodes) {
            DefaultWeightedEdge e = mstGraph.outgoingEdgesOf(v).stream().findFirst().orElse(null);

            if(e!=null) {
                int s = v;
                int t = mstGraph.getEdgeSource(e) == s ? mstGraph.getEdgeTarget(e) : mstGraph.getEdgeSource(e);

                int d = t - s;

                genome.set(s, TranslateDirection(d, width));
                mstGraph.removeEdge(e);
            }

        }

        // Then start at each leaf node, and build subgraph until another existing graph blocks the way
        for(int i = 0; i < leafNodes.size(); i++) {
            int s = TraverseDirection(leafNodes.get(i), genome.get(leafNodes.get(i)), width, n);

            BuildSubGraph(s, width, genome, mstGraph, false);
        }

        return new SIGraphGenome(genome, width, height);
    }

    private void BuildSubGraph(int s, int w, ArrayList<SIGraphDirection> genome, Graph<Integer, DefaultWeightedEdge> graph, boolean allowEnd) {
        if(!allowEnd && genome.get(s) != SIGraphDirection.End) { return; }

        Set<DefaultWeightedEdge> allowedEdges = graph.outgoingEdgesOf(s);

        if(allowedEdges.size() == 1) {
            DefaultWeightedEdge e = allowedEdges.stream().findAny().orElse(null);
            int t = graph.getEdgeSource(e) == s ? graph.getEdgeTarget(e) : graph.getEdgeSource(e);

            genome.set(s, TranslateDirection(t - s, w));
            graph.removeEdge(e);
            BuildSubGraph(t, w, genome, graph, allowEnd);
        }

    }

    private SIGraphDirection TranslateDirection(int d, int w) {
        if(d == 1) { return SIGraphDirection.Right; }
        else if(d == -1) { return SIGraphDirection.Left; }
        else if(d == -w) { return SIGraphDirection.Up; }
        else if(d == w) { return SIGraphDirection.Down; }
        else {
            return null;
        }
    }

    private int TraverseDirection(int i, SIGraphDirection d, int w, int n) {
        return switch (d) {
            case Right -> i + 1;
            case Left -> i - 1;
            case Up -> i - w;
            case Down -> i + w;
            default -> i;
        };
    }

    private void CalculateAndAddEdge(int source, int target, Graph<Integer, DefaultWeightedEdge> graph) {
        DefaultWeightedEdge e = graph.addEdge(source, target);
        if(e!=null) {
            // This means edge has already been added the other direction
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


}
