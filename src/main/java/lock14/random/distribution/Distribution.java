package lock14.random.distribution;

import lock14.random.sampling.DistributionSampler;

public interface Distribution<N extends Number> extends DistributionSampler<N> {
    Double mean();
    Double variance();
    Double pdf(N x);
    Double cdf(N x);
    N inverseCdf(Double p);
}
