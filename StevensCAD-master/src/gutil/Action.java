package gutil;

import java.util.*;
import java.awt.event.*;

/**
 *
 * @author dkruger Internationalized Action capable of being attached to menus
 * and buttons whose language change dynamically with the selected user language
 */
public abstract class Action {

    private static HashMap<String, Action> actions;

    static {
        actions = new HashMap<>();
    }
    private String name; // internal name used for looking up display name

    public String getName() { return name; }
    public Action(String name) {
        this.name = name;
        Action exists = actions.get(name);
        if (exists != null) {
            System.err.println("Action " + name + " is already registered.  Check for duplicates");
        }
        actions.put(name, this);
    }

    public static void setLocale(String locale) {
        
    }

    public abstract void doIt(ActionEvent e);
    public abstract void undo(ActionEvent e);
    
    public static Action lookup(String name) {
        return actions.get(name);
    }
    public static void execute(String name) {
        Action a = lookup(name);
        if (a == null)
            return;
        System.err.println("executing: " + name);
        a.doIt(null);
    }
}
