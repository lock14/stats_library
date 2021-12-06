package lock14.random.sampling;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DistributionSampler<N extends Number> {

    void setSeed(long seed);

    N sample();
    
    default List<N> sample(int n) {
        return Stream.generate(this::sample)
                     .limit(n)
                     .collect(Collectors.toList());
    }
}
