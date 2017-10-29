package lock14.random.distribution;

import java.util.Random;

public abstract class AbstractDistribution<N extends Number> implements Distribution<N> {
    protected Random rng;
    
    public AbstractDistribution() {
        rng = new Random();
    }
    
    public void setSeed(long seed) {
        rng.setSeed(seed);
    }
    
    public void setRandomNumberGenerator(Random rng) {
        this.rng = rng;
    }

}
