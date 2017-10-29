package lock14.random;

import java.util.Random;

public class MyRandom extends Random {
    public static final long LONG_MASK = 0x00000000ffffffffL;
    public static final int FLOAT_BITS = 30;
    public static final float FLOAT_VAL = 1 / (float)(1 << FLOAT_BITS);
    
    public static final int DOUBLE_BITS_UPPER = 31;
    public static final int DOUBLE_BITS_LOWER = 31;
    public static final double DOUBLE_VAL = 1 / (double)(1L << (DOUBLE_BITS_UPPER + DOUBLE_BITS_LOWER));
    
    private static final long serialVersionUID = 1L;
    
    private boolean hasNextGaussian = false;
    private double nextGaussian;
    
    public MyRandom() {
        super();
        //setSeed();
    }
    
    public int next(int n) {
        // TODO: replace this with own implementation
        // something akin to: seed = (alpha * seed) + beta % m
        // need to figure out appropriate values for alpha, beta, and m
        // return seed
        return super.next(n);
    }
    
    public boolean nextBoolean() {
        return next(1) == 0;
    }
    
    public int nextInt() {
        return next(32);
    }
    
    public long nextLong() {
        // java uses signed ints. thus if next(32) < 0
        // then the cast (long)next(32) < 0, which changes the bits
        // (long)next(32) & 0x00000000ffffffffL maps negative ints to there positive counterparts
        // e.g (long)(1 << 31) & 0x00000000ffffffffL maps to 2^32 instead of -2^32
        return ((long)next(32) << 32) | ((long)next(32) & LONG_MASK);
    }
    
    public float nextFloat() {
        return next(FLOAT_BITS) * FLOAT_VAL;
    }
    
    public double nextDouble() {
        return (((long)next(DOUBLE_BITS_UPPER) << DOUBLE_BITS_LOWER) + next(DOUBLE_BITS_LOWER)) * DOUBLE_VAL;
    }
    
    // alternative name for nextDouble to make it more clear that it
    // samples from the uniform distribution on the interval (0.0,1.0)
    public double nextUniform() {
        return nextDouble();
    }
    
    public double nextUniform(double lowerBound, double upperBound) {
        return (nextDouble() * (upperBound - lowerBound)) + lowerBound;
    }
    
    public double nextGaussian() {
        if (hasNextGaussian) {
            hasNextGaussian = false;
            return nextGaussian;
        } else {
            double r, z1, z2, y1, y2;
            do {
                z1 = nextUniform(-1, 1);
                z2 = nextUniform(-1, 1);
                r = (z1 * z1) + (z2 * z2); // would call this r^2 but can't do that
            } while (r > 1 || r == 0); // r can't be 0 because of division below
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(r) / (r));
            y1 = z1 * multiplier;
            y2 = z2 * multiplier;
            nextGaussian = y2;
            hasNextGaussian = true;
            return y1;
        }
    }
    
    public double nextGaussian(double mu, double sigma) {
        return (sigma * nextGaussian()) + mu;
    }
}
