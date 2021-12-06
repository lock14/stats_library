package lock14.random.distribution;

import lock14.random.stats.DescriptiveStatistics;
import lock14.random.stats.Histogram;

import java.util.List;
import java.util.Random;

public class Cauchy extends AbstractDistribution<Double> {
    private final double x0;
    private final double gamma;
    private final double gammaSquared;
    private final double pdfConst;

    Cauchy(double x0, double gamma) {
        this(x0, gamma, new Random());
    }

    Cauchy(double x0, double gamma, Random random) {
        super(random);
        this.x0 = x0;
        this.gamma = gamma;
        this.gammaSquared = gamma * gamma;
        this.pdfConst = (gammaSquared) / (Math.PI * gamma);
    }
    
    @Override
    public Double mean() {
        throw new UnsupportedOperationException("Cauchy Distribution mean is undefined");
    }

    @Override
    public Double variance() {
        throw new UnsupportedOperationException("Cauchy Distribution variance is undefined");
    }

    @Override
    public Double pdf(Double x) {
        double xdiff = x - x0;
        return pdfConst / ((xdiff * xdiff) + gammaSquared);
    }

    @Override
    public Double cdf(Double x) {
        return (Math.atan((x - x0) / gamma) / Math.PI) + 0.5;
    }

    @Override
    public Double inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return (Math.tan(Math.PI * (p + 0.5)) * gamma) + x0;
    }
    
    public static void main(String[] args) {
        Distribution<Double> distribution = new Cauchy(0, 1);
        List<Double> samples = distribution.sample(1000000);
        DescriptiveStatistics stats = new DescriptiveStatistics(samples);
        for (int i = 0; i < 30; i++) {
            System.out.print("_");
        }
        System.out.println();
        Histogram histogram = new Histogram(stats, 20);
        histogram.print();
    }
}
