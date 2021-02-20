import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {
    static final private String errors_path = "errors.txt";
    static final private String sites_path = "sites.txt";
    static private boolean isFirstError = false;
    static private boolean isFirstSites = false;

    static public void writeErrors(String message) {
        try {
            if(!isFirstError) {
                clearFile(errors_path);
                isFirstError = true;
            }
            Files.write(Paths.get(errors_path), (message + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch(Exception e) { System.out.println(e.getMessage()); }
    }

    static public void writeSites(String message){
        try {
            if(!isFirstSites) {
                clearFile(sites_path);
                isFirstSites = true;
            }
            Files.write(Paths.get(sites_path), (message + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch(Exception e) { System.out.println(e.getMessage()); }
    }

    static public void clearFile(String path) throws Exception {
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }
}