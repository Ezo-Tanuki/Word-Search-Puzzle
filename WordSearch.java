import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.nio.Buffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordSearch {
    public WordSearch() throws IOException{
        puzzleStream = openFile("Enter puzzle file");
        wordStream = openFile("Enter dictionary file");
        System.out.println("Reading Files...");
        readPuzzle();
        readWords();
    }

    public int solvePuzzle() {
        int numFound = 0;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < columns; c++){
                numFound += solveDirection(r, c, 1, 0);
                numFound += solveDirection(r, c, 1, 1);
                numFound += solveDirection(r, c, 0, 1);
                numFound += solveDirection(r, c, -1, 1);
                numFound += solveDirection(r, c, -1, 0);
                numFound += solveDirection(r, c, -1, -1);
                numFound += solveDirection(r, c, 1, -1);
                numFound += solveDirection(r, c, 0, -1);
            }
        }
        return numFound;
    }

    private int rows;
    private int columns;
    private char theBoard[][];
    private String[] theWords;
    private BufferedReader puzzleStream;
    private BufferedReader wordStream;
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private static int prefixSearch(String[] a, String x){
        return Arrays.binarySearch(a, x);
    }
    private BufferedReader openFile(String message){
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;

        do {
            System.out.println(message + ": ");

            try
            {
                fileName = in.readLine();
                if(fileName == null) System.exit(0);
                theFile = new FileReader(fileName);
                fileIn = new BufferedReader(theFile);
            }
            catch( IOException e) {System.err.println("Cannot open " + fileName);}
        }

        while(fileIn == null);

        return fileIn;
    }
    private void readWords() throws IOException{
        List<String> words = new ArrayList<>();

        String lastWord = null;
        String thisWord;

        while((thisWord = wordStream.readLine()) != null){
            if(lastWord != null && thisWord.compareTo(lastWord) < 0)
            {
                System.err.println("Dictionary is not sorted... skipping");
            }
            words.add(thisWord);
            lastWord = thisWord;
        }

        theWords = new String[words.size()];
        theWords = words.toArray(theWords);
    }
    private void readPuzzle() throws IOException{
        String oneLine;
        List<String> puzzleLines = new ArrayList<>();

        if((oneLine = puzzleStream.readLine()) == null) throw new IOException("No lines in puzzle file");

        columns = oneLine.length();
        puzzleLines.add(oneLine);

        while((oneLine = puzzleStream.readLine()) != null){
            if(oneLine.length() != columns) System.err.println("Puzzle is not rectangular; skipping row");
            else puzzleLines.add(oneLine);
        }
        rows = puzzleLines.size();
        theBoard = new char[rows][columns];

        int r = 0;
        for(String theLine : puzzleLines) theBoard[r++] = theLine.toCharArray();
    }
    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta){
//        String theWord = String.valueOf(theBoard[baseRow][baseCol]);
        String theWord = "";
        int found = 0;
        int x = baseCol, y = baseRow;

        int numFound = 0;
        while(true) {
            if(x < 0 || y < 0 || x >= columns || y >= rows) break;
            theWord += String.valueOf(theBoard[x][y]);
            x += colDelta;
            y += rowDelta;
            found += prefixSearch(theWords, theWord) < 0 ? 0 : 1;
        }

        if(found > 0){
            System.out.printf("Found matches in [%d, %d] to [%d, %d]\n", baseRow, baseCol, x, y);
        }

        return found;
    }
}
