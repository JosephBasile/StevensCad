package gutil;

import java.util.ArrayList;

public class CmdHistory {
    private ArrayList<Action> hist;
    private int current;
    
    public CmdHistory() {
        hist = new ArrayList<>();
        current = 0;
    }
    
    public void doIt(Action a) {
	hist.add(a);
        a.doIt(null);
    }
  
    //TODO: undo does not work
    public void undo() {
        final int last = hist.size()-1;
//        Action a = hist.remove(last);
//        a.undo();
    }
    //TODO: not implemented.  The cleanest way would be to write our own list class
    //TODO: There is a question of what redo means after undo() undo() and something new.
    public void redo() {}
}
