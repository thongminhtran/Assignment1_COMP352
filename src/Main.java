import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Random;


public class Main {
    public static char hidden = '#';
    public static char circle = 'O';
    public static char notCircle = 'X';
    public static String outputPath = "out.txt";
    public static void main(String[] args) {
        try {
            // Create a file named "out.txt" to record all data after running
            File file = new File(outputPath);

            // Reference: https://stackoverflow.com/questions/8544771/how-to-write-data-with-fileoutputstream-without-losing-old-data
            // Reference: https://stackoverflow.com/questions/1994255/how-to-write-console-output-to-a-txt-file

            // I am using this below code to keep the old data when writing new data into file.

            char[] input = rowGenerator(10, 4);
            // Convert input to String
            String inputString = new String(input);
            PrintStream printStream = new PrintStream(new FileOutputStream(file, true));
            printStream.println("=========Here is the output of string " + inputString
                    + "    ===========");
            System.out.println("=========Here is the output of string " + inputString
                    + "    ===========");

            printStream.println("Output of recursive method: ");
            System.out.println("Output of recursive method: ");

            // Reference: https://stackoverflow.com/questions/180158/how-do-i-time-a-methods-execution-in-java
            // Measure running time of recursiveUnHide method
            long startTime = System.nanoTime();
            recursiveUnHide(0, input, printStream);
            long endTime = System.nanoTime();
            // Divide duration by 10^9 to get the time in second
            double timesInSeconds = (endTime - startTime) / 1000000000.0;
            System.out.println("Running time of recursive method is " + timesInSeconds + " seconds");
            printStream.println("Running time of recursive method is " + timesInSeconds + " seconds");

            // Display result of iterative method and measure its running time
            System.out.println("Output of Iterative method: ");
            printStream.println("Output of Iterative method: ");
            startTime = System.nanoTime();
            iterativeUnHide(input, printStream);
            endTime = System.nanoTime();
            timesInSeconds = (endTime - startTime) / 1000000000.0;
            System.out.println("Running time of iterative method is "+timesInSeconds+" seconds.");
            printStream.println("Running time of iterative method is "+timesInSeconds+" seconds.");
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot open the output file");
        }
    }
    public static char generateRandomPossibleCharacter() {
        Random random = new Random();
        char[] possibleInputChars = {notCircle, circle};
        int randomPositionToPickup = random.nextInt(possibleInputChars.length);
        return possibleInputChars[randomPositionToPickup];
    }
    public static char[] rowGenerator(int numberOfCharacters, int numbersOfHiddenCharacter) {

        // Reference: https://stackoverflow.com/questions/2626835/is-there-functionality-to-generate-a-random-character-in-java
        char[] generatedRow = new char[numberOfCharacters];
        for (int i = 0; i < numberOfCharacters; i++) {
            generatedRow[i] = generateRandomPossibleCharacter();
        }

        int numberOfHiddenCharactersAssigned = 0;

        do {
            // Reference: https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java

            int randomPositionToAssign = ThreadLocalRandom.current().nextInt(0, numberOfCharacters);
            boolean randomPositionNotHidden = generatedRow[randomPositionToAssign] != hidden;
            if (randomPositionNotHidden) {
                generatedRow[randomPositionToAssign] = hidden;
                numberOfHiddenCharactersAssigned++;
            }
        } while (numberOfHiddenCharactersAssigned < numbersOfHiddenCharacter);  // if count reaches the number of '#', break the

        return generatedRow;
    }

    /**
     * Method Unhide using Iterative solution
     * @param input generated row that we want to unhide
     * @param ps the stream that we want to write to the output file
     */
    public static void iterativeUnHide(char[] input, PrintStream ps) {
        char[] originalInput = input.clone();
        int numberOfHiddenCharacters = 0;
        // This is the number of # sign in the string input
        for(char character: input) {
            if (character == hidden) numberOfHiddenCharacters++;
        }
        int maximumPossibleCases = (int)(Math.pow(2, numberOfHiddenCharacters));
        String[] possibleCases = new String[maximumPossibleCases];
        int currentIndex = 0;
        do {
            for (int i = 0; i < originalInput.length; i++) {
                if (originalInput[i] == hidden) {
                    // Replace # by a random letter between X and O
                    originalInput[i] = generateRandomPossibleCharacter();
                }
            }
            String unHiddenCharacters = new String(originalInput);

            boolean doesPossibleCasesContainThisCase = false;
            for (int index = 0; index < currentIndex; index++) {
                if (possibleCases[index].equals(unHiddenCharacters)) {
                    doesPossibleCasesContainThisCase = true;
                    break;
                }
            }
            if (!doesPossibleCasesContainThisCase) {
                possibleCases[currentIndex] = unHiddenCharacters;
                System.out.println(unHiddenCharacters);
                ps.println(unHiddenCharacters);
                currentIndex = currentIndex+1;
            }
            originalInput = input.clone();
        } while (currentIndex < maximumPossibleCases);
    }

    /**
     * Unhide method using recursive
     * @param currentPosition: Start with a position in the input
     * @param input: array of characters that I want to unhide
     * @param ps to print to file
     */
    public static void recursiveUnHide(int currentPosition, char[] input, PrintStream ps) {
        boolean isLastPosition = currentPosition == input.length;
        if (isLastPosition) {
            String stringOfLetters = new String(input);
            ps.println(stringOfLetters);
            System.out.println(stringOfLetters);
            return;
        }
        boolean isCurrentPositionHidden = input[currentPosition] == hidden;
        if (isCurrentPositionHidden) {
            input[currentPosition] = notCircle;
            recursiveUnHide(currentPosition + 1, input, ps);
            input[currentPosition] = circle;
            recursiveUnHide(currentPosition + 1, input, ps);
            input[currentPosition] = hidden;
        } else {
            recursiveUnHide(currentPosition + 1, input, ps);
        }

    }
}



