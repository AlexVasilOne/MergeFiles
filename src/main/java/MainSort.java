import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.Queue;

public class MainSort {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("run config not set");
            return;
        }
        // Path to current directory
        final Path currDir = Paths.get(System.getProperty("user.dir"));
        Queue<Path> inFiles = new ArrayDeque<>();
        boolean order = true;
        Path out;
        Path result;
        int i;
        if (args[0].equals("-a") || args[0].equals("-d")) { // order defined
            if (args[0].equals("-d")) {
                order = false;
            }
            out = currDir.resolve(Paths.get(args[1]));
            i = 2;
        } else {
            out = currDir.resolve(Paths.get(args[0]));
            i = 1;
        }
        while (i < args.length) {
            Path infile = currDir.resolve(Paths.get(args[i]));
            if (Files.isRegularFile(infile)) {
                inFiles.add(infile);
            }
            i++;
        }
        if (inFiles.size() != 0) {
            result = Merge.getMergeResult(order, inFiles);
        } else {
            System.out.println("input files do not exist");
            return;
        }
        try {
            Files.move(result, out, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("failed to copy file to current directory");
        }
    }
}
