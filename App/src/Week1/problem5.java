package Week1;
import java.util.*;

public class problem5 {

    // page -> total visits
    static HashMap<String, Integer> pageViews = new HashMap<>();

    // page -> unique users
    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // source -> count
    static HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process event
    public static void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique users
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Track traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get Top 10 pages
    public static List<Map.Entry<String, Integer>> getTopPages() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        return list.subList(0, Math.min(10, list.size()));
    }

    // Dashboard output
    public static void getDashboard() {

        System.out.println("\n--- REAL-TIME DASHBOARD ---");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        System.out.println("Top Pages:");
        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url + " - " + views +
                    " views (" + unique + " unique)");
            rank++;
        }

        System.out.println("\nTraffic Sources:");
        for (String src : trafficSources.keySet()) {
            System.out.println(src + ": " + trafficSources.get(src));
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Simulate incoming events
        processEvent("/article/breaking-news", "user1", "google");
        processEvent("/article/breaking-news", "user2", "facebook");
        processEvent("/sports/championship", "user3", "google");
        processEvent("/article/breaking-news", "user1", "direct");
        processEvent("/sports/championship", "user4", "facebook");
        processEvent("/tech/ai", "user5", "google");

        // Simulate real-time update every 5 sec
        while (true) {
            getDashboard();
            Thread.sleep(5000);
        }
    }
}
