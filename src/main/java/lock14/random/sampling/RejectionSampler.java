package lock14.random.sampling;

import lock14.random.distribution.Distribution;
import lock14.random.distribution.Uniform;

import java.util.function.Function;

public class RejectionSampler<N extends Number> implements DistributionSampler<N> {
    private final Uniform uniform;
    private final Function<N, Double> p;
    private final Function<N, Double> q;
    private final DistributionSampler<N> qSampler;
    private final double M;

    public RejectionSampler(Distribution<N> desired, Distribution<N> proposal,
                            double proposalMultiplier) {
        this(desired::pdf, proposal::pdf, proposal, proposalMultiplier);
    }

    public RejectionSampler(Function<N, Double> desiredDensity,
                            Distribution<N> proposal,
                            double proposalMultiplier) {
        this(desiredDensity, proposal::pdf, proposal, proposalMultiplier);
    }

    public RejectionSampler(Function<N, Double> desiredDensity,
                            Function<N, Double> proposalDensity,
                            DistributionSampler<N> proposalSampler,
                            double proposalMultiplier) {
        uniform = new Uniform();
        p = desiredDensity;
        q = proposalDensity;
        qSampler = proposalSampler;
        M = proposalMultiplier;
    }

    @Override
    public void setSeed(long seed) {
        qSampler.setSeed(seed);
    }

    @Override
    public N sample() {
        N sample;
        double accept;
        do {
            sample = qSampler.sample();
            accept = p.apply(sample) / (M * q.apply(sample));
        } while (uniform.sample() > accept);
        return sample;
    }
}
