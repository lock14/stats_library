package lock14.random.distribution;

import lock14.random.stats.DescriptiveStatistics;
import lock14.random.stats.Histogram;

import java.util.List;
import java.util.Random;

public class Exponential extends AbstractDistribution<Double> {
    private final double lambda;

    public Exponential(double lambda) {
        this(lambda, new Random());
    }

    public Exponential(double lambda, Random random) {
        super(random);
        this.lambda = lambda;
    }
    
    @Override
    public Double mean() {
        return 1 / lambda;
    }

    @Override
    public Double variance() {
        return 1 / (lambda * lambda);
    }

    @Override
    public Double pdf(Double x) {
        return (x < 0) ? 0.0 : lambda * Math.exp(-(x * lambda));
    }

    @Override
    public Double cdf(Double x) {
        return (x < 0) ? 0.0 : 1 - Math.exp(-(x * lambda));
    }

    @Override
    public Double inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return (-Math.log(1 - p) / lambda);
    }
    
    public static void main(String[] args) {
        Distribution<Double> distribution = new Exponential(0.5);
        List<Double> samples = distribution.sample(1000000);
        DescriptiveStatistics stats = new DescriptiveStatistics(samples);
        System.out.println("True mean: " + distribution.mean());
        System.out.println("True variance: " + distribution.variance());
        System.out.println("Sample mean: " + stats.mean());
        System.out.println("Sample variance: " + stats.variance());
        for (int i = 0; i < 30; i++) {
            System.out.print("_");
        }
        System.out.println();
        Histogram histogram = new Histogram(stats, 20);
        histogram.print();
    }
}
