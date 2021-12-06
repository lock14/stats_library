package lock14.random.distribution;

import lock14.random.numerical.Functions;

import java.util.Random;
import java.util.function.Function;

public class StudentT extends AbstractDistribution<Double>{
    private final double inverseCdfConst;
    private final double pdfConst;
    private final double df; // degrees of freedom

    public StudentT(double degreesOfFreedom) {
        this(degreesOfFreedom, new Random());
    }

    public StudentT(double degreesOfFreedom, Random random) {
        super(random);
        if (degreesOfFreedom <= 0.0) {
            throw new IllegalArgumentException("Degrees of freedom must be greater than zero");
        }
        df = degreesOfFreedom;
        pdfConst = Math.sqrt(df) * Functions.beta(0.5, df / 2);
        inverseCdfConst = (Functions.gamma((df * 0.5) + 0.5) / Functions.gamma(0.5 * df)) / Math.sqrt(df * Math.PI);
    }

    @Override
    public Double mean() {
        return 0.0;
    }

    @Override
    public Double variance() {
        if (df <= 1) {
            throw new IllegalStateException("Variance undefined for degreees of freedom <= 1");
        } else if (df <= 2) {
            return Double.POSITIVE_INFINITY;
        } else {
            return df / (df - 2);
        }
    }

    @Override
    public Double pdf(Double t) {
        return Math.pow(1 + ((t * t) / df), -(df + 1) * 0.5) / pdfConst;
    }

    @Override
    public Double cdf(Double t) {
        return 1 - 0.5 * Functions.incompleteBeta(df / (t * t + df), 0.5 * df, 0.5);
    }

    @Override
    public Double inverseCdf(Double p) {
        return 100 * Functions.incompleteBeta(df / (p * p + df), 0.5 * df, 0.5);
    }
    
    private final Function<Double, Double> inverseCdfFun = new Function<Double, Double>() {
        @Override
        public Double apply(Double t) {
            return Math.pow(1 + ((t * t) / df), -(df + 1) * 0.5);
        }};
    
    public static void main(String[] args) {
        Distribution<Double> t = new StudentT(3);
    }
}
