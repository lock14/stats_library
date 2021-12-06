package lock14.random.numerical;

/**
 * Incomplete Beta function
 *
 * @author Didier H. Besset
 */
public class IncompleteBetaFunction {
    /**
     * Function parameters.
     */
    private final double alpha1;
    private final double alpha2;
    /**
     * Constant to be computed once only.
     */
    private final double logNorm;
    /**
     * Continued fractions.
     */
    private IncompleteBetaFunctionFraction fraction;
    private IncompleteBetaFunctionFraction inverseFraction;

    /**
     * Constructor method.
     *
     * @param a1 double
     * @param a2 double
     */
    public IncompleteBetaFunction(double a1, double a2) {
        alpha1 = a1;
        alpha2 = a2;
        logNorm = Functions.logGamma(alpha1 + alpha2)
                  - Functions.logGamma(alpha1)
                  - Functions.logGamma(alpha2);
    }

    /**
     * @param x double
     * @return double
     */
    private double evaluateFraction(double x, double alpha1, double alpha2) {
        if (fraction == null) {
            fraction = new IncompleteBetaFunctionFraction(alpha1, alpha2);
            fraction.setDesiredPrecision(DhbMath.getMachinePrecision());
        }
        fraction.setArgument(x);
        fraction.evaluate();
        return fraction.getResult();
    }

    public double value(double x) {
        if (x == 0) {
            return 0;
        }
        if (x == 1) {
            return 1;
        }
        double norm = Math.exp(alpha1 * Math.log(x)
                               + alpha2 * Math.log(1 - x) + logNorm);
        return (alpha1 + alpha2 + 2) * x < (alpha1 + 1)
               ? norm / (evaluateFraction(x, alpha1, alpha2) * alpha1)
               : 1 - norm / (evaluateFraction(1 - x, alpha2, alpha1) * alpha2);
    }
}
