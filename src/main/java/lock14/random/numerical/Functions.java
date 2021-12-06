package lock14.random.numerical;

import org.apache.commons.math3.special.Beta;

/**
 * This functions in this class are adapted from the numerical methods described
 * in Object-Oriented Implementation of Numerical Methods by Dider H. Besset.
 * <p>
 * Source: https://github.com/SquareBracketAssociates/ArchiveOONumericalMethods/
 */
public class Functions {
    private static final double SQRT_2PI = Math.sqrt(2 * Math.PI);
    private static final double[] coefficients = {76.18009172947146,
                                                  -86.50532032941677, 24.01409824083091, -1.231739572450155,
                                                  0.1208650973866179e-2, -0.5395239384953e-5};

    public static double beta(double a, double b) {
        return Math.exp(logBeta(a, b));
    }

    public static double logBeta(double a, double b) {
        return logGamma(a) + logGamma(b) - logGamma(a + b);
    }

    public static double incompleteBeta(double x, double a, double b) {
        return Beta.regularizedBeta(x, a, b);
    }

    public static double incompleteBeta2(double x, double a, double b) {
        if (x == 0 || x == 1) {
            return x;
        }
        double norm = Math
            .exp(a * Math.log(x) + b * Math.log(1 - x) - logBeta(a, b));
        return ((a + b + 2) * x) < (a + 1)
               ? norm / (evaluateFraction(x, a, b) * a)
               : 1 - norm / (evaluateFraction(1 - x, b, a) * b);
    }

    public static double incompleteBeta3(double x, double a, double b) {
        return new IncompleteBetaFunction(a, b).value(x);
    }

    public static double inverseBeta(double p, double alpha, double beta) {
        return inverseBeta(p, alpha, beta, -1, -1);
    }

    /// <summary>
    /// Returns the inverse of the cumulative beta probability density function.
    /// </summary>
    /// <param name="p">Probability associated with the beta
    /// distribution.</param>
    /// <param name="alpha">Parameter of the distribution.</param>
    /// <param name="beta">Parameter of the distribution.</param>
    /// <param name="A">Optional lower bound to the interval of x.</param>
    /// <param name="B">Optional upper bound to the interval of x.</param>
    /// <returns>Inverse of the cumulative beta probability density function for
    /// a given probability</returns>

    public static double inverseBeta(double p, double alpha, double beta, double A,
                                     double B) {
        double x = 0;
        double a = 0;
        double b = 1;
        double precision = Math.pow(10, -15); // converge until there is 15
        // decimal places precision

        while ((b - a) > precision) {
            x = (a + b) / 2;
            if (incompleteBeta3(x, alpha, beta) > p) {
                b = x;
            } else {
                a = x;
            }
        }

        if ((B > 0) && (A > 0)) {
            x = x * (B - A) + A;
        }
        return x;
    }

    public static double gamma(double x) {
        return Math.exp(logGamma(x));
    }

    public static double logGamma(double x) {
        return (x > 1) ? leadingFactor(x) + Math.log(series(x) * SQRT_2PI / x)
                       : (x > 0) ? logGamma(x + 1) - Math.log(x) : Double.NaN;
    }

    private static double evaluateFraction(double x, double a, double b) {
        int maxIterations = 50;
        double desiredPrecision = DhbMath.getMachinePrecision();
        double[] factors = new double[2];
        factors[1] = 1;
        int i = 0;
        double numerator = DhbMath.smallNumber();
        double denominator = 0;
        double result = numerator;
        while (i++ < maxIterations) {
            int m = i / 2;
            int m2 = 2 * m;
            factors[0] = m2 == i ? x * m * (b - m) / ((a + m2) * (a + m2 - 1))
                                 : -x * (a + m) * (a + b + m) / ((a + m2) * (a + m2 + 1));
            double r1 = factors[0] * denominator + factors[1];
            double r2 = factors[0] / numerator + factors[1];
            denominator = 1 / (Math.abs(r1) < DhbMath.smallNumber()
                               ? DhbMath.smallNumber()
                               : r1);
            numerator = (Math.abs(r2) < DhbMath.smallNumber()
                         ? DhbMath.smallNumber()
                         : r2);
            double delta = numerator * denominator;
            result *= delta;
            double precision = Math.abs(delta - 1);
            if (precision < desiredPrecision) {
                break;
            }
        }
        return result;
    }

    private static double leadingFactor(double x) {
        double temp = x + 5.5;
        return Math.log(temp) * (x + 0.5) - temp;
    }

    private static double series(double x) {
        double answer = 1.000000000190015;
        double term = x;
        for (int i = 0; i < 6; i++) {
            term += 1;
            answer += coefficients[i] / term;
        }
        return answer;
    }

    public static double nChooseK(int n, int k) {
        return Math.exp(logGamma(n + 1) - logGamma(k + 1) - logGamma(n - k + 1));
    }

    public static void main(String[] args) {
        System.out.println(incompleteBeta(0.5, 0.5, 0.5));
        System.out.println(incompleteBeta2(0.5, 0.5, 0.5));
        System.out.println(incompleteBeta3(0.5, 0.5, 0.5));
        System.out.println(inverseBeta(incompleteBeta(0.5, 0.5, 0.5), 0.5, 0.5));
        System.out.println();

        System.out.println(nChooseK(3, 2));
    }
}
