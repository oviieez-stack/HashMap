package Week1;

import java.util.*;

public class problem3 {

    // DNS Entry class
    static class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // LRU Cache using LinkedHashMap
    static int MAX_SIZE = 5;

    static LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_SIZE;
                }
            };

    static int hits = 0;
    static int misses = 0;

    // Simulate upstream DNS
    public static String queryUpstream(String domain) {
        return "192.168.1." + new Random().nextInt(100);
    }

    // Resolve domain
    public static String resolve(String domain) {

        long start = System.nanoTime();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                long end = System.nanoTime();
                System.out.println(domain + " → Cache HIT → " + entry.ip +
                        " (" + (end - start) + " ns)");
                return entry.ip;
            } else {
                cache.remove(domain);
                System.out.println(domain + " → Cache EXPIRED");
            }
        }

        // Cache miss
        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(ip, 5)); // TTL = 5 sec (demo)

        System.out.println(domain + " → Cache MISS → " + ip);
        return ip;
    }

    // Cache stats
    public static void getCacheStats() {
        int total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits * 100.0 / total);

        System.out.println("Hit Rate: " + hitRate + "%");
        System.out.println("Hits: " + hits + ", Misses: " + misses);
    }

    public static void main(String[] args) throws InterruptedException {

        resolve("google.com"); // MISS
        resolve("google.com"); // HIT

        Thread.sleep(6000); // wait for expiry

        resolve("google.com"); // EXPIRED → MISS

        resolve("facebook.com");
        resolve("amazon.com");
        resolve("youtube.com");
        resolve("twitter.com");
        resolve("linkedin.com"); // triggers LRU eviction

        getCacheStats();
    }
}