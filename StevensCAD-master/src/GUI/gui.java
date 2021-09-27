package GUI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Listener.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import static java.util.Collections.list;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.text.*;
import viewer.*;
import primitives.*;
import scad.*;
import test.Test;
import static test.Test.*;
import transformation.Transformation;
import transformation.*;
import scad.Vector;
/**
 *
 * @author yangbaif
 */
public class gui extends JFrame {
    private Container c;
    private CADEditor view;
    public static HashMap<String, NameShape3d> shapesByName;
    private String[] menuItem = {"File", "Edit", "Insert", "Transform", "Binary Operation"};
    private String[] fileItem = {"File", "Load", "Export", "Import"};
    private String[] editItem = {"Edit_shape","Delete","Empty"};
    
    private String[] insertItem = {"Sphere", "Cylinder", "Cube", "Polyhedron", "Custom Shape"};
    private String[] transformItem = {"Translate", "Scale", "Rotate"};
    private String[] binaryOperationItem = {"Union", "Intersection", "Difference", "ConvexHull", "Remove Overlaps"};
    
    public gui() {
        shapesByName = new HashMap<>();
        setSize(600, 400);
        c = getContentPane();
        JMenuBar JMB = new JMenuBar();
        setJMenuBar(JMB);
        JMenu[] jmenu = new JMenu[menuItem.length];
        for(int i = 0; i < menuItem.length; i++) {
            jmenu[i] = new JMenu(menuItem[i]);
            JMB.add(jmenu[i]);
            //jmenu[i].addActionListener(menuListener);
        }
       
        JMenuItem[] fileMenu = new JMenuItem[fileItem.length];
        for(int i = 0; i < fileItem.length; i++) {
            fileMenu[i] = new JMenuItem(fileItem[i]);
            jmenu[0].add(fileMenu[i]);
            fileMenu[i].addActionListener(new fileListener());
        }
        
        JMenuItem[] editMenu = new JMenuItem[editItem.length];
        for(int i = 0; i < editItem.length; i++) {
            editMenu[i] = new JMenuItem(editItem[i]);
            jmenu[1].add(editMenu[i]);
            //editMenu[i].addActionListener(new EditListener());
        }
       
        JMenuItem[] insertMenu = new JMenuItem[insertItem.length];
        for(int i = 0; i < insertItem.length; i++) {
            insertMenu[i] = new JMenuItem(insertItem[i]);
            jmenu[2].add(insertMenu[i]);
            //insertMenu[i].addActionListener(new InsertListener());
        }
        
        
        JMenuItem[] transformMenu = new JMenuItem[transformItem.length];
        for(int i = 0; i < transformItem.length; i++) {
            transformMenu[i] = new JMenuItem(transformItem[i]);
            jmenu[3].add(transformMenu[i]);
            transformMenu[i].addActionListener(new TransformListener());
        }
        
        JMenuItem[] binaryOperationMenu = new JMenuItem[binaryOperationItem.length];
        for(int i = 0; i < binaryOperationItem.length; i++) {
            binaryOperationMenu[i] = new JMenuItem(binaryOperationItem[i]);
            jmenu[4].add(binaryOperationMenu[i]);
            //binaryOperationMenu[i].addActionListener(new BinaryOperationListener());
        }
       
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        view = CADEditor.openWindow();
        setTitle("GUI");
        setVisible(true);
    }
    
    public static void main(String[] arg) {
        new gui();
    }   
}