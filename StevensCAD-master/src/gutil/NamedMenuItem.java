/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil;

import javax.swing.JMenuItem;

/**
 *
 * @author dkruger
 */
public class NamedMenuItem extends JMenuItem {
    private String name;
    public String getInternalName() { return name; }
    public NamedMenuItem(String text, String name, NamedActionListener listener) {
        super(text);
        this.name = name;
        addActionListener(listener);
    }
}
