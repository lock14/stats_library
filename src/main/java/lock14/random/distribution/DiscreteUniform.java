package lock14.random.distribution;

import java.util.List;
import java.util.stream.Collectors;

import lock14.random.stats.DescriptiveStatistics;
import lock14.random.stats.Histogram;

public class DiscreteUniform extends AbstractDistribution<Integer> {
    private static String errFormat = "Invalid interval, (a=%.2f, b=%.2f). Must have a < b";
    private int b;
    private int a;
    
    public DiscreteUniform(int a, int b) {
        if (b <= a) {
            throw new IllegalArgumentException(String.format(errFormat, a, b));
        }
        this.b = b;
        this.a = a;
    }
    
    @Override
    public Double mean() {
        return 0.5 * (a + b);
    }
    
    @Override
    public Double variance() {
        double n = b - a + 1;
        return (n * n - 1) / 12.0;
    }
    
    @Override
    public Double pdf(Integer x) {
        return (x < a) || (x > b) ? 0.0 : 1.0 / (b - a + 1.0);
    }

    @Override
    public Double cdf(Integer x) {
        return (x < a) ? 0.0 : (x > b) ? 1.0 : (x - a + 1.0) / (b - a + 1.0);
    }
    
    @Override
    public Integer inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return (int)Math.ceil((p * (b - a + 1.0)) + (a - 1));
    }

    @Override
    public Integer sample() {
        return rng.nextInt(b - a + 1) + a;
    }
    
    public static void main(String[] args) {
        Distribution<Integer> distribution = new DiscreteUniform(1, 20);
        List<Double> samples = distribution.sample(1000000)
                                           .stream()
                                           .map(Double::valueOf)
                                           .collect(Collectors.toList());
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
