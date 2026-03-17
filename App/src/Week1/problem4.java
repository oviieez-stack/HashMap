package Week1;

import java.util.*;

public class problem4 {

    // n-gram index: ngram -> set of document IDs
    static HashMap<String, Set<String>> index = new HashMap<>();

    static int N = 3; // you can change to 5 or 7

    // Generate n-grams
    public static List<String> generateNGrams(String text) {
        List<String> ngrams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    // Add document to index
    public static void addDocument(String docId, String text) {
        List<String> ngrams = generateNGrams(text);

        for (String ng : ngrams) {
            index.putIfAbsent(ng, new HashSet<>());
            index.get(ng).add(docId);
        }
    }

    // Analyze document
    public static void analyzeDocument(String docId, String text) {
        List<String> ngrams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String ng : ngrams) {
            if (index.containsKey(ng)) {
                for (String existingDoc : index.get(ng)) {
                    if (!existingDoc.equals(docId)) {
                        matchCount.put(existingDoc,
                                matchCount.getOrDefault(existingDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Analyzing: " + docId);
        System.out.println("Total n-grams: " + ngrams.size());

        for (String otherDoc : matchCount.keySet()) {
            int matches = matchCount.get(otherDoc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matched with " + otherDoc + ": " +
                    matches + " n-grams → Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("⚠️ PLAGIARISM DETECTED with " + otherDoc);
            }
        }
    }

    public static void main(String[] args) {

        // Existing documents
        addDocument("essay_089", "data structures and algorithms are important for coding");
        addDocument("essay_092", "data structures and algorithms are very important for coding practice");

        // New document
        String newEssay = "data structures and algorithms are important for coding";

        analyzeDocument("essay_123", newEssay);
    }
}