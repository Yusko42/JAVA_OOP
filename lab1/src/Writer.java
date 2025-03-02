import java.io.*;
import java.util.Map;

public class Writer {
    private final Map<String, Integer> wordCount;
    private BufferedWriter csvWriter = null;

    public Writer (Map<String, Integer> words) {
        wordCount = words;
    }

    public void writeWordsIntoCSVFile(String fileName, double number){
        try {
            File file = new File(fileName);

            csvWriter = new BufferedWriter(new FileWriter(file));
            csvWriter.write("Word;Frequency;Frequency in %");
            csvWriter.newLine();

            wordCount.entrySet()
                    .stream()
                    .sorted((a, b) -> {
                        int freqCompare = Integer.compare(b.getValue(), a.getValue());             //Sorted by frequency
                        return (freqCompare != 0) ? freqCompare : a.getKey().compareTo(b.getKey());//Sorted by alphabet
                    })
                    .forEach(entry ->  {
                        try {
                            double frequency = (entry.getValue() /  number);
                            csvWriter.write(entry.getKey() + ";" + String.format("%.4f",frequency) + ";" +
                                    String.format("%.4f", frequency * 100));
                            csvWriter.newLine();
                        } catch (IOException e) {
                            System.err.println("ERROR in the process of writing:" + e.getLocalizedMessage());
                            System.exit(2);
                        }
                    });
        } catch (IOException e) {
            System.err.println("ERROR while writing into a file:" + e.getLocalizedMessage());
            System.exit(3);
        } finally {
            if (csvWriter != null)
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
        }
    }
}