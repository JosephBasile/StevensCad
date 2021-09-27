/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author dkruger
 */
public class NamedButton extends JButton {
    private String name;
    public String getInternalName() { return name; }
    public NamedButton(String text, String name, NamedActionListener listener) {
        super(text);
        this.name = name;
        addActionListener(listener);
    }
    public NamedButton(ImageIcon img, String name, NamedActionListener listener) {
        super(img);
        this.name = name;
        addActionListener(listener);
    }
}
