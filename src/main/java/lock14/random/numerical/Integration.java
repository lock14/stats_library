package lock14.random.numerical;

import java.util.function.Function;

public class Integration {

    public static Double compositeTrapezoid(Function<Double, Double> f,
                                            double a, double b, int n) {
        double h = (b - a) / n;
        double x0 = f.apply(a) + f.apply(b);
        double x1 = 0.0;
        for (int i = 1; i < n; i++) {
            Double x = a + (i * h);
            x1 += f.apply(x);
        }
        return h * ((x0 + (2 * x1)) / 2.0);
    }

    public static Double compositeMidpoint(Function<Double, Double> f, double a,
                                           double b, int n) {
        double h = (b - a) / (n + 2);
        double x0 = 0.0;
        for (int i = 0; i <= n; i += 2) {
            Double x = a + ((i + 1) * h);
            x0 += f.apply(x);
        }
        return 2 * h * x0;
    }

    public static Double compositeSimpsons(Function<Double, Double> f, double a,
                                          double b, int n) {
        double h = (b - a) / n;

        double x0 = f.apply(a) + f.apply(b);
        double x1 = 0.0;
        double x2 = 0.0;
        for (int i = 1; i < n; i++) {
            Double x = a + (i * h);
            if (i % 2 == 0) { // i is even
                x2 += f.apply(x);
            } else { // i is odd
                x1 += f.apply(x);
            }
        }
        return h * ((x0 + (2 * x2) + (4 * x1)) / 3.0);
    }

    public static Double gaussianQuadrature(Function<Double, Double> f,
                                            double a, double b, int n) {
        Legendre l = new Legendre(n);
        double[][] roots = l.getRoots();
        double[][] coeff = l.getCoeff();
        double approx = 0.0;
        for (int i = 0; i < n; i++) {
            Double x = (a + b + roots[n - 1][i] * (b - a)) / 2.0;
            approx = approx + coeff[n - 1][i] * f.apply(x);
        }
        approx = approx * (b - a) / 2.0;
        return approx;
    }
}
