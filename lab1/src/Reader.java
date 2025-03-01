import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Reader {
    /*** Private Section */
    private Map<String, Integer> wordCount = new HashMap<>();
    private InputStreamReader reader = null;

    //Constructor...
    public Reader(String inputFile) {
        try {
            reader = new InputStreamReader(new FileInputStream(inputFile)); /*, StandardCharsets.UTF_16*/

            StringBuilder word = new StringBuilder();
            int ch;

            while ((ch = reader.read()) != -1) {
                if (Character.isLetterOrDigit(ch)) //PROBLEM: words with "-"
                    word.append((char) ch);
                else if (!word.isEmpty()) {
                    String string = word.toString().toLowerCase();
                    wordCount.put(string, wordCount.getOrDefault(string, 0) + 1);
                    word.setLength(0);
                }
            }
            if (!word.isEmpty()) {
                String string = word.toString().toLowerCase(); //rename it!!!
                wordCount.put(string, wordCount.getOrDefault(string, 0) + 1);
            }

        } catch (IOException e) {
            System.err.println("ERROR while reading file:" + e.getLocalizedMessage());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

    public void CollectWordsFromTextFile(){}
}
