/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil;

import editorV2.CADEditor2;
import gutil.test.GUI;
import gutil.test.GUI2;
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
 *
 * @author yangbai
 */
public class BeanEditor extends JPanel {
    private GUI gui;
    private GUI2 gui2;
    private CADEditor view;
    private CADEditor2 view2;
    private Style labelStyle; // the style to display the text explanations
    private Style fieldStyle; // the style used for data entry of the fields

    private Object obj;
    private HashMap<String, Class> mp;
    
    private JTextField[] tf;
    private ArrayList<String> attributes;
    private JTextField[] tf_xyz;



    private void getAttributeList(Class c, String[] attr, String[] inheritedAttr) {
        if (attr.length != 0) {
            attributes = new ArrayList<>(attr.length + inheritedAttr.length);
            mp = new HashMap<>();
            for (String a : attr){
                attributes.add(a);
                
                // to fix bug
                Method[] methods = c.getMethods();
                String cp = "set" + a;
                for (Method m : methods){
                    if (m.getName().equals(cp)){
                        mp.put(cp, m.getParameterTypes()[0]);
                    }
                }
            }
            for (String a : inheritedAttr){
                attributes.add(a);
            }
            return;
        }
        mp = new HashMap<>();
        Method[] methods = c.getMethods();
        HashSet<String> getters = new HashSet<>();
        HashSet<String> setters = new HashSet<>();
//        System.out.println(int.class);
        for(int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if(name.startsWith("get")) getters.add(name.substring(3));
            else if(name.startsWith("set")) { 
                setters.add(name.substring(3));
                Class[] parameterTypes = methods[i].getParameterTypes(); 
                //System.out.println(parameterTypes[0]);
                mp.put(name, parameterTypes[0]);
            }
        }
        attributes = new ArrayList<>(getters.size());
        for(String g : getters) {
            if(setters.contains(g)) {
                attributes.add(g);
                //System.out.println("Method:" + g);
            }
        }
        Collections.sort(attributes);
    }
    
    private String getStaticField(Class c, String attribute) {
        try {
            Field f = c.getDeclaredField(attribute);
            return f.get(null).toString();
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            Class parent = c.getSuperclass();
            try {
                Field f = parent.getDeclaredField(attribute);
                return f.get(null).toString();
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e2) {
                System.out.println("Error getting value for: " + c.getName() + ", " + parent.getName() + "." + attribute);
                return null;
            }
//            System.out.println("Error getting value for: " + c.getName() + "." + attribute);
//            return null;
        }
    }
    
