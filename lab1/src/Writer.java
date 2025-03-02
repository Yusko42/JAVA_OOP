import java.io.*;
import java.util.Map;

public class Writer {
    private Map<String, Integer> wordCount;
    //private BufferedWriter csvWriter = null;

    public Writer (String fileName, double number, Map<String, Integer> wordCount) {
        try {
            File file = new File(fileName);

            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file));

            wordCount.entrySet()
                    .stream()
                    .sorted((a, b) -> {
                        int freqCompare = Integer.compare(b.getValue(), a.getValue());             //Sorted by frequency
                        return (freqCompare != 0) ? freqCompare : a.getKey().compareTo(b.getKey());//Sorted by alphabet
                    })
                    .forEach(entry -> /*System.out.println(entry.getKey() + ": " + entry.getValue())*/ {
                        try {
                            csvWriter.write(entry.getKey() + ";" + entry.getValue() + ";" +
                                            ((entry.getValue() /  number) * 100));
                            csvWriter.newLine();
                        } catch (IOException e) {
                            System.err.println("ERROR in the process of writing:" + e.getLocalizedMessage());
                            System.exit(2);
                        }
                    });
            csvWriter.close();
        }
        catch (IOException e) {
            System.err.println("ERROR while writing into a file:" + e.getLocalizedMessage());
        }
    }
}