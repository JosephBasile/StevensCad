package editorV2;

import gutil.Action;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.*;

/**
 *
 * @author dkruger
 */
public class InputEventMap {
    private int[] inputEvents;
    private Action[] actions;
    private static Pattern inputEventFormat = Pattern.compile("^(C?)(A?)(S?)((?:M1)?)((?:M2)?)((?:M3)?)-?(\\w+)\\s+(\\w+)");
    public static final int MOD = 11;
    public static final int BUTTONSTATE = 8;
    public InputEventMap() {
        actions = new Action[4096];
    }
    
    private static void buildKeymapStrings() {
        Class c = org.lwjgl.glfw.GLFW.class;
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            String name = f.getName();
            if (!name.startsWith("GLFW_KEY"))
                continue;
            name = name.substring(8);
            System.out.println(name + f.getType());
        }
    }
    static {
        buildKeymapStrings();
    }
    public InputEventMap(String filename) throws IOException {
        this();
        Scanner s = new Scanner(new FileReader(filename));
        HashMap<String,Integer> keyCode = new HashMap<>(256);
        //TODO: See if GL already has string names for keys that we can use at runtime.  This is a pain.
        for (char i = 'A'; i <= 'Z'; i++)
            keyCode.put("" + i, 0);
        for (char i = 'a'; i <= 'z'; i++)
            keyCode.put("" + i, 0);

        while (s.hasNext()) {
            String line = s.nextLine();
            Matcher m = inputEventFormat.matcher(filename);
            if (m.find()) {
                int control = m.group(1).equals("C") ? 1 : 0;
                int alt = m.group(2).equals("A") ? 2 : 0;
                int shift = m.group(3).equals("S") ? 4 : 0;
                int button1 = m.group(4).equals("M1") ? 1 : 0;
                int button2 = m.group(5).equals("M2") ? 2 : 0;
                int button3 = m.group(6).equals("M3") ? 3 : 0;
                String keyName = m.group(7);
                String actionName = m.group(8);
                Action a = Action.lookup(actionName);
                if (a == null)
                    continue; // this action does nothing, skip and let the lookup method report the error
                
                if (keyName.equals("")) {
                    int eventCode = ((control|alt|shift) << MOD) | (button1 + button2 + button3);
                    add(eventCode, a);
                } else {
                    int eventCode = ((control|alt|shift) << MOD) | 
                            ((button1 + button2 + button3) << BUTTONSTATE) | 
                            keyCode.get(keyName);
                }
            }
            
        }
        
        
    }
    public void add(int eventCode, Action a) {
        actions[eventCode] = a;
    }
    public final void exec(int eventCode) {
        ActionEvent e = new ActionEvent(this, 1024, actions[eventCode].getName());
        actions[eventCode].doIt(e);
    }
}
