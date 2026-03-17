package Week2;

import java.util.*;

public class problem6 {

    // Token Bucket class
    static class TokenBucket {
        int tokens;
        long lastRefillTime;

        int maxTokens = 1000;         // limit per hour
        double refillRate = 1000.0 / 3600; // tokens per second

        TokenBucket() {
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on time
        void refill() {
            long now = System.currentTimeMillis();
            double seconds = (now - lastRefillTime) / 1000.0;

            int tokensToAdd = (int) (seconds * refillRate);

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }

        // Consume token
        synchronized boolean allowRequest() {
            refill();

            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }
    }

    // clientId -> TokenBucket
    static HashMap<String, TokenBucket> clients = new HashMap<>();

    // Check rate limit
    public static String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket());
        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            return "Denied (0 requests remaining)";
        }
    }

    // Status
    public static void getRateLimitStatus(String clientId) {
        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No data for client");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("{used: " + used +
                ", limit: " + bucket.maxTokens + "}");
    }

    public static void main(String[] args) {

        String client = "abc123";

        // Simulate requests
        for (int i = 0; i < 5; i++) {
            System.out.println(checkRateLimit(client));
        }

        getRateLimitStatus(client);
    }
}