package customTesting.paddle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *
 * @author dov
 */
public class STLMerger {
    final private ArrayList<File> files;
    private BufferedReader fr;
    private BufferedWriter fw;

    public STLMerger(String ... files) {
        
        this.files = new ArrayList<>(files.length);
        for(String s:files)
            this.files.add(new File(s));
    }
    
    public void merge(String fileName, String readMe)throws Exception{
        
        fw = new BufferedWriter(new FileWriter(new File(fileName)));
        copyFile(files.get(0), false, true);
        
        for(int i = 1; i < files.size() - 1; i++)
            copyFile(files.get(i), true, true);
        
        copyFile(files.get(files.size() - 1), true, false);
        
        fw.close();
        fw = new BufferedWriter(new FileWriter(new File(fileName.replace(".stl", " ")+ "ReadMe.txt")));
        fw.write(readMe);
        fw.close();
        
    }
    
    private void copyFile(File source, boolean skipFirstLine, boolean skipLastLine) throws Exception{
        
        FileReader ifr = new FileReader(source);
        fr = new BufferedReader(ifr);
        
        if(skipFirstLine)fr.readLine();
                
        while(fr.ready()){
            String line = fr.readLine();
            if(skipLastLine && !fr.ready()) break;
            fw.write(line + "\n");
        }
        Files.delete(source.toPath());
        fr.close();
    }
    
}
/*RandomAccessFile f = new RandomAccessFile(fileName, "rw");
long length = f.length() - 1;
do {                     
  length -= 1;
  f.seek(length);
  byte b = f.readByte();
} while(b != 10);
f.setLength(length+1);
f.close();*/