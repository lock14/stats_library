package lock14.random.distribution;

import lock14.random.sampling.DistributionSampler;

public interface Distribution<N extends Number> extends DistributionSampler<N> {
    public Double mean();
    public Double variance();
    public Double pdf(N x);
    public Double cdf(N x);
    public N inverseCdf(Double p);
}
