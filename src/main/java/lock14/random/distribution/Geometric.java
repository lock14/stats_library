package lock14.random.distribution;

import lock14.random.stats.DescriptiveStatistics;
import lock14.random.stats.Histogram;

import java.util.List;
import java.util.Random;

public class Geometric extends AbstractDistribution<Integer> {
    private final double p;

    public Geometric(double p) {
        this(p, new Random());
    }

    public Geometric(double p, Random random) {
        super(random);
        this.p = p;
    }
    
    @Override
    public Double mean() {
        return 1 / p;
    }
    
    @Override
    public Double variance() {
        return (1 - p) / (p * p);
    }
    
    @Override
    public Double pdf(Integer x) {
        if (x < 1) {
            throw new IllegalArgumentException("argument cannot be less than 1: " + x);
        }
        return Math.pow(1 - p, x - 1) * p;
    }

    @Override
    public Double cdf(Integer x) {
        if (x < 1) {
            throw new IllegalArgumentException("argument cannot be less than 1: " + x);
        }
        return 1 - Math.pow(1 - p, x);
    }
    
    @Override
    public Integer inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return (int) Math.ceil(Math.log(1 - p) / Math.log(1 - this.p));
    }
    
    public static void main(String[] args) {
        Distribution<Integer> distribution = new Geometric(0.005);
        List<Integer> samples = distribution.sample(1000000);
        DescriptiveStatistics stats = new DescriptiveStatistics(samples);
        System.out.println("True mean: " + distribution.mean());
        System.out.println("True variance: " + distribution.variance());
        System.out.println("Sample mean: " + stats.mean());
        System.out.println("Sample variance: " + stats.variance());
        for (int i = 0; i < 30; i++) {
            System.out.print("_");
        }
        System.out.println();
        Histogram<Integer> histogram = new Histogram<>(stats, 20);
        histogram.print();
    }
}
