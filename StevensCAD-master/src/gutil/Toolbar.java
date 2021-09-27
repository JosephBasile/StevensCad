package gutil;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
/**
 *
 * @author dov
 */
public class Toolbar extends JPanel {
    public Toolbar(App app, String[][] names, int size, NamedActionListener listener) throws Exception {
        init(app, names, size, listener);
    }
    public void init(App app, String[][] names, int size, NamedActionListener listener) throws Exception {
        int rows = names.length, cols = 2; //names[0].length;
        setLayout(new GridLayout(rows,cols));
        final String dir = "img/" + size + "/";
        final String suffix = ".png";
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (names[i].length <= j) {
                    add(new JLabel(""));
                    continue;
                }
                String text = app.lookupText(names[i][j]);
                ImageIcon img = new ImageIcon(dir + names[i][j] + suffix);
                JButton b = img == null ?
                    new NamedButton(text, names[i][j], listener) :
                    new NamedButton(img, names[i][j], listener);
                b.setToolTipText(text);
                add(b);
            }
        }
    }    
}
