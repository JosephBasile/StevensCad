/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil;

import java.awt.event.*;

/**
 *
 * @author yangbai
 */
public class NamedActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NamedMenuItem m = (NamedMenuItem)e.getSource();
        Action.execute(m.getInternalName());
    }
    
}
