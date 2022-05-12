import java.io.BufferedReader;
import java.io.IOException;


public class CorrectData {
    private final BufferedReader bufferedReader;
    private final boolean order; // if its true - ascending order
    private int countUse = 0; //count of returned correct lines
    private Integer prev = null;

    public CorrectData(BufferedReader bufferedReader, boolean order) {
        this.bufferedReader = bufferedReader;
        this.order = order;
    }

    public Integer getCorrectInt() throws IOException {
        String s;
        if (countUse == 0) { //if this is the first reading
            while (((s = bufferedReader.readLine()) != null)) {
                try {
                    prev = Integer.parseInt(s); //  Set prev.
                } catch (NumberFormatException e) { //if the string being read is not a digit, get next line
                    continue;
                }
                countUse++;
                return prev; // Return first correct digit.
            }
        }
        if (prev != null) { //Return next correct digit.
            while (((s = bufferedReader.readLine()) != null)) {
                int curr;
                try {
                    curr = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    continue;
                }
                if (getOrder(curr)) {
                    prev = curr;
                    return curr;
                }
            }
        }
        return null; // file does not contain valid data
    }

    private boolean getOrder(final int curr) {
        if (order) {
            return curr >= prev;
        } else {
            return curr <= prev;
        }
    }
}
