/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gutil.test;

import gutil.PercentInfo;
import gutil.PercentLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import primitives.Shape3d;
import viewer.CADEditor;

/**
 *
 * @author hemor
 */
public class TestBeanEditorWindow extends JPanel{
    
    private Object obj;
    private HashMap<String, Class> mp;
    private JTextField[] tf;
    
    public TestBeanEditorWindow(Object originObj){
        super();
        this.obj = originObj;
        
        PercentLayout p = new PercentLayout();
        this.setLayout(p);
        String[] attributes = getAttributeList(this.obj);
        int countAttributes = 0;
        for (; countAttributes < attributes.length; countAttributes++) {
            if (attributes[countAttributes] == null) {
                break;
            }
        }
        Font font = new Font("Times", Font.BOLD, 28);
        
        tf = new JTextField[countAttributes + 1];
        JLabel[] labs = new JLabel[countAttributes + 1];
        for(int i = 0; i < countAttributes; i++) {
            float yp1 = i / (float) (countAttributes + 1);
            float yp2 = (i + 1) / (float)(countAttributes + 1);
            labs[i] = new JLabel(attributes[i]);
            labs[i].setFont(font);
            tf[i] = new JTextField();
            String defaultValue = getStaticField("default" + attributes[i]);
            tf[i].setText(defaultValue);
            
            
            tf[i].setFont(font);
            this.add(labs[i], new PercentInfo(.1, yp1, 20, 20, .5, yp2, 20, 20));
            this.add(tf[i], new PercentInfo(.6, yp1, 20, 20, .9, yp2, 20, 20));
        }
        JButton getAttributes = new JButton("ok");
        getAttributes.addActionListener((ActionEvent e) -> {
            String[] attributes1 = getAttributeList(this.obj);
            for (int i = 0; i < attributes1.length; i++) {
                String methodName = "set" + attributes1[i];
                Class c1 = this.obj.getClass();
                try {
                    //Method m = c.getMethod(methodName, int.class);
                    Method m = c1.getMethod(methodName, mp.get(methodName));
                    //                        if(m == null) return;
                    System.out.println(methodName);
                    System.out.println(tf[i].getText());
                    //                        CADViewer.removeShape((Shape) obj);
                    m.invoke(this.obj, Integer.valueOf(tf[i].getText()));
//                        CADViewer.addShape((Shape) obj);
                }catch (NoSuchMethodException | SecurityException
                        | IllegalArgumentException | IllegalAccessException
                        | InvocationTargetException ex) {
                    //Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("error1");
                    System.out.println(ex.getMessage());
                }
            }
//            CADViewer.view.remove((Shape3d) originObj);
//            CADViewer.view.add((Shape3d) this.obj);            
        });
        this.add(getAttributes, new PercentInfo(.1f, .8f, 20, 20, .9f, .9f, 20, 20));
    }
    
    private String getStaticField(String attribute) {
        Class c = obj.getClass();
        try {
            Field f = c.getDeclaredField(attribute);
            return f.get(null).toString();
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            System.out.println("Error getting value for: " + attribute);
            return null;
        }
    }
    
        
    private String[] getAttributeList(Object obj) {
        mp = new HashMap<>();
        Class c = obj.getClass();
        Method[] methods = c.getMethods();
        HashSet<String> getters = new HashSet<>();
        HashSet<String> setters = new HashSet<>();
        System.out.println(int.class);
        for(int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if(name.startsWith("get")) getters.add(name.substring(3));
            else if(name.startsWith("set")) { 
                setters.add(name.substring(3));
                Class[] parameterTypes = methods[i].getParameterTypes(); 
                System.out.println(parameterTypes[0]);
                mp.put(name, parameterTypes[0]);
            }
        }
        int countAttributes = 0;
        String[] attributes = new String[getters.size()];
        for(String g : getters) {
            if(setters.contains(g)) {
                attributes[countAttributes++] = g;
                //System.out.println("Method:" + g);
            }
        }
        return attributes;
    }
    
 
}
