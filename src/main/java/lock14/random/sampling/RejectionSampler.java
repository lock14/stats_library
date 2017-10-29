package lock14.random.sampling;

import java.util.function.Function;

import lock14.random.distribution.Distribution;
import lock14.random.distribution.Uniform;

public class RejectionSampler<N extends Number> implements DistributionSampler<N> {
    private Uniform                uniform;
    private Function<N, Double>    p;
    private Function<N, Double>    q;
    private DistributionSampler<N> qSampler;
    private double                 M;

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
