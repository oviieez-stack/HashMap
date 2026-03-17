package Week1;
import java.util.*;

public class Problem1 {

    static HashMap<String, Integer> users = new HashMap<>();
    static HashMap<String, Integer> attempts = new HashMap<>();
    static int userIdCounter = 1;

    public static void register(String username) {
        users.put(username, userIdCounter++);
    }

    public static boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username); // ✅ FIXED
    }

    public static List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        if (username.contains("_")) {
            String alt = username.replace("_", ".");
            if (!users.containsKey(alt)) {
                suggestions.add(alt);
            }
        }

        return suggestions;
    }

    public static String getMostAttempted() {
        String maxUser = "";
        int maxCount = 0;

        for (String user : attempts.keySet()) {
            if (attempts.get(user) > maxCount) {
                maxCount = attempts.get(user);
                maxUser = user;
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    public static void main(String[] args) {

        register("john_doe");
        register("admin");

        System.out.println("john_doe → " + checkAvailability("john_doe"));
        System.out.println("jane_smith → " + checkAvailability("jane_smith"));

        System.out.println("Suggestions: " + suggestAlternatives("john_doe"));

        checkAvailability("admin");
        checkAvailability("admin");
        checkAvailability("admin");

        System.out.println("Most attempted: " + getMostAttempted());
    }
}