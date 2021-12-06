package lock14.random;

import lock14.random.distribution.Distribution;
import lock14.random.distribution.Geometric;

import java.util.Scanner;

public class DropChanceCalc {
    public static void main(String[] args) {
        double dropChance;
        try (Scanner console = new Scanner(System.in)) {
            System.out.print("Input drop chance as percentage: ");
            dropChance = console.nextDouble() / 100;
        }
        Distribution<Integer> dist = new Geometric(dropChance);
        System.out.println("Average number of runs until first drop: " + dist.mean());
        System.out.println("Number of runs to achieve 95% chance of a drop occurring: " + dist.inverseCdf(0.95));
    }
}
