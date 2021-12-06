package lock14.random.stats;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class DescriptiveStatistics {
    private final List<Double> samples;
    private Double mean;
    private Double geometricMean;
    private Double variance;
    private Double populationVariance;
    private Double median;
    private Double Q1;
    private Double Q3;
    private Double kurtosis;
    private Double skewness;
    private Double min;
    private Double max;
    private Double excessKurtosis;

    public <N extends Number> DescriptiveStatistics(Iterable<N> samples) {
        // This is useful even if N is Double type as we need to make
        // a defensive copy of the collection anyway
        this.samples = new LinkedList<>();
        samples.forEach(sample -> this.samples.add(sample.doubleValue()));
        Collections.sort(this.samples);
        reset();
    }

    public void addSample(double x) {
        // do a sorted insert
        ListIterator<Double> itr = samples.listIterator();
        // set to x + 1 so first condition of while loop is
        // always met
        double val = x + 1;
        while (x < val && itr.hasNext()) {
            val = itr.next();
        }
        if (itr.hasPrevious()) {
            itr.previous();
        }
        itr.add(x);
        reset();
    }

    public List<Double> sortedSamples() {
        return Collections.unmodifiableList(samples);
    }

    public int numSamples() {
        return samples.size();
    }

    public double mean() {
        if (mean == null) {
            mean = samples.stream()
                          .mapToDouble(Double::doubleValue)
                          .average()
                          .orElse(0);
        }
        return mean;
    }

    public double geometricMean() {
        if (geometricMean == null) {
            double prod = samples.stream()
                                 .mapToDouble(Double::doubleValue)
                                 .reduce(1.0, (x1, x2) -> x1 * x2);
            double pow = 1.0 / samples.size();
            geometricMean = StrictMath.pow(prod, pow);
        }
        return geometricMean;
    }

    public double variance() {
        if (variance == null) {
            variance = samples.stream()
                              .mapToDouble(x -> StrictMath.pow(x - mean(), 2))
                              .sum() / (samples.size() - 1);
        }
        return variance;
    }

    public double populationVariance() {
        if (populationVariance == null) {
            populationVariance = nthMomentAboutMean(2);
        }
        return populationVariance;
    }

    public double median() {
        if (median == null) {
            median = kth_q_quantile(1, 2, samples);
        }
        return median;
    }

    public double Q1() {
        if (Q1 == null) {
            Q1 = kth_q_quantile(1, 4, samples);
        }
        return Q1;
    }

    public double Q3() {
        if (Q3 == null) {
            Q3 = kth_q_quantile(3, 4, samples);
        }
        return Q3;
    }

    public double excessKurtosis() {
        if (excessKurtosis == null) {
            return kurtosis() - 3;
        }
        return excessKurtosis;
    }

    public double kurtosis() {
        if (kurtosis == null) {
            kurtosis = (nthMomentAboutMean(4) / StrictMath.pow(populationVariance(), 2));
        }
        return kurtosis;
    }

    public double skewness() {
        if (skewness == null) {
            skewness = nthMomentAboutMean(3) / StrictMath.pow(populationVariance(), 1.5);
        }
        return skewness;
    }

    public double min() {
        if (min == null) {
            if (samples.isEmpty()) {
                throw new IllegalStateException();
            }
            min = samples.get(0);
        }
        return min;
    }

    public double max() {
        if (max == null) {
            if (samples.isEmpty()) {
                throw new IllegalStateException();
            }
            max = samples.get(samples.size() - 1);
        }
        return max;
    }

    public double range() {
        return max() - min();
    }

    public double IQR() {
        return Q3() - Q1();
    }

    public double standardDeviation() {
        return StrictMath.sqrt(variance());
    }

    public double populationStandardDeviation() {
        return StrictMath.sqrt(populationVariance());
    }

    public double percentile(int percentile) {
        return kth_q_quantile(percentile, 100, samples);
    }

    public double nthMomentAboutMean(int n) {
        return samples.stream()
                      .mapToDouble(x -> StrictMath.pow(x - mean(), n))
                      .average()
                      .orElse(0);
    }

    private void reset() {
        mean = null;
        geometricMean = null;
        variance = null;
        populationVariance = null;
        median = null;
        Q1 = null;
        Q3 = null;
        kurtosis = null;
        excessKurtosis = null;
        skewness = null;
        min = null;
        max = null;
    }

    private double kth_q_quantile(int k, int q, List<Double> list) {
        if (k < 0 || q < 0) {
            throw new IllegalArgumentException();
        }
        double i = (k * (list.size() - 1)) / (double) q;
        if ((i == StrictMath.floor(i)) && !Double.isInfinite(i)) {
            return list.get((int) i);
        } else {
            double lower = list.get((int) StrictMath.floor(i));
            double upper = list.get((int) StrictMath.ceil(i));
            double frac = i - StrictMath.floor(i);
            return ((1 - frac) * lower) + (frac * upper);
        }
    }
}
