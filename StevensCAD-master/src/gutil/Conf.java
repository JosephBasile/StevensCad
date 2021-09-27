/*
 */
package gutil;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;

/**
 * Load in configuration files with object format
 * Supports values of type int, double, boolean, arrays, etc.
 * can be used to load and configure menus and preferences among other things
 * 
 * @author dkruger
 */
public class Conf {
    private static boolean verbose;
    
    public static void setVerbose(boolean b) { verbose = b; }
    private HashMap<String,String> map;
    private HashMap<String,String> languageMap;
    private static final Pattern SPACES, BLANKLINE, COMMANDS, FIELDS;
            
    static {
        verbose = true;
        // initialize all regex patterns once for speed
        SPACES = Pattern.compile("\\s+");
        BLANKLINE = Pattern.compile("^\\s*$");
        COMMANDS = Pattern.compile("(^\\s+$|^\\s*#|include|define|menu|regex|toolbar)\\s+([a-zA-Z][a-zA-Z0-9]*)");
        FIELDS = Pattern.compile("\\s*(\\w+)\\s+(.*)");
    }

    public void load(String filename) throws IOException {
        Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
        for (int lineNum = 1; s.hasNext(); ++lineNum) {
            String line = s.nextLine();
            if (verbose) {
                System.out.println(lineNum + ": " + line);
            }
//            Matcher m = COMMANDS.matcher(line);
            Matcher m = FIELDS.matcher(line);
            if (m.find()) {
                String name = m.group(1);
                String value = m.group(2);
                //TODO: special handling for multi-line menus
                //if (name.equals("menu")) {
                    
                  //  continue;
                //}
                map.put(name, value);
                if (verbose) {
                    System.err.println("Mapped: " + name + "=" + value);
                }
            } else {
                System.err.println("Error " + filename + ":" + lineNum + " No valid command found");
            }
        }
        s.close();
    }

    //TODO: implement save!
    public void save(String filename) {
        
    }
    public HashMap<String,String> getMap() { return map; }
    public String defaulted(String name, String defaultVal) {
        System.out.println("finding " + name + " value");
        String v = map.get(name);
        return v == null ? defaultVal : v;
    }
    
    public int defaulted(String name, int defaultVal) {
        String v = map.get(name);
        if (v == null) return defaultVal;
        try {
            return Integer.parseInt(v);
        } catch(Exception e) {
            System.err.println("Expected int for " + name);
            return defaultVal;
        }
    }
    
    public double defaulted(String name, double defaultVal) {
        String v = map.get(name);
        if (v == null) return defaultVal;
        try {
            return Double.parseDouble(v);
        } catch(Exception e) {
            System.err.println("Expected double for " + name);
            return defaultVal;
        }
    }

    public Style defaulted(String name, Style defaultVal) {
        String v = map.get(name);
        if (v == null) return defaultVal;
        try {
            String[] s = v.split("\\s+");
            return new Style(s[0],
                    Integer.parseInt(s[1]),
                    Integer.parseInt(s[2]),
                    new Color(Integer.parseInt(s[3], 16)),
                    new Color(Integer.parseInt(s[4], 16))
            );
        } catch(Exception e) {
            System.err.println("Expected Style for " + name + " val = " + v);
            return defaultVal;
        }
    }
    
    public Color defaulted(String name, Color defaultVal) {
        String v = map.get(name);
        if (v == null) return defaultVal;
        try {
            return new Color(Integer.parseInt(v, 16));
        } catch(Exception e) {
            System.err.println("Expected Color for " + name);
            return defaultVal;
        }
    }

    public String [][] defaulted(String name, String[][] defaultVal) {
        String v = map.get(name);
        if (v == null) return defaultVal;
        try {
            String[] menus = v.split("\\s*\\/\\s*");
            String[][] arr = new String[menus.length][1];
            for (int i = 0; i < menus.length; i++) {
                arr[i] = menus[i].split("\\s+");
            }

            return arr;
        } catch(Exception e) {
            System.err.println("Expected double for " + name);
            return defaultVal;
        }
    }
    
    /*public HashMap<String, String> getLanguageMap() {
        return languageMap;
    }*/
    
    public HashMap<String, String> defaulted(String language, HashMap<String, String> defaultVal) throws FileNotFoundException{
        String fileLink = "conf/" + language + ".properties";
        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(fileLink)));
            s.nextLine();
            for(; s.hasNext();) {
                String line = s.nextLine();
                Matcher m = FIELDS.matcher(line);
                if(m.find()) {
                    String pro = m.group(1);
                    String con = m.group(2);
                    languageMap.put(pro, unicodeToString(con));
                    System.out.println("pro: " + pro + " con: " + unicodeToString(con));
                }
            }
            return languageMap;
        } catch(FileNotFoundException e) {
            return defaultVal;
        }
    }

    public void set(String name, int v) {
        map.put(name, v + "");
    }

    public void set(String name, double v) {
        map.put(name, v + "");
    }
    
    public void set(String name, String v) {
        map.put(name, v);
    }
    
public static String unicodeToString(String str) {
 
    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
    Matcher matcher = pattern.matcher(str);
    char ch;
    while (matcher.find()) {
        ch = (char) Integer.parseInt(matcher.group(2), 16);
        str = str.replace(matcher.group(1), ch + "");    
    }
    return str;
}

    /*public void loadLanguageMap(String language) throws FileNotFoundException {
        String fileLink = "conf/" + language + ".properties";
        System.out.println(fileLink);
        
        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(fileLink)));
            s.nextLine();
            for(; s.hasNext();) {
                String line = s.nextLine();
                Matcher m = FIELDS.matcher(line);
                if(m.find()) {
                    String pro = m.group(1);
                    String con = m.group(2);
                    languageMap.put(pro, unicodeToString(con));
                    System.out.println("pro: " + pro + " con: " + unicodeToString(con));
                }
            }
        } catch(FileNotFoundException e) {
            loadLanguageMap("en");
        }
    }*/


    public Conf(String filename) throws IOException {
        verbose = true; // turn it on for now
        map = new HashMap<>();
        languageMap = new HashMap<>();
        load(filename);
        //loadLanguageMap(map.get("language"));
    }
}
