import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WordReducer {

    public static void main(String[] args) {
        try {
            Map<Integer, Set<String>> allWordsByLength = loadAllWords();
            long beforeStart = System.currentTimeMillis();
            allWordsByLength.put(1, new HashSet<>(Arrays.asList("A", "I")));
            Set<String> nineLetterWords = allWordsByLength.getOrDefault(9, new HashSet<>());
            Set<String> validWords = new HashSet<>();

            for (String word : nineLetterWords) {
                if (canReduceToSingleLetter(word, allWordsByLength)) {
                    validWords.add(word);
                }
            }

            System.out.printf("Valid Words: %s;\nFinished for %s ms.", validWords.size(), System.currentTimeMillis() - beforeStart);
//            validWords.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean canReduceToSingleLetter(String word, Map<Integer, Set<String>> wordsByLength) {
        int length = word.length();
        if (length == 1) {
            return true;
        }
        Set<String> shorterWords = wordsByLength.getOrDefault(length - 1, new HashSet<>());
        for (int i = 0; i < length; i++) {
            String reducedWord = word.substring(0, i) + word.substring(i + 1);
            if (shorterWords.contains(reducedWord) && canReduceToSingleLetter(reducedWord, wordsByLength)) {
                return true;
            }
        }
        return false;
    }

    private static Map<Integer, Set<String>> loadAllWords() throws IOException {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            return br.lines().skip(2).collect(Collectors.groupingBy(String::length, Collectors.toSet()));
        }
    }
}
