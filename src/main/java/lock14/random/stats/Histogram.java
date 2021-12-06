package lock14.random.stats;

import java.util.List;

public class Histogram<N extends Number> {
    public static double PRINT_SCALE = 0.04;

    private final double[] freq; // freq[bin] = # occurrences in that bin
    private double max;  // max frequency of any bin

    public Histogram(List<N> samples) {
        this(samples, null);
    }

    public Histogram(List<N> samples, Integer bins) {
        this(new DescriptiveStatistics(samples), bins);
    }

    public Histogram(DescriptiveStatistics stats) {
        this(stats, null);
    }

    public Histogram(DescriptiveStatistics stats, Integer bins) {
        if (bins == null) {
            double h = 2 * stats.IQR() * Math.pow(stats.numSamples(), -(1.0 / 3.0));
            bins = (int) Math.ceil(stats.range() / h);
        }
        freq = new double[bins];
        addDataPoints(stats);
    }

    private void addDataPoints(DescriptiveStatistics stats) {
        double range = stats.range();
        double min = stats.min();
        double max = stats.max();
        int bins = freq.length;
        double binLength = range / bins;
        List<Double> sortedSamples = stats.sortedSamples();

        for (double sample : sortedSamples) {
            if (sample == max) {
                addDataPoint(freq.length - 1);
            } else {
                addDataPoint((int) Math.floor((sample - min) / binLength));
            }
        }
    }

    // Add one occurrence to the bin.
    private void addDataPoint(int bin) {
        freq[bin]++;
        if (freq[bin] > max) {
            max = freq[bin];
        }
    }

    public void print() {
        int scale = (int) (max * PRINT_SCALE);
        for (int bin = 0; bin < freq.length; bin++) {
            String label = String.format("%02d: ", (bin + 1));
            int scaled_freq = (int) (freq[bin] / scale);
            System.out.println(label + convertToStars(scaled_freq));
        }
    }

    private String convertToStars(int num) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < num; j++) {
            builder.append('*');
        }
        return builder.toString();
    }
}
