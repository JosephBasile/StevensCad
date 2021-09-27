package gutil;

import java.util.*;
import java.awt.event.*;

/**
 *
 * @author dkruger Internationalized Action capable of being attached to menus
 * and buttons whose language change dynamically with the selected user language
 */
public abstract class EditedAction extends Action {

    protected BeanEditor editor;
    public EditedAction(String name, BeanEditor editor) {
        super(name);
        this.editor = editor;
    }
    public void undo(ActionEvent e) {}
    
}
