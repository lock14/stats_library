package lock14.random.distribution;

import lock14.random.stats.DescriptiveStatistics;
import lock14.random.stats.Histogram;

import java.util.List;
import java.util.Random;

public class Uniform extends AbstractDistribution<Double> {
    private static final String errFormat = "Invalid interval, (a=%.2f, b=%.2f). Must have a < b";
    private final double a;
    private final double b;
    
    public Uniform() {
        this(0.0, 1.0);
    }

    public Uniform(double a, double b) {
        this(a, b, new Random());
    }

    public Uniform(double a, double b, Random random) {
        super(random);
        if (b <= a) {
            throw new IllegalArgumentException(String.format(errFormat, a, b));
        }
        this.b = b;
        this.a = a;
    }
    
    @Override
    public Double mean() {
        return (a + b) / 2;
    }
    
    @Override
    public Double variance() {
        return ((b - a) * (b - a)) / 12;
    }
    
    @Override
    public Double pdf(Double x) {
        return (x < a || x > b)? 0.0 : 1 / (b - a);
    }
    
    @Override
    public Double cdf(Double x) {
        return (x < a)? 0.0 : (x > b)? 1.0 : (x - a) / (b - a);
    }
    
    @Override
    public Double inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return (b - a) * p  + a;
    }
    
    public static void main(String[] args) {
        Distribution<Double> distribution = new Uniform();
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
        Histogram<Double> histogram = new Histogram<>(stats, 20);
        histogram.print();
    }
}
