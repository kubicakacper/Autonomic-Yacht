package yacht.sail;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadSailCoefficients {
    /*
    how to use:
    1. Create text file in the same folder as app's.
    2. File format: int double double int double double ...
    3. Use class:   ReadSailCoefficients.openFile("filename");
                    ReadSailCoefficients.readRecords(tab1, tab2);
                    ReadSailCoefficients.closeFile();
    */

    private static Scanner input;

    // open file
    public static void openFile(String fileName) {
        try {
            input = new Scanner(Paths.get(fileName));
        } catch (IOException ioException) {
            System.err.println("Error opening file. Terminating.");
            System.exit(1);
        }
    }

    // read record from file
    public static void readRecords(double[] liftCoefficient, double[] dragCoefficient) {
        try {
            int i;
            while (input.hasNext()) {// while there is more to read
                i = input.nextInt();
                liftCoefficient[i] = input.nextDouble();
                dragCoefficient[i] = input.nextDouble();
            }
        } catch (NoSuchElementException elementException) {
            System.err.println("File improperly formed. Terminating.");
        } catch (IllegalStateException stateException) {
            System.err.println("Error reading from file. Terminating.");
        }
    }

    // close file and terminate application
    public static void closeFile() {
        if (input != null)
            input.close();
    }
}
