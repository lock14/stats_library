package lock14.random;

import lock14.random.stats.DescriptiveStatistics;
import lock14.random.stats.Histogram;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomTest {
    public static boolean SHOW_HISTOGRAMS = true;
    public static int NUM_SAMPLES = 100000;
    public static int PRINT_BINS = 20;

    public static void main(String[] args) {
        MyRandom r = new MyRandom();

        randomTest("Boolean Test: Bernoulli(0.5):",
                    r::nextBoolean,
                    b -> b ? 1.0 : 0.0, 2, 0.5, 0.5);
        System.out.println();

        randomTest("Integer Test: Uniform(-2^32, 2^32 - 1)",
                   r::nextInt,
                   "proportion less than zero: ",
                   x -> x < 0,
                   Double::valueOf,
                   -0.5, -0.08333333333333333);
        System.out.println();

        randomTest("Float Test : Uniform(0, 1)",
                    r::nextFloat,
                    "proportion with last bit checked: ",
                    f -> (Float.floatToIntBits(f) & 1) == 1,
                    Double::valueOf,
                    0.5, StrictMath.sqrt(1.0/12.0));
        System.out.println();

        randomTest("Double Test: Uniform(0, 1)",
                    r::nextDouble,
                    "proportion with last bit checked: ",
                    d -> (Double.doubleToLongBits(d) & 1) == 1,
                    Function.identity(),
                    0.5, StrictMath.sqrt(1.0/12.0));
        System.out.println();

        double a = 10;
        double b = 20;
        randomTest("Uniform Test: Uniform(" + a + ", " + b + ")",
                   () -> r.nextUniform(a, b),
                   Function.identity(),
                   (a + b) / 2, StrictMath.sqrt((1.0/12.0) * StrictMath.pow(b - a, 2)));
        System.out.println();

        randomTest("Guassian Test : Normal(0, 1)",
                    r::nextGaussian,
                    Function.identity(),
                    0.0, 1.0);
        System.out.println();

        double mu = 5;
        double sigma = 10;
        randomTest("Guassian Test : Normal(" + mu + ", " + sigma + ")",
                   () -> r.nextGaussian(mu, sigma),
                   Function.identity(),
                   mu, sigma);
    }

    public static <T> void randomTest(String testName, Supplier<T> supplier,
                                      Function<T, Double> mapper, Double... params) {
        randomTest(testName, supplier, mapper, null,  params);
    }

    public static <T> void randomTest(String testName, Supplier<T> supplier,
                                      Function<T, Double> mapper, Integer bins, Double... params) {
        randomTest(testName, supplier, null, null, mapper, bins,  params);
    }

    public static <T> void randomTest(String testName, Supplier<T> supplier,
                                      String predicateMessage, Predicate<T> proportionPredicate,
                                      Function<T, Double> mapper, Double... params) {
        randomTest(testName, supplier, predicateMessage, proportionPredicate, mapper, null,  params);
    }

    public static <T> void randomTest(String testName, Supplier<T> supplier,
                                      String predicateMessage, Predicate<T> proportionPredicate,
                                      Function<T, Double> mapper, Integer bins, Double... params) {
        Optional<String> predString = Optional.ofNullable(predicateMessage);
        Optional<Predicate<T>> propPred = Optional.ofNullable(proportionPredicate);
        Optional<Integer> bin = Optional.ofNullable(bins);
        OptionalDouble mu = OptionalDouble.empty();
        OptionalDouble sigma = OptionalDouble.empty();
        if (params.length == 2) {
            mu = OptionalDouble.of(params[0]);
            sigma = OptionalDouble.of(params[1]);
        }
        randomTest(testName, supplier, predString, propPred, mapper, bin,  mu, sigma);
    }

    public static <T> void randomTest(String testName, Supplier<T> supplier,
                                      Optional<String> predicateMessage,
                                      Optional<Predicate<T>> proportionPredicate,
                                      Function<T, Double> mapper, Optional<Integer> bins,
                                      OptionalDouble mu, OptionalDouble sigma) {
        System.out.println(testName);
        List<T> samples = Stream.generate(supplier)
                                .limit(NUM_SAMPLES)
                                .collect(Collectors.toList());

        if (proportionPredicate.isPresent()) {
            // check proportion that have last bit set
            double proportion = samples.stream()
                                       .filter(proportionPredicate.get())
                                       .count() / (double)NUM_SAMPLES;

            System.out.println(predicateMessage.get() + proportion);
        }

        DescriptiveStatistics stats = new DescriptiveStatistics(samples.stream()
                                                                       .map(mapper)
                                                                       .collect(Collectors.toList()));
        if (mu.isPresent() && sigma.isPresent()) {
            printStats(stats, mu.getAsDouble(), sigma.getAsDouble());
        }
        createHistogram(stats, bins);
    }

    public static void printStats(DescriptiveStatistics stats, double mu, double sigma) {
        System.out.println("True Mean: " + mu);
        System.out.println("True Sigma: " + sigma);
        System.out.println("True Sigma^2: " + StrictMath.pow(sigma, 2));
        System.out.println("Sample Mean: " + stats.mean());
        System.out.println("Sample Sigma: " + stats.standardDeviation());
        System.out.println("Sample Sigma^2: " + stats.variance());
        System.out.println();
    }

    public static void createHistogram(DescriptiveStatistics stats, Optional<Integer> bins) {
        if (SHOW_HISTOGRAMS) {
            Histogram histogram = bins.map(integer -> new Histogram(stats, integer))
                                      .orElseGet(() -> new Histogram(stats, PRINT_BINS));
            histogram.print();
        }
    }
}
