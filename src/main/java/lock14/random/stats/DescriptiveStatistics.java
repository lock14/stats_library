package lock14.random.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

public class DescriptiveStatistics {
    private List<Double> samples;
    private OptionalDouble mean;
    private OptionalDouble geometricMean;
    private OptionalDouble variance;
    private OptionalDouble populationVariance;
    private OptionalDouble median;
    private OptionalDouble Q1;
    private OptionalDouble Q3;
    private OptionalDouble kurtosis;
    private OptionalDouble skewness;
    private Double min;
    private Double max;
    
    public DescriptiveStatistics(Collection<Double> samples) {
        if (samples.size() == 0) {
            throw new IllegalArgumentException("Collection must be non-empty");
        }
        this.samples = new ArrayList<>(samples);
        Collections.sort(this.samples);
        mean = OptionalDouble.empty();
        geometricMean = OptionalDouble.empty();
        variance = OptionalDouble.empty();
        populationVariance = OptionalDouble.empty();
        median = OptionalDouble.empty();
        Q1 = OptionalDouble.empty();
        Q3 = OptionalDouble.empty();
        kurtosis = OptionalDouble.empty();
        skewness = OptionalDouble.empty();
        min = this.samples.get(0);
        max = this.samples.get(this.samples.size() - 1);
    }
    
    public List<Double> sortedSamples() {
        return Collections.unmodifiableList(samples);
    }
    
    public int numSamples() {
        return samples.size();
    }
    
    public double mean() {
        if (!mean.isPresent()) {
            double m = samples.stream()
                              .mapToDouble(Double::doubleValue)
                              .average().getAsDouble();
            mean = OptionalDouble.of(m);
        }
        return mean.getAsDouble();
    }
    
    public double geometricMean() {
        if (!geometricMean.isPresent()) {
            double prod = samples.stream()
                                .mapToDouble(Double::doubleValue)
                                .reduce(1.0, (x1, x2) -> x1 * x2);
            double pow = 1 / samples.size();
            geometricMean = OptionalDouble.of(StrictMath.pow(prod, pow));
        }
        return geometricMean.getAsDouble();
    }
    
    public double variance() {
        if (!variance.isPresent()) {
            double v = samples.stream()
                              .mapToDouble(x -> StrictMath.pow(x - mean(), 2))
                              .sum() / (samples.size() - 1);
            variance = OptionalDouble.of(v);
        }
        return variance.getAsDouble();
    }
    
    public double populationVariance() {
        if (!populationVariance.isPresent()) {
            populationVariance = OptionalDouble.of(nthMomentAboutMean(2));
        }
        return populationVariance.getAsDouble();
    }
    
    public double median() {
        if (!median.isPresent()) {
            median = OptionalDouble.of(kth_q_quantile(1, 2, samples));
        }
        return median.getAsDouble();
    }
    
    public double Q1() {
        if (!Q1.isPresent()) {
            Q1 = OptionalDouble.of(kth_q_quantile(1, 4, samples));
        }
        return Q1.getAsDouble();
    }
    
    public double Q3() {
        if (!Q3.isPresent()) {
            Q3 = OptionalDouble.of(kth_q_quantile(3, 4, samples));
        }
        return Q3.getAsDouble();
    }
    
    public double kurtosis() {
        if(!kurtosis.isPresent()) {
            double m4 = nthMomentAboutMean(4);
            double m2 = populationVariance();
            kurtosis = OptionalDouble.of((m4 / StrictMath.pow(m2, 2)) - 3);
        }
        return kurtosis.getAsDouble();
    }
    
    public double skewness() {
        if(!skewness.isPresent()) {
            double m3 = nthMomentAboutMean(3);
            skewness = OptionalDouble.of(m3 / StrictMath.pow(variance(), 1.5));
        }
        return skewness.getAsDouble();
    }
    
    public double min() {
        return min;
    }
    
    public double max() {
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
                      .average().getAsDouble();
    }
    
    private double kth_q_quantile(int k, int q, List<Double> list) {
        if (k < 0 || q < 0) {
            throw new IllegalArgumentException();
        }
        double i = (k * (list.size() - 1)) / (double)q;
        if ((i == StrictMath.floor(i)) && !Double.isInfinite(i)) {
            return list.get((int)i);
        } else {
            double lower = list.get((int)StrictMath.floor(i));
            double upper = list.get((int)StrictMath.ceil(i));
            double frac = i - StrictMath.floor(i);
            return ((1 - frac) * lower) + (frac * upper);
        }
    }
}
