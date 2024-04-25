package model;

import algorithm.fitness.IFitness;
import algorithm.fitness.SegmentedImageFitness;
import utils.SegmentImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SIGraphGenome implements IGenome {

    public SegmentedImageFitness Fitness;

    public ArrayList<SIGraphDirection> Genome;

    private ArrayList<Integer> Phenotype;
    public BufferedImage PhenotypeImage;
    private int SegmentCount;
    public int SmallSegmentCount;

    public final int Width;
    public final int Height;

    public SIGraphGenome(ArrayList<SIGraphDirection> genome, int width, int height) {
        this.Genome = genome;
        this.Width = width;
        this.Height = height;
    }

    public int GetSegmentCount() {
        if(Phenotype == null) { GeneratePhenotype(); }
        return SegmentCount;
    }

    public ArrayList<Integer> GetPhenotype() {

        if(Phenotype == null) {
            GeneratePhenotype();
        }

        /*return new ArrayList<Integer>(List.of(
                0, 0, 0, 0, 1, 1,
                0, 0, 0, 0, 1, 1,
                0, 0, 0, 0, 1, 1,
                0, 0, 0, 0, 1, 1,
                0, 0, 0, 0, 1, 1,
                2, 2, 2, 0, 1, 1,
                2, 2, 2, 0, 0, 0
        ));*/

        return Phenotype;
    }

    private void GeneratePhenotype() {
        int l = GetGenomeLength();

        ArrayList<SIGraphDirection> genomeCopy = new ArrayList<>(Genome);
        ArrayList<Integer> phenotype = new ArrayList<>(l);

        for(int i = 0; i < l; i++) { phenotype.add(-1); }

        int currSegment = 0;

        for(int i = 0; i < l; i++) {
            ArrayList<Integer> visitedNodes = new ArrayList<>();
            int genomeIndex = i;

            SIGraphDirection currDirection = genomeCopy.get(genomeIndex);

            if (currDirection == SIGraphDirection.Visited) {
                continue;
            }

            while (currDirection != SIGraphDirection.Visited && currDirection != SIGraphDirection.End) {
                genomeCopy.set(genomeIndex, SIGraphDirection.Visited);
                visitedNodes.add(genomeIndex);

                switch (currDirection) {
                    case Right -> genomeIndex += 1;
                    case Left -> genomeIndex -= 1;
                    case Up -> genomeIndex -= Width;
                    case Down -> genomeIndex += Width;
                }

                currDirection = genomeCopy.get(genomeIndex);
            }

            if(currDirection == SIGraphDirection.End) {
                genomeCopy.set(genomeIndex, SIGraphDirection.Visited);
                visitedNodes.add(genomeIndex);
            }

            int segment = currSegment;
            if (currDirection == SIGraphDirection.Visited && !visitedNodes.contains(genomeIndex)) {
                // Node has been visited before from another segment. We want to add all visited nodes to that segment
                segment = phenotype.get(genomeIndex);
            } else {
                currSegment++;
            }

            // Set the visited nodes to the correct segment
            for (int node : visitedNodes) {
                phenotype.set(node, segment);
            }
        }

        final int min_seg_size = 150;
        int[] segmentSize = new int[currSegment];

        for(int i = 0; i < phenotype.size(); i++) {
            segmentSize[phenotype.get(i)] += 1;
        }

        SmallSegmentCount = 0;
        for(int sSize : segmentSize) {
            if(sSize < min_seg_size) {
                SmallSegmentCount++;
            }
        }

        SegmentCount = currSegment;
        Phenotype = phenotype;
    }

    // Dominance check method
    public boolean Dominates(SIGraphGenome other) {
        boolean dom = (this.Fitness.EdgeValue <= other.Fitness.EdgeValue && this.Fitness.OverallDeviation <= other.Fitness.OverallDeviation && this.Fitness.ConnectivityMeasure <= other.Fitness.ConnectivityMeasure) &&
                (this.Fitness.EdgeValue <  other.Fitness.EdgeValue || this.Fitness.OverallDeviation <  other.Fitness.OverallDeviation || this.Fitness.ConnectivityMeasure > other.Fitness.ConnectivityMeasure);

        return dom;
    }

    @Override
    public IFitness GetFitness() {
        return Fitness;
    }

    @Override
    public int GetGenomeLength() {
        return Genome.size();
    }

    /*
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < GetGenomeLength(); i++) {
            if(i % Width == 0) {s += "\n"; }
            s += SIGraphDirection.ToStringSymbol(Genome.get(i));
        }

        return s;
    }
    */
}
