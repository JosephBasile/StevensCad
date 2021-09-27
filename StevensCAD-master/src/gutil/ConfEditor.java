/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil;
import java.awt.Container;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import primitives.*;
import primitives.Cylinder;
import primitives.Shape3d;
import primitives.Sphere;
import scad.Polyhedron;
import viewer.CADEditor;
/**
 * Create a JPanel editing all the values in the Conf object
 * @author dkruger
 */
public class ConfEditor extends JPanel {
    private App app;
    private Conf conf;
    private HashMap<String, Class> mp;
    private JTextField[] tf;
    private String[] attributes;
    private static Font font;
    private CADEditor view;
    
    //TODO: move fonts to a central place in App so every class does not set its own fonts
    //TODO: font sizes should be taken from config or preferences
    //TODO: Conf and Prefs should be merged somehow
    static {
        font = new Font("Times", Font.BOLD, 24);
    }
    
    public ConfEditor(Conf conf) {
        this.conf = conf;
        PercentLayout p = new PercentLayout();
        setLayout(p);
        createFields();
    }
    
    public void createFields() {
        HashMap<String,String> map = conf.getMap();
        Object[] listObj = map.keySet().toArray();
        //String[] fields = (String[]) listObj;
        //fields = (String[])Arrays.sort(fields);
        Arrays.sort(listObj);
        float rowSize = 1.0f / (listObj.length + 1); // leave room for buttons at bottom
        float y = 0; // starting location on the panel
        
        for (Object obj : listObj) {
            String f = (String) obj; // TODO: Fix this ugly code, why can't we cast Object[] to String[]?
            JLabel lab = new JLabel(f);
            add(lab, new PercentInfo(0, y, +2, 0, 50, y+rowSize, -2, -5));
            JTextField tf = new JTextField();
            add(tf, new PercentInfo(50, y, +2, 0, 100,y+rowSize, -2, -5));
            y += rowSize;
        }

        //TODO: add to internationalization
        JButton save = new JButton("save");
        add(save, new PercentInfo(25, y, -50,0, 25, y, +50,0));
        JButton cancel = new JButton("cancel");
        add(cancel, new PercentInfo(25, y, -50,0, 25, y, +50,0));
    }    
}