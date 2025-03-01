import java.util.Map;

public class Writer {
    private Map<String, Integer> wordCount;
    public Writer (Map<String, Integer> wordCount) {
        wordCount.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}