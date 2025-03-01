import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("ERROR: " + args.length + " - the number of arguments.\n" +
                    "Please input in this format: [input_file.txt] [output_file.csv].");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];

        //place where all the words will be stored
        Map<String, Integer> wordCount = new HashMap<>();

        //1 - READING FROM FILE: SHOULD BE ANOTHER OBJECT
        Reader wordReader = new Reader(inputFile);

        //2 - WRITING TO CSV FILE: SHOULD BE ANOTHER OBJECT
        Writer wordWriter = new Writer(wordReader.getWordCount());

        System.out.println("Completed!");

    }
}