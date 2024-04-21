package model;

import algorithm.fitness.IFitness;

import java.util.ArrayList;

public class SIGraphGenome implements IGenome {

    public IFitness Fitness;

    public ArrayList<SIGraphDirection> Genome;

    private ArrayList<Integer> Phenotype;
    private int SegmentCount;

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
                if(!visitedNodes.isEmpty()) {
                    currSegment++;
                }
            }

            // Set the visited nodes to the correct segment
            for (int node : visitedNodes) {
                phenotype.set(node, segment);
            }
        }


        SegmentCount = currSegment;
        Phenotype = phenotype;
    }

    @Override
    public IFitness GetFitness() {
        return Fitness;
    }

    @Override
    public int GetGenomeLength() {
        return Genome.size();
    }


}
