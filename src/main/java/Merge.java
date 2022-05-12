import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

public class Merge {
    // merge two files and return reference to the temp with result
    //return null if merge failed
    private static Path getMergeTemp(final Path inFile1, final Path inFile2, final boolean order) {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("data-", ".txt");
        } catch (IOException e) {
            System.out.printf("fail create temp file for merging files %s and %s",
                    inFile1.getFileName(), inFile2.getFileName());
            return null;
        }
        try (BufferedReader bR1 = Files.newBufferedReader(inFile1, StandardCharsets.UTF_8);
             BufferedReader bR2 = Files.newBufferedReader(inFile2, StandardCharsets.UTF_8);
             BufferedWriter bW = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            CorrectData correctData1 = new CorrectData(bR1, order);
            CorrectData correctData2 = new CorrectData(bR2, order);
            Integer currInt1 = correctData1.getCorrectInt();
            Integer currInt2 = correctData2.getCorrectInt();
            //writing to a temporary file occurs if the in files contain valid data
            while (!((currInt1 == null) && (currInt2 == null))) {
                if ((currInt1 != null) && (currInt2 != null)) {
                    if (compareOrder(currInt1, currInt2, order)) {
                        currInt1 = writeCurr(bW, currInt1, correctData1);
                    } else currInt2 = writeCurr(bW, currInt2, correctData2);
                } else if (currInt1 == null) {
                    currInt2 = writeCurr(bW, currInt2, correctData2);
                } else {
                    currInt1 = writeCurr(bW, currInt1, correctData1);
                }
            }
        } catch (IOException e) {
            System.out.printf("fail read/write from merging files %s and %s",
                    inFile1.getFileName(), inFile2.getFileName());
            return null;
        }
        tempFile.toFile().deleteOnExit(); // delete temp files after shutdown JVM
        return tempFile;
    }

    // return path to the temp file with correct data, without merge
    //use in case we have one initial file for merge
    //return null if merge is failed
    private static Path getMergeTemp(final Path inFile1, final boolean order) {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("data-", ".txt");
        } catch (IOException e) {
            System.out.printf("fail create temp file for correcting file %s",
                    inFile1.getFileName());
            return null;
        }
        try (BufferedReader bR1 = Files.newBufferedReader(inFile1, StandardCharsets.UTF_8);
             BufferedWriter bW = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            CorrectData correctData1 = new CorrectData(bR1, order);
            Integer currInt1 = correctData1.getCorrectInt();
            while (currInt1 != null) {
                currInt1 = writeCurr(bW, currInt1, correctData1);
            }
        } catch (IOException e) {
            System.out.printf("fail read/write for correcting files %s",
                    inFile1.getFileName());
            return null;
        }
        tempFile.toFile().deleteOnExit(); // delete temp files after shutdown JVM
        return tempFile;
    }

    //Write current int data and return next correct data from file.
    private static Integer writeCurr(final BufferedWriter bW, final Integer curr, final CorrectData correctData)
            throws IOException {
        bW.write(String.valueOf(curr));
        bW.newLine();
        return correctData.getCorrectInt();
    }

    private static boolean compareOrder(final int curr1, final int curr2, final boolean order) {
        if (order) return curr1 < curr2;
        else return curr1 > curr2;
    }

    // Return path to the temp file with correct data, with merge
    //all files in param. if order = true - ascending order.
    // return null if merge fail
    public static Path getMergeResult(final boolean order, Queue<Path> initialFiles) {
        if (initialFiles.size() == 1) {  // If we have one initial file for merge.
            if (initialFiles.peek() != null) {
                return getMergeTemp(initialFiles.peek(), order);
            }
        }
        Queue<Path> temp = new ArrayDeque<>();
        while (initialFiles.size() > 1) { // Merge all files in pair until one is left.
            while (!initialFiles.isEmpty()) {
                if (initialFiles.size() > 1) { // if initialFiles contain  pairs
                    Path pairMerge = getMergeTemp(initialFiles.poll(), initialFiles.poll(), order);
                    if (pairMerge != null) {
                        temp.add(pairMerge);
                    }
                } else { // if initialFiles not contain any pair
                    Path oneMerge = initialFiles.poll();
                    if (oneMerge != null) {
                        temp.add(oneMerge);
                    }
                }
            }
            initialFiles = temp;
            temp = new ArrayDeque<>();
        }
        return initialFiles.poll();
    }
}
