package Week1;

import java.util.*;

public class problem8 {

    static final int SIZE = 500;

    // Spot structure
    static class Spot {
        String vehicle;
        long entryTime;
        boolean occupied;

        Spot() {
            this.vehicle = null;
            this.occupied = false;
        }
    }

    static Spot[] table = new Spot[SIZE];

    static int totalProbes = 0;
    static int totalParks = 0;

    static {
        for (int i = 0; i < SIZE; i++) {
            table[i] = new Spot();
        }
    }

    // Hash function
    public static int hash(String plate) {
        return Math.abs(plate.hashCode()) % SIZE;
    }

    // Park vehicle
    public static void parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % SIZE; // linear probing
            probes++;
        }

        table[index].vehicle = plate;
        table[index].occupied = true;
        table[index].entryTime = System.currentTimeMillis();

        totalProbes += probes;
        totalParks++;

        System.out.println(plate + " → Assigned spot #" + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public static void exitVehicle(String plate) {
        int index = hash(plate);

        while (table[index].occupied) {
            if (plate.equals(table[index].vehicle)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMs / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                table[index].vehicle = null;

                System.out.println(plate + " exited from #" + index +
                        ", Duration: " + String.format("%.2f", hours) +
                        " hrs, Fee: $" + String.format("%.2f", fee));
                return;
            }
            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found");
    }

    // Statistics
    public static void getStatistics() {
        int occupied = 0;

        for (Spot s : table) {
            if (s.occupied) occupied++;
        }

        double occupancy = (occupied * 100.0) / SIZE;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) throws InterruptedException {

        parkVehicle("ABC-1234");
        parkVehicle("ABC-1235");
        parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate time

        exitVehicle("ABC-1234");

        getStatistics();
    }
}