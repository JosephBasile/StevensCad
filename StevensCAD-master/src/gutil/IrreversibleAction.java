package gutil;

import java.util.*;
import java.awt.event.*;

/**
 *
 * @author dkruger Internationalized Action capable of being attached to menus
 * and buttons whose language change dynamically with the selected user language
 */
public abstract class IrreversibleAction extends Action {

    public IrreversibleAction(String name) {
        super(name);
    }

    public void undo(ActionEvent e) {}    
}
