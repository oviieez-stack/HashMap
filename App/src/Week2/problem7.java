package Week1;

import java.util.*;

public class problem7 {

    // Trie Node
    static class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        String word = "";
    }

    static TrieNode root = new TrieNode();

    // Global frequency map
    static HashMap<String, Integer> freqMap = new HashMap<>();

    // Insert word into Trie
    public static void insert(String word) {
        TrieNode node = root;

        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }

        node.isEnd = true;
        node.word = word;
    }

    // Update frequency
    public static void updateFrequency(String word) {
        freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        insert(word);
    }

    // DFS to collect words
    public static void dfs(TrieNode node, List<String> result) {
        if (node.isEnd) {
            result.add(node.word);
        }

        for (char ch : node.children.keySet()) {
            dfs(node.children.get(ch), result);
        }
    }

    // Search prefix
    public static List<String> search(String prefix) {
        TrieNode node = root;

        for (char ch : prefix.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new ArrayList<>();
            }
            node = node.children.get(ch);
        }

        List<String> words = new ArrayList<>();
        dfs(node, words);

        // Sort by frequency (top 10)
        words.sort((a, b) -> freqMap.getOrDefault(b, 0) - freqMap.getOrDefault(a, 0));

        return words.subList(0, Math.min(10, words.size()));
    }

    public static void main(String[] args) {

        // Add queries
        updateFrequency("java tutorial");
        updateFrequency("javascript");
        updateFrequency("java download");
        updateFrequency("java tutorial");
        updateFrequency("java tutorial");

        // Search
        List<String> results = search("jav");

        System.out.println("Suggestions:");
        int rank = 1;
        for (String res : results) {
            System.out.println(rank + ". " + res + " (" + freqMap.get(res) + ")");
            rank++;
        }
    }
}