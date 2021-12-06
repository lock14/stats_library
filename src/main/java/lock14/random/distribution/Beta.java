package lock14.random.distribution;

import lock14.random.numerical.Functions;

import java.util.Random;

public class Beta extends AbstractDistribution<Double> implements Distribution<Double> {
    private final double alpha;
    private final double beta;
    private final double logBetaAlphaBeta;

    public Beta(double alpha, double beta) {
        this(alpha, beta, new Random());
    }

    public Beta(double alpha, double beta, Random random) {
        super(random);
        this.alpha = alpha;
        this.beta = beta;
        this.logBetaAlphaBeta = Functions.logBeta(alpha, beta);
    }

    @Override
    public Double mean() {
        return alpha / (alpha + beta);
    }

    @Override
    public Double variance() {
        double alphaPlsBeta = alpha + beta;
        return (alpha * beta) / (alphaPlsBeta * alphaPlsBeta * (alphaPlsBeta + 1));
    }

    @Override
    public Double pdf(Double x) {
        double term1 = (alpha - 1) * Math.log(x);
        double term2 = (beta - 1) * Math.log(1 - x);
        return Math.exp(term1 + term2 - logBetaAlphaBeta);
    }

    @Override
    public Double cdf(Double x) {
        return Functions.incompleteBeta3(x, alpha, beta);
    }

    @Override
    public Double inverseCdf(Double p) {
        return Functions.inverseBeta(p, alpha, beta);
    }
    
    public static void main(String[] args) {
        double a = 2;
        double b = 2;
        double x = 0.5;
        Distribution<Double> beta = new Beta(a, b);
    }

}
