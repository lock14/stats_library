package lock14.random.stats;

import lock14.random.distribution.Distribution;
import lock14.random.distribution.Gaussian;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Proportion {
    private static final Distribution<Double> NORMAL = new Gaussian();

    public static double[] confidenceInterval95(Collection<Double> counts) {
        return confidenceInterval(counts, 0.95);
    }

    public static double[] confidenceInterval99(Collection<Double> counts) {
        return confidenceInterval(counts, 0.99);
    }

    // returns (p_hat, standard error)
    // because Java does not support pairs/tuples, returns a two-element array
    public static double[] confidenceInterval(Collection<Double> counts, double confidenceLevel) {
        DescriptiveStatistics stats = new DescriptiveStatistics(counts);
        double pHat = stats.mean();
        double multiplier = NORMAL.inverseCdf((1 + confidenceLevel) / 2);
        double se = multiplier * StrictMath.sqrt((pHat * (1 - pHat)) / counts.size());
        return pair(pHat, se);
    }

    private static double[] pair(double first, double second) {
        double[] pair = new double[2];
        pair[0] = first;
        pair[1] = second;
        return pair;
    }

    public static void main(String[] args) {
        int numPositive = 220;
        int totalSample = 400;
        List<Double> counts = Stream.generate(() -> 1.0)
                                    .limit(numPositive)
                                    .collect(Collectors.toList());
        List<Double> negativeCount = Stream.generate(() -> 0.0)
                                           .limit(totalSample - numPositive)
                                           .collect(Collectors.toList());
        counts.addAll(negativeCount);
        double[] ci = confidenceInterval95(counts);
        System.out.println("95% confidence interval is : (" + ci[0] + ", " + ci[1] + ")");
    }
}
