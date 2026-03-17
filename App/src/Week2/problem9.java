package Week1;

import java.util.*;

public class Problem9 {

    // Transaction class
    static class Transaction {
        int id;
        int amount;
        String merchant;
        long time; // store as millis

        Transaction(int id, int amount, String merchant, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.time = time;
        }
    }

    // ------------------ TWO SUM ------------------
    public static void findTwoSum(List<Transaction> list, int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : list) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction t2 = map.get(complement);
                System.out.println("Two-Sum Pair: (" + t2.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // ------------------ TWO SUM WITH TIME WINDOW ------------------
    public static void findTwoSumWithTime(List<Transaction> list, int target) {
        HashMap<Integer, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                for (Transaction prev : map.get(complement)) {
                    if (Math.abs(t.time - prev.time) <= 3600000) { // 1 hour
                        System.out.println("Time-Window Pair: (" + prev.id + ", " + t.id + ")");
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
    }

    // ------------------ DUPLICATE DETECTION ------------------
    public static void detectDuplicates(List<Transaction> list) {
        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {
            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {
            List<Transaction> group = map.get(key);
            if (group.size() > 1) {
                System.out.print("Duplicate: ");
                for (Transaction t : group) {
                    System.out.print(t.id + " ");
                }
                System.out.println();
            }
        }
    }

    // ------------------ K-SUM ------------------
    public static void findKSum(List<Transaction> list, int k, int target) {
        kSumHelper(list, k, target, 0, new ArrayList<>());
    }

    private static void kSumHelper(List<Transaction> list, int k, int target,
                                   int start, List<Integer> path) {

        if (k == 0 && target == 0) {
            System.out.println("K-Sum: " + path);
            return;
        }

        if (k <= 0 || target < 0) return;

        for (int i = start; i < list.size(); i++) {
            path.add(list.get(i).id);
            kSumHelper(list, k - 1, target - list.get(i).amount, i + 1, path);
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        long now = System.currentTimeMillis();

        transactions.add(new Transaction(1, 500, "StoreA", now));
        transactions.add(new Transaction(2, 300, "StoreB", now + 1000));
        transactions.add(new Transaction(3, 200, "StoreC", now + 2000));
        transactions.add(new Transaction(4, 500, "StoreA", now + 3000));

        // Two Sum
        findTwoSum(transactions, 500);

        // Time Window
        findTwoSumWithTime(transactions, 500);

        // Duplicate Detection
        detectDuplicates(transactions);

        // K-Sum
        findKSum(transactions, 3, 1000);
    }
}