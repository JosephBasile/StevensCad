package gutil;

import java.io.*;

/**
 *
 * @author dov
 */
public class FileUtil {
    public static void displayPWD() throws IOException {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:" + current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);
    }
}
