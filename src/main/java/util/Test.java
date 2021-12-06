package util;

import lock14.random.stats.DescriptiveStatistics;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {
        Random r = new Random();
        List<Double> x = IntStream.range(0, 100)
                                  .mapToObj(Double::valueOf)
                                  .collect(Collectors.toList());
        List<Double> y = x.stream()
                          .map(n -> (1.85 * n) + 5 + (2 * r.nextDouble()))
                          .collect(Collectors.toList());
        
        DescriptiveStatistics X = new DescriptiveStatistics(x);
        DescriptiveStatistics Y = new DescriptiveStatistics(y);

        double x_bar = X.mean();
        double y_bar = Y.mean();
        
        double num = 0.0;
        for (int i = 0; i < x.size(); i++) {
            num += (x.get(i) - x_bar) * (y.get(i) - y_bar);
            
        }
        double denom = x.stream().mapToDouble(n -> (n - x_bar) * (n - x_bar))
                                 .sum();

        double b1 = num / denom;
        double b0 = y_bar - (b1 * x_bar);
        
        System.out.println(b1);
        System.out.println(b0);
    }
}
