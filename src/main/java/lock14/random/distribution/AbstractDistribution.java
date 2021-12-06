package lock14.random.distribution;

import java.util.Random;

public abstract class AbstractDistribution<N extends Number> implements Distribution<N> {
    protected final Random randomGenerator;
    
    public AbstractDistribution(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public void setSeed(long seed) {
        randomGenerator.setSeed(seed);
    }

    @Override
    public N sample() {
        return inverseCdf(randomGenerator.nextDouble());
    }
}
