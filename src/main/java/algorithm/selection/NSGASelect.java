package algorithm.selection;

import model.SIGraphGenome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class NSGASelect implements ISelection<SIGraphGenome> {

    public static final int EDGE_VALUE_I = 0;
    public static final int CONNECTIVITY_MEASURE_I = 1;
    public static final int OVERALL_DEVIATION_I = 2;

    @Override
    public ArrayList<SIGraphGenome> Select(ArrayList<SIGraphGenome> population, int mu) {
        return NonDominatedSorting(population, mu);
    }

    private ArrayList<SIGraphGenome> NonDominatedSorting(ArrayList<SIGraphGenome> population, int mu) {
        ArrayList<SIGraphGenome> R = new ArrayList<>();
        int M = 3; // Number of features

        while (R.size() < mu) {
            ArrayList<SIGraphGenome> paretoFront = GetParetoFront(population);
            population.removeAll(paretoFront);

            if(R.size() + paretoFront.size() < mu) {
                R.addAll(paretoFront);
            } else {
                // R do not have space for the last front and we need to select w
                // n is the number of individuals we can select from the last front
                int l = paretoFront.size();
                int n = mu - R.size();

                float[] d = new float[l];
                Arrays.fill(d, 0);

                float[] max_f = new float[M];
                float[] min_f = new float[M];

                max_f[EDGE_VALUE_I] = (float) paretoFront.stream().mapToDouble(i -> i.Fitness.EdgeValue).max().orElse(-1);;
                max_f[CONNECTIVITY_MEASURE_I] = (float) paretoFront.stream().mapToDouble(i -> i.Fitness.ConnectivityMeasure).max().orElse(-1);
                max_f[OVERALL_DEVIATION_I] = (float) paretoFront.stream().mapToDouble(i -> i.Fitness.OverallDeviation).max().orElse(-1);;

                min_f[EDGE_VALUE_I] = (float) paretoFront.stream().mapToDouble(i -> i.Fitness.EdgeValue).min().orElse(-1);;
                min_f[CONNECTIVITY_MEASURE_I] = (float) paretoFront.stream().mapToDouble(i -> i.Fitness.ConnectivityMeasure).min().orElse(-1);
                min_f[OVERALL_DEVIATION_I] = (float) paretoFront.stream().mapToDouble(i -> i.Fitness.OverallDeviation).min().orElse(-1);;

                // I is the sorted indices of the pareto front given feature m
                Integer[][] I = new Integer[M][l];
                for(int m = 0; m < M; m++) {
                    I[m] = IntStream.range(0, l).boxed().toArray(Integer[]::new);
                }

                Arrays.sort(I[EDGE_VALUE_I], Comparator.<Integer>comparingDouble(i -> paretoFront.get(i).Fitness.EdgeValue).reversed());
                Arrays.sort(I[CONNECTIVITY_MEASURE_I], Comparator.<Integer>comparingDouble(i -> paretoFront.get(i).Fitness.ConnectivityMeasure).reversed());
                Arrays.sort(I[OVERALL_DEVIATION_I], Comparator.<Integer>comparingDouble(i -> paretoFront.get(i).Fitness.OverallDeviation).reversed());

                for(int m = 0; m < M; m++) {
                    d[I[m][0]] = d[I[m][l-1]] = Integer.MAX_VALUE - 1;

                    for(int j = 1; j < (l - 1); j++) {
                        float f_1 = 0, f_2 = 0;

                        switch (m) {
                            case EDGE_VALUE_I -> {
                                f_1 = paretoFront.get(I[m][j+1]).Fitness.EdgeValue;
                                f_2 = paretoFront.get(I[m][j-1]).Fitness.EdgeValue;
                            }
                            case CONNECTIVITY_MEASURE_I -> {
                                f_1 = paretoFront.get(I[m][j+1]).Fitness.ConnectivityMeasure;
                                f_2 = paretoFront.get(I[m][j-1]).Fitness.ConnectivityMeasure;
                            }
                            case OVERALL_DEVIATION_I -> {
                                f_1 = paretoFront.get(I[m][j+1]).Fitness.OverallDeviation;
                                f_2 = paretoFront.get(I[m][j-1]).Fitness.OverallDeviation;
                            }
                        }

                        d[I[m][j]] += (f_1 - f_2) / (max_f[m] - min_f[m]);
                    }

                }

                Integer[] I_d = IntStream.range(0, l).boxed().toArray(Integer[]::new);
                Arrays.sort(I_d, Comparator.<Integer>comparingDouble(i -> d[i]).reversed());

                for(int i = 0; i < n; i++) {
                    R.add(paretoFront.get(I_d[i]));
                }

            }

        }

        return R;
    }

    private ArrayList<SIGraphGenome> GetParetoFront(ArrayList<SIGraphGenome> population) {
        ArrayList<SIGraphGenome> nonDominated = new ArrayList<>();

        // Use a set to ensure uniqueness in non-dominated solutions
        List<SIGraphGenome> dominatedSet = new ArrayList<>();

        for (SIGraphGenome d : population) {
            boolean dominated = false;

            for (SIGraphGenome r : population) {
                if (r.Dominates(d)) {
                    dominated = true;
                    break;
                }
            }

            if (!dominated) {
                nonDominated.add(d);
            } else {
                // If 'd' is dominated, add it to the dominated set
                dominatedSet.add(d);
            }
        }

        // Remove dominated solutions from the non-dominated list
        nonDominated.removeAll(dominatedSet);

        return nonDominated;
    }


}
