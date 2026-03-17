package Week1;

import java.util.*;

public class problem2 {

    // productId -> stock count
    static HashMap<String, Integer> inventory = new HashMap<>();

    // productId -> waiting list (FIFO)
    static HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    // Add product
    public static void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock (O(1))
    public static int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    // Purchase item (Thread-safe)
    public static synchronized String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Out of stock, added to waiting list. Position #" + queue.size();
        }
    }

    // Show waiting list
    public static void showWaitingList(String productId) {
        System.out.println("Waiting List: " + waitingList.get(productId));
    }

    public static void main(String[] args) {

        addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " + checkStock("IPHONE15_256GB"));

        System.out.println(purchaseItem("IPHONE15_256GB", 101));
        System.out.println(purchaseItem("IPHONE15_256GB", 102));
        System.out.println(purchaseItem("IPHONE15_256GB", 103));

        // Now stock = 0 → waiting list starts
        System.out.println(purchaseItem("IPHONE15_256GB", 104));
        System.out.println(purchaseItem("IPHONE15_256GB", 105));

        showWaitingList("IPHONE15_256GB");
    }
}