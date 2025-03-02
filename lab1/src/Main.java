public class Main{
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println("ERROR: " + args.length + " - this number of arguments is not suitable.\n" +
                    "Please input in this format: <input_file.txt> <output_file.csv>.");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];

        //1 - READING FROM A TXT FILE
        Reader wordReader = new Reader();
        wordReader.collectWordsFromTextFile(inputFile);

        //2 - WRITING TO A CSV FILE
        Writer wordWriter = new Writer(wordReader.getWordCount());
        wordWriter.writeWordsIntoCSVFile(outputFile, wordReader.getNumberOfWords());

        System.out.println("Completed! The result is in the file: " + outputFile);
    }
}