import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Random;


public class Main {
    public static char[] rowGenerator(int lengthOfRow, int numbersOfHiddenCharacter) {

        // Reference: https://stackoverflow.com/questions/2626835/is-there-functionality-to-generate-a-random-character-in-java
        Random r = new Random();
        String input = "XO";
        char[] result = new char[lengthOfRow];
        for (int i = 0; i < lengthOfRow; i++) {
            result[i] = input.charAt(r.nextInt(input.length()));
        }

        int count = 0;

        do {
            // Reference: https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java

            int randomIndex = ThreadLocalRandom.current().nextInt(0, lengthOfRow);
            if (result[randomIndex] != '#') {
                result[randomIndex] = '#';
                count++;
            }
        } while (count < numbersOfHiddenCharacter);  // if count reaches the number of '#', break the

        return result;
    }

    public static void main(String[] args) {
        // write your code here

        // Create a temporary

        PrintStream ps = null;
        try {
            // Create a file named "out.txt" to record all data after running
            File file = new File("out.txt");

            // Reference: https://stackoverflow.com/questions/8544771/how-to-write-data-with-fileoutputstream-without-losing-old-data
            // Reference: https://stackoverflow.com/questions/1994255/how-to-write-console-output-to-a-txt-file

            // I am using this below code to keep the old data when writing new data into file.

            char[] input = rowGenerator(10, 4);
            ps = new PrintStream(new FileOutputStream(file, true));
            ps.println("=========Here is the output of string " + convertArrayOfLettersToString(input)
                    + "    ===========");
            System.out.println("=========Here is the output of string " + convertArrayOfLettersToString(input)
                    + "    ===========");

            ps.println("Output of recursive method: ");
            System.out.println("Output of recursive method: ");

            // Reference: https://stackoverflow.com/questions/180158/how-do-i-time-a-methods-execution-in-java
            // Measure running time of recursiveUnHide method
            long startTime = System.nanoTime();
            recursiveUnHide(0, input, ps);
            long endTime = System.nanoTime();
            // Divide duration by 10^9 to get the time in second
            double timesInSeconds = (endTime - startTime) / 1000000000.0;
            System.out.println("Running time of recursive method is " + timesInSeconds + " seconds");
            ps.println("Running time of recursive method is " + timesInSeconds + " seconds");

            // Display result of iterative method and measure its running time
            System.out.println("Output of Iterative method: ");
            ps.println("Output of Iterative method: ");
            startTime = System.nanoTime();
            IterativeUnHide(input, ps);
            endTime = System.nanoTime();
            timesInSeconds = (endTime - startTime) / 1000000000.0;
            System.out.println("Running time of iterative method is "+timesInSeconds+" seconds.");
            ps.println("Running time of iterative method is "+timesInSeconds+" seconds.");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot open the output file");
        }
        ps.close();

    }

    public static void IterativeUnHide(char[] input, PrintStream ps) {
        ArrayList<String> list = new ArrayList<String>();

        // Here, I create a temporary string to keep data of input before using input for the below loop
        String tempString = convertArrayOfLettersToString(input);

        // This is the number of # sign in the string input
        int numberOfHiddenCharacter = countNumberOfHiddenCharacter(tempString);

        // Set X and O to be two elements that will replace #
        String containedLetters = "XO";
        Random r = new Random();
        do {
            for (int i = 0; i < input.length; i++) {
                if (input[i] == '#') {
                    // Replace # by a random letter between X and O
                    input[i] = containedLetters.charAt(r.nextInt(containedLetters.length()));
                }
            }
            // After the above loop, we have a completely new string from input
            String newElement = convertArrayOfLettersToString(input);
            if (!checkIfAlreadyContain(newElement, list)) {
                // Add the new element to the list if the list hasn't contained it yet.
                list.add(newElement);
            }
            // Here I change input to be its original form, so that we can use input again.
            input = convertStringToArrayOfLetters(tempString);

        } while (list.size() < (int) (Math.pow(2, numberOfHiddenCharacter)));
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            ps.println(list.get(i));
        }

    }

    // This method is written by the recursive method
    public static void recursiveUnHide(int tempPosition, char[] arrayOfLetters, PrintStream ps) {
        if (tempPosition == arrayOfLetters.length) {
            ps.println(convertArrayOfLettersToString(arrayOfLetters));
            System.out.println(convertArrayOfLettersToString(arrayOfLetters));
            return;
        }
        if (arrayOfLetters[tempPosition] == '#') {
            arrayOfLetters[tempPosition] = 'X';
            recursiveUnHide(tempPosition + 1, arrayOfLetters, ps);
            arrayOfLetters[tempPosition] = 'O';
            recursiveUnHide(tempPosition + 1, arrayOfLetters, ps);
            arrayOfLetters[tempPosition] = '#';
        } else {
            recursiveUnHide(tempPosition + 1, arrayOfLetters, ps);
        }

    }

    public static boolean checkIfAlreadyContain(String text, ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (text.equals(list.get(i))) {
                return true; // return true if list already contained text
            }
        }
        return false; //If the list doesn't contain text
    }

    public static int countNumberOfHiddenCharacter(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '#') {
                count++;
            }
        }
        return count;
    }

    public static char[] convertStringToArrayOfLetters(String string) {
        char array[] = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            array[i] = string.charAt(i);
        }
        return array;
    }

    public static String convertArrayOfLettersToString(char[] array) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result += array[i];
        }
        return result;
    }


}



