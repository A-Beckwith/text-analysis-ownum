import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class TextAnalysis {

    /*
        Reads the contents of provided file (passage.txt) into the program line by line
        Appends each line to a StringBuilder separated by a space
        Returns contents of the file as a single string
    */
    public String readFileToString(String filePath) {
        StringBuilder passageStringBuilder = new StringBuilder();

        try (Stream<String> myStream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            myStream.forEach(str -> passageStringBuilder.append(str).append(" "));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passageStringBuilder.toString();
    }

    /*
        Returns the word count of the text file
        Splits the text at spaces, and converts the resulting array to a stream
        Filters the stream to remove any elements that are only whitespace
    */
    public long getWordCount(String str) {
        return Arrays.stream(str.split(" ")).filter(s -> s.trim().length() > 0).count();
    }

    /*
        Returns a list of map entries representing the words sorted by frequency
    */
    public ArrayList<Map.Entry<String, Integer>> getTopWords(String str) {
        Map<String, Integer> wordMap = new HashMap<String, Integer>();

        // Remove all punctuation, split at spaces, remove elements of only whitespace
        Stream<String> wordStream = Arrays.stream(str.replaceAll("[^a-zA-Z\\- ]", "").toLowerCase().split(" ")).filter(s -> s.trim().length() > 0);

        // Add each word as a key to the map, count frequency of words
        wordStream.forEach(word -> {
            if (wordMap.containsKey(word)) {
                wordMap.put(word, wordMap.get(word) + 1);
            } else {
                wordMap.put(word, 1);
            }
        });

        // Sort new list by entry values, then reverse list
        ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        Collections.reverse(entryList);
        return entryList;
    }

    /*
        Returns the last sentence in the text that contains the most used word
    */
    public String getLastSentenceWithMostUsedWord(String str, String mostUsedWord) {
        // Create list of sentences by splitting text at periods
        ArrayList<String> sentenceList = new ArrayList<String>(Arrays.asList(str.split("\\.")));
        Collections.reverse(sentenceList);
        return sentenceList.stream().filter(s -> s.toLowerCase().contains(mostUsedWord)).findFirst().get();
    }

    public static void main(String[] args) {
        TextAnalysis textAnalysis = new TextAnalysis();
        String passage = textAnalysis.readFileToString("./passage.txt");

        // Get desired information
        long wordCount = textAnalysis.getWordCount(passage);
        ArrayList<Map.Entry<String, Integer>> mostUsedWords = textAnalysis.getTopWords(passage);
        String mostUsedWord = mostUsedWords.get(0).getKey();
        String lastSentenceWithMostUsedWord = textAnalysis.getLastSentenceWithMostUsedWord(passage, mostUsedWord);

        // Format and print values
        System.out.printf("\nWord count of the text file: %d\n\n", wordCount);
        System.out.println("Top 10 most used words in the file:");
        for(int i = 0; i < 10; i++) {
            Map.Entry<String, Integer> entry = mostUsedWords.get(i);
            System.out.printf("\t%d. %s: %d\n", i + 1, entry.getKey(), entry.getValue());
        }
        System.out.printf("\nLast sentence containing the most used word (%s):\n%s\n", mostUsedWord, lastSentenceWithMostUsedWord);
    }
}