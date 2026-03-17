package Week1;

import java.util.*;

public class problem10 {

    // L1 Cache (LRU)
    static int L1_SIZE = 3;
    static LinkedHashMap<String, String> L1 = new LinkedHashMap<>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, String> e) {
            return size() > L1_SIZE;
        }
    };

    // L2 Cache (LRU)
    static int L2_SIZE = 5;
    static LinkedHashMap<String, String> L2 = new LinkedHashMap<>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, String> e) {
            return size() > L2_SIZE;
        }
    };

    // L3 (Database simulation)
    static HashMap<String, String> DB = new HashMap<>();

    // Access count for promotion
    static HashMap<String, Integer> accessCount = new HashMap<>();

    // Stats
    static int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    // Get video
    public static String getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 Check
        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println(videoId + " → L1 HIT");
            return L1.get(videoId);
        }

        // L2 Check
        if (L2.containsKey(videoId)) {
            l2Hits++;
            String data = L2.get(videoId);

            // Promote to L1
            L1.put(videoId, data);
            System.out.println(videoId + " → L2 HIT → Promoted to L1");
            return data;
        }

        // L3 (DB)
        l3Hits++;
        String data = DB.getOrDefault(videoId, "VideoData_" + videoId);

        // Add to L2
        L2.put(videoId, data);

        System.out.println(videoId + " → L3 HIT → Added to L2");

        return data;
    }

    // Update access count
    public static void updateAccess(String videoId) {
        accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);
    }

    // Stats
    public static void getStats() {
        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("\n--- CACHE STATS ---");
        System.out.println("L1 Hits: " + l1Hits);
        System.out.println("L2 Hits: " + l2Hits);
        System.out.println("L3 Hits: " + l3Hits);

        double hitRate = total == 0 ? 0 : ((l1Hits + l2Hits) * 100.0 / total);
        System.out.println("Overall Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) {

        // Preload DB
        DB.put("video_123", "Movie_A");
        DB.put("video_999", "Movie_B");

        getVideo("video_123"); // L3
        getVideo("video_123"); // L1
        getVideo("video_999"); // L3
        getVideo("video_999"); // L1
        getVideo("video_123"); // L1

        getStats();
    }
}