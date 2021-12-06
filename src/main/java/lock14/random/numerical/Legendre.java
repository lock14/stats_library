package lock14.random.numerical;

/**
 * This Legendre class was adapted from
 * https://userpages.umbc.edu/~squire/download/Legendre.java
 */
public class Legendre {
    double[][] R; // roots of Pn(x) the x[i]
    double[][] W; // weights of Pn(x) the w[i]

    public Legendre(int n) {
        double[][] P = generatePolys(n);
        double[][] DP = generateDerivs(P, n);
        R = findRoots(P, n);
        W = findCoeff(P, DP, R, n);
    }

    double[][] getRoots() {
        return R;
    }

    double[][] getCoeff() {
        return W;
    }

    // generate family of Legendre polynomials Pn(x)
    // as P[degree][coefficients]
    // build coefficients of Pn(x)
    static double[][] generatePolys(int n) {
        double[][] p = new double[n + 1][n + 1];
        p[0][0] = 1.0;
        p[1][1] = 1.0; // start for recursion
        for (int i = 2; i < n + 1; i++) {
            for (int j = 0; j <= i; j++) {
                if (j < i - 1) {
                    p[i][j] = -((i - 1) / (double) i) * p[i - 2][j];
                }
                if (j > 0) {
                    p[i][j] = p[i][j]
                              + ((2 * i - 1) / (double) i) * p[i - 1][j - 1];
                }
            }
        }
        return p;
    }

    // compute derivitives of Pn(x)
    static double[][] generateDerivs(double[][] polys, int n) {
        double[][] dp = new double[n][n];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                dp[i][j] = (double) (j + 1) * polys[i][j + 1];
            }
        }
        return dp;
    }

    // find roots of Pn(x), the x[i]
    static double[][] findRoots(double[][] polys, int n) {
        double[][] roots = new double[n][n];
        roots[1][0] = 1.0;
        for (int i = 2; i < n; i++) {
            legendreRoot(i, polys, roots);
        }
        return roots;
    }

    // compute coefficients of Pn(x)
    static double[][] findCoeff(double[][] polys, double[][] derivs,
                                double[][] roots, int n) {
        double[][] coeff = new double[n][n];
        coeff[1][0] = 1.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                coeff[i][j] = -2.0
                              / ((i + 1) * Evaluate(i, derivs[i], roots[i][j])
                                 * Evaluate(i + 1, polys[i + 1], roots[i][j]));
            }
        }
        return coeff;
    }

    // specialized for roots of Legendre polynomials
    // check first for zero root, reduce into T.
    // T=initial polynomial, store root at R[n][i]
    static void legendreRoot(int n, double[][] P, double[][] R) {
        double[] T = new double[n + 1];
        double[] DT = new double[n];
        int i = 0;

        if (P[n][0] == 0.0) {
            R[n][i] = 0.0;
            i++;
            for (int j = 1; j <= n; j++) {
                T[j - 1] = P[n][j]; // reduce
            }
        } else {
            for (int j = 0; j <= n; j++) {
                T[j] = P[n][j];
            }
        }
        while (i < n) {
            for (int j = 0; j < n - i; j++) {
                DT[j] = (double) (j + 1) * T[j + 1];
            }
            // do root Newton
            double rold = 0.0, r = 1.0;

            for (int k = 0; k < 20; k++) {
                r = r - Evaluate(n - i, T, r) / Evaluate(n - i - 1, DT, r);
                if (Math.abs(r - rold) < 1.0E-14) {
                    break;
                }
                rold = r;
            }
            for (int j = n - i; j > 0; j--) {
                T[j - 1] = T[j - 1] + r * T[j];
            }
            for (int j = 1; j <= n - i; j++) {
                T[j - 1] = T[j]; // reduce
            }
            R[n][i] = r;
            i++;
            r = -r;
            for (int j = n - i; j > 0; j--) {
                T[j - 1] = T[j - 1] + r * T[j];
            }
            for (int j = 1; j <= n - i; j++) {
                T[j - 1] = T[j]; // reduce
            }
            R[n][i] = r;
            i++;
        }
    }

    private static double Evaluate(int n, double[] P, double x) {
        double value = P[n];
        for (int i = n - 1; i >= 0; i--) {
            value = value * x + P[i];
        }
        return value;
    }
}
