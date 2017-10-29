package lock14.random.distribution;

public class Uniform extends AbstractDistribution<Double> {
    private static String errFormat = "Invalid interval, (a=%.2f, b=%.2f). Must have a < b";
    private double a;
    private double b;
    
    public Uniform() {
        this(0.0, 1.0);
    }
    
    public Uniform(double a, double b) {
        if (b <= a) {
            throw new IllegalArgumentException(String.format(errFormat, a, b));
        }
        this.b = b;
        this.a = a;
    }
    
    @Override
    public Double mean() {
        return (a + b) / 2;
    }
    
    @Override
    public Double variance() {
        return ((b - a) * (b - a)) / 12;
    }
    
    @Override
    public Double pdf(Double x) {
        return (x < a || x > b)? 0.0 : 1 / (b - a);
    }
    
    @Override
    public Double cdf(Double x) {
        return (x < a)? 0.0 : (x > b)? 1.0 : (x - a) / (b - a);
    }
    
    @Override
    public Double inverseCdf(Double p) {
        if (p == null || p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Invalid probability: p = " + p);
        }
        return p * (b - a) + a;
    }
    
    @Override
    public Double sample() {
        return (b - a) * rng.nextDouble() + a;
    }
    
    public static void main(String[] args) {
        Distribution<Double> distribution = new Uniform(-1, -5);
        System.out.println(distribution.sample(10));
    }
}