    private void setAttribute(String name, JTextField f) {
        String methodName = "set" + name;
        Class c = obj.getClass();
        try {
            Method m = null;
            do {
                m = c.getMethod(methodName, mp.get(methodName));
                c = c.getSuperclass();
                if (c == null) {
                    Logger.getLogger(BeanEditor.class.getName()).log(Level.WARNING, null, "Can't find method " + methodName);
                    return;
                }
            } while (m == null);
            if (m == null)
                Logger.getLogger(BeanEditor.class.getName()).log(Level.WARNING, null, "Can't find method " + methodName);
            System.out.println(methodName + "(" + f.getText() + ")");
            m.invoke(obj, String.valueOf(f.getText()));
        } catch (Exception e) {
            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private Object getAttribute(String name, JTextField f) {
        String methodName = "get" + name;
        Class c = obj.getClass();
        try {
            Method m = null;
            do {
                m = c.getMethod(methodName, null);
                c = c.getSuperclass();
                if (c == null) {
                    Logger.getLogger(BeanEditor.class.getName()).log(Level.WARNING, null, "Can't find method " + methodName);
                    return null;
                }
            } while (m == null);
            if (m == null)
                Logger.getLogger(BeanEditor.class.getName()).log(Level.WARNING, null, "Can't find method " + methodName);
            System.out.println(methodName + "(" + f.getText() + ")");
            return m.invoke(obj, null);
        } catch (Exception e) {
            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    /**
     * Build an editor to edit fields of an object of type claz.
     * If the list of methods is empty, automatically compute the list by looking at all getters/setters
     * @param view
     * @param x
     * @param y
     * @param w
     * @param h
     * @param methods 
     */
    public BeanEditor(GUI gui, CADEditor view, Class claz, String[] attr, String[] inheritedAttr) {
        this.gui = gui;
        this.view = view;
        Prefs prefs = gui.getPrefs();
        labelStyle = prefs.getLabelStyle();
        fieldStyle = prefs.getFieldStyle();
        setLayout(new PercentLayout());

        getAttributeList(claz, attr, inheritedAttr);     
        int numFields = attributes.size();
        tf = new JTextField[numFields+1]; // 1 extra for the ok button
        float fieldSize = 1.0f / (numFields + 1);
        JLabel[] labs = new JLabel[numFields+1];
        float yp1 = 0, yp2 = fieldSize;
        for(int i = 0; i < numFields; i++, yp1 += fieldSize, yp2 += fieldSize) {
            String text = gui.lookupText(attributes.get(i));
            labs[i] = new JLabel(text);
            labelStyle.set(labs[i]);
            tf[i] = new JTextField();
            String defaultValue = getStaticField(claz, "default" + attributes.get(i));
            tf[i].setText(defaultValue);
            fieldStyle.set(tf[i]);
            add(labs[i], new PercentInfo(0, yp1, 5, 2, .4, yp2, 0, 0));
            add(tf[i], new PercentInfo(.4, yp1, 5, 2, 1, yp2, -5, 0));
        }
        JButton getAttributes = new JButton("ok");
        fieldStyle.set(getAttributes);
        
        // if inheritedAttr not only contains setX setY setZ, this need be changed
        getAttributes.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < attr.length; i++) {
                System.out.println(attributes.get(i));
                String methodName = "set" + attributes.get(i);
                Class c1 = obj.getClass();
                try {
                    //Method m = c.getMethod(methodName, int.class);
                    Method m = c1.getMethod(methodName, mp.get(methodName));
                    System.out.println(methodName + "(" + tf[i].getText() + ")");
                    m.invoke(obj, tf[i].getText());
                } catch (NoSuchMethodException | SecurityException
                        | IllegalArgumentException | IllegalAccessException
                        | InvocationTargetException ex) {
                    System.out.println("Catch you little .........BUG        " + methodName + "(" + tf[i].getText() + ")");
                    //Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
            }
            
            Shape shapeObj = (Shape)obj;
            int attrLen = attr.length;
            double locationX = Double.parseDouble(tf[attrLen].getText());
            double locationY = Double.parseDouble(tf[attrLen+1].getText());
            double locationZ = Double.parseDouble(tf[attrLen+2].getText());
            shapeObj.translate(locationX, locationY, locationZ);
            
            setVisible(false);
            view.replaceShape(shapeObj);            
        });
        add(getAttributes, new PercentInfo(.1f, yp1, 5, 2, .9f, yp2, -5, 0));
    }
    
    public BeanEditor(GUI2 gui, CADEditor2 view, Class claz, String[] attr, String[] inheritedAttr) {
        this.gui2 = gui;
        this.view2 = view;
        Prefs prefs = gui.getPrefs();
        labelStyle = prefs.getLabelStyle();
        fieldStyle = prefs.getFieldStyle();
        setLayout(new PercentLayout());

        getAttributeList(claz, attr, inheritedAttr);     
        int numFields = attributes.size();
        tf = new JTextField[numFields+1]; // 1 extra for the ok button
        float fieldSize = 1.0f / (numFields + 1);
        JLabel[] labs = new JLabel[numFields+1];
        float yp1 = 0, yp2 = fieldSize;
        for(int i = 0; i < numFields; i++, yp1 += fieldSize, yp2 += fieldSize) {
            String text = gui.lookupText(attributes.get(i));
            labs[i] = new JLabel(text);
            labelStyle.set(labs[i]);
            tf[i] = new JTextField();
            String defaultValue = getStaticField(claz, "default" + attributes.get(i));
            tf[i].setText(defaultValue);
            fieldStyle.set(tf[i]);
            add(labs[i], new PercentInfo(0, yp1, 5, 2, .4, yp2, 0, 0));
            add(tf[i], new PercentInfo(.4, yp1, 5, 2, 1, yp2, -5, 0));
        }
        JButton getAttributes = new JButton("Ok");
        fieldStyle.set(getAttributes);
        
        // if inheritedAttr not only contains setX setY setZ, this need be changed
        getAttributes.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < attr.length; i++) {
                System.out.println(attributes.get(i));
                String methodName = "set" + attributes.get(i);
                Class c1 = obj.getClass();
                try {
                    //Method m = c.getMethod(methodName, int.class);
                    Method m = c1.getMethod(methodName, mp.get(methodName));
                    System.out.println(methodName + "(" + tf[i].getText() + ")");
                    m.invoke(obj, tf[i].getText());
                } catch (NoSuchMethodException | SecurityException
                        | IllegalArgumentException | IllegalAccessException
                        | InvocationTargetException ex) {
                    System.out.println("Catch you little .........BUG        " + methodName + "(" + tf[i].getText() + ")");
                    //Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
            }
            
            Shape shapeObj = (Shape)obj;
            int attrLen = attr.length;
            double locationX = Double.parseDouble(tf[attrLen].getText());
            double locationY = Double.parseDouble(tf[attrLen+1].getText());
            double locationZ = Double.parseDouble(tf[attrLen+2].getText());
            shapeObj.translate(locationX+ shapeObj.t.getMatrix()[3], locationY+ shapeObj.t.getMatrix()[5], locationZ+ shapeObj.t.getMatrix()[7]);
            
            setVisible(false);
            view2.replaceShape(shapeObj);            
        });
        add(getAttributes, new PercentInfo(.1f, yp1, 5, 2, .9f, yp2, -5, 0));
    }
    
    public void edit(Object obj) {
        this.obj = obj;
        setVisible(true);
    }
    
    
}