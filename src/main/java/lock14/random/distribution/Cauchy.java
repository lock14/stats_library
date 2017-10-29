package lock14.random.distribution;

public class Cauchy extends AbstractDistribution<Double> {
    private double x0;
    private double gamma;
    private double gammaSquared;
    private double pdfConst;
    
    Cauchy(double x0, double gamma) {
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

    @Override
    public Double sample() {
        return inverseCdf(rng.nextDouble());
    }
}
