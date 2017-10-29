package lock14.random;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Double> list = Arrays.asList(3.0, 5.0, 7.0, 8.0, 9.0, 11.0, 15.0, 16.0, 20.0, 21.0);
        System.out.println(percentile(list, 25));
        System.out.println(percentile(list, 50));
        System.out.println(percentile(list, 75));
        
        System.out.println();
        
        System.out.println(kth_q_quantile(1, 4, list));
        System.out.println(kth_q_quantile(2, 4, list));
        System.out.println(kth_q_quantile(3, 4, list));
        
        System.out.println();
        
        System.out.println(kth_q_quantile(1, 2, list));
        
        System.out.println();
        
        List<Double> list2 = Arrays.asList(1.0, 2.0, 5.0, 6.0, 7.0, 9.0, 12.0, 15.0, 18.0, 19.0, 27.0);
        System.out.println(percentile(list2, 25));
        System.out.println(percentile(list2, 50));
        System.out.println(percentile(list2, 75));
    }

    public static Double percentile(List<Double> list, int percentile) {
        return kth_q_quantile(percentile, 100, list);
    }
    
    public static double kth_q_quantile(int k, int q, List<Double> list) {
        if (k < 0 || q < 0) {
            throw new IllegalArgumentException();
        }
        double i = (k * (list.size() - 1)) / (double)q;
        if ((i == Math.floor(i)) && !Double.isInfinite(i)) {
            return list.get((int)i);
        } else {
            double lower = list.get((int)Math.floor(i));
            double upper = list.get((int)Math.ceil(i));
            double frac = i - Math.floor(i);
            return ((1 - frac) * lower) + (frac * upper);
        }
    }
}
