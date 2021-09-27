package editorV2;

import gutil.*;
import java.util.*;
import java.awt.event.*;

/**
 *
 * @author dkruger Internationalized Action capable of being attached to menus
 * and buttons whose language change dynamically with the selected user language
 */
public abstract class DisplayChangingAction extends Action {
    private Window w;
    public DisplayChangingAction(String name, Window w) {
        super(name);
        this.w = w;
    }
    public abstract void doIt();
    @Override
    public void doIt(ActionEvent e) {
        doIt();
        w.setChanged(true);
    }
    public void undo(ActionEvent e) {}
}
