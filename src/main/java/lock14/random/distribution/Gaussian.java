package lock14.random.distribution;

import lock14.random.numerical.Integration;

public class Gaussian extends AbstractDistribution<Double> {
    private static double HALF_LN_TWO_PI = 0.5 * Math.log(2 * Math.PI);
    private double mu;
    private double sigma;
    
    public Gaussian() {
        this(0.0, 1.0);
    }
    
    public Gaussian(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }
    
    @Override
    public Double mean() {
        return mu;
    }
    
    @Override
    public Double variance() {
        return sigma * sigma;
    }
    
    @Override
    public Double pdf(Double x) {
        double z = (x - mu) / sigma;
        double logProb = (-(z*z) / 2) - HALF_LN_TWO_PI;
        return Math.exp(logProb);
    }
    
    @Override
    public Double cdf(Double x) {
        // lower bound is supposed to be -infinity.
        // but 10 standard deviations below the mean should do the trick
        double lowerBound = (-10.0 * sigma) + mu;
        return Integration.gaussianQuadrature(this::pdf, lowerBound, x, 20) / sigma;
    }
    
    @Override
    public Double inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return null;
    }
            
    @Override
    public Double sample() {
        return sigma * rng.nextGaussian() + mu;
    }
    
    public static void main(String[] args) {
        Distribution<Double> distribution = new Gaussian();
        System.out.println(distribution.pdf(0.2));
        System.out.println(distribution.cdf(0.2));
        Distribution<Double> distribution2 = new Gaussian(5, 10);
        System.out.println(distribution2.pdf(7.0));
        System.out.println(distribution2.cdf(7.0));
    }
}
