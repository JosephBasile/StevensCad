/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customShape;

import GUI.GetNamedShape3ds;
import GUI.NameShape3d;
import GUI.gui;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import primitives.Shape;
import scad.Polyhedron;
import scad.STL;
import scad.Triangle;
import scad.Vector;
import viewer.CADEditor;
import scad.Facet;
/**
 *
 * @author Long Huang
 */
public class CustomShape extends JFrame{
    private CADEditor edit;
    private String selectedShape;
    private File shapeDirectory;
    private JList<String> shapeList;
    private String savedName;
    private JFrame saveWindow;
    JTextField nametf;
    
    public CustomShape(CADEditor edit){
        super("Insert Custom Shape");
        this.edit = edit;
        setSize(300,200);
        Container c = getContentPane();
        
        JPanel namePanel = new JPanel();
        namePanel.setPreferredSize(new Dimension(300,35));
        JPanel shapePanel = new JPanel();
        shapePanel.setPreferredSize(new Dimension(300,135));
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(300,30));
        controlPanel.setLayout(new GridLayout(1,3,2,2));
        c.add(BorderLayout.NORTH,namePanel);
        c.add(BorderLayout.CENTER,shapePanel);
        c.add(BorderLayout.SOUTH,controlPanel);
        
        Font boldFont = new Font("Monospaced", Font.BOLD, 16);
        JLabel namelb = new JLabel("Name: ");
        nametf = new JTextField(10);
        namelb.setFont(boldFont);
        nametf.setFont(boldFont);
        namePanel.add(namelb);
        namePanel.add(nametf);
        
        shapeDirectory = new File("CustomShape");
        if(!shapeDirectory.exists()){
            shapeDirectory.mkdir();
        }
        
        shapeList = new JList<String>();
        shapeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shapeList.setListData(shapeDirectory.list());
        shapeList.addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            selectedShape = shapeList.getSelectedValue();
        }
        });
        shapeList.setSelectedIndex(0);
        JScrollPane jsp=new JScrollPane(shapeList);
        jsp.setPreferredSize(new Dimension(285, 125));
        shapePanel.add(jsp);
        
        controlPanel.add(new JButton(new AbstractAction("Insert"){
            public void actionPerformed(ActionEvent e){
                if(selectedShape != null){
                    if(gui.shapesByName.containsKey(nametf.getText())){
                        insertConflict();
                    }
                    else{
                        try{
                            insertCustomShape();
                        }catch(IOException e1){
                        }
                    }
                }
            }
        }));
        
        controlPanel.add(new JButton(new AbstractAction("Delete"){
            public void actionPerformed(ActionEvent e){
                if(selectedShape != null){
                    deleteCustomShape();
                }
            }
        }));
        
        controlPanel.add(new JButton(new AbstractAction("Save"){
            public void actionPerformed(ActionEvent e){
                saveConfirm();
            }
        }));
        
        this.pack();
        setVisible(true);
    }
    
    public void saveConfirm(){
        saveWindow = new JFrame("Save as a new custom shape?");
        saveWindow.setSize(300,80);
        Container c= saveWindow.getContentPane();
        c.setLayout(new GridLayout(2,2,2,2));
        JLabel l = new JLabel("Please insert name");
        JTextField tf = new JTextField();
        JButton b1 = new JButton(new AbstractAction("Save"){
            public void actionPerformed(ActionEvent e){
                savedName = tf.getText();
                if(!savedName.isEmpty()){
                    int sign = 0;
                    for(String s:shapeDirectory.list()){
                        if(s.equals(savedName)){
                            sign = 1;
                        }
                    }
                    if(sign == 0){
                        saveCustomShape(savedName);
                        shapeList.setListData(shapeDirectory.list());
                        saveWindow.dispose();
                    }
                    else{
                        saveConflict();
                    }
                }
            }
        });
        JButton b2 = new JButton(new AbstractAction("Cancel"){
            public void actionPerformed(ActionEvent e){
                saveWindow.dispose();
            }
        });
        
        c.add(l);
        c.add(tf);
        c.add(b1);
        c.add(b2);
        saveWindow.setVisible(true);
    }

    public void saveCustomShape(String name){
        ArrayList<Shape> polyhedrons = edit.getShapes();
        ArrayList<Facet> facets = new ArrayList<>();
        for(Shape p: polyhedrons){
            for(Triangle t: p.triangles()){
                facets.add(t);
            }
        }
        Polyhedron customPolyhedron = new Polyhedron();
        customPolyhedron.facets = new ArrayList<>();
        customPolyhedron.addAll(facets);
        STL.writeSTL(customPolyhedron, "CustomShape/"+name);
    }
    
    public void saveConflict(){
        JFrame f = new JFrame("File already exists");
        f.setSize(300,50);
        Container c = f.getContentPane();
        c.setLayout(new GridLayout(1,1,2,2));
        c.add(new JButton(new AbstractAction("Replace"){
            public void actionPerformed(ActionEvent e){
                saveCustomShape(savedName);
                shapeList.setListData(shapeDirectory.list());
                f.dispose();
                saveWindow.dispose();
            }
        }));
        c.add(new JButton(new AbstractAction("Cancel"){
            public void actionPerformed(ActionEvent e){
                f.dispose();
            }
        }));
        f.setVisible(true);
    }
    
    public void insertCustomShape() throws IOException{
        String pname = nametf.getText();
        Polyhedron p = new Polyhedron();
        p.facets = new ArrayList<>();
        
        File file = new File("CustomShape/"+selectedShape);
        
        BufferedReader in = new BufferedReader(new FileReader(file));
        ArrayList<Vector> vertices = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            String[] num = line.trim().split("\\s+");
            if (num[0].equals("vertex")) {
                Vector v = new Vector(Float.parseFloat(num[1].split(",")[0]),
                                      Float.parseFloat(num[2].split(",")[0]),
                                      Float.parseFloat(num[3]));
                vertices.add(v);
            }
        }
        in.close();
        
        for(int i=0;i<vertices.size()/3;i++){
            Triangle t = new Triangle(vertices.get(i*3),vertices.get(i*3+1),vertices.get(i*3+2));
            p.add(t);
        }
        
        edit.add(p);
        NameShape3d np = new NameShape3d(p, pname);
        gui.shapesByName.put(pname, np);
        GetNamedShape3ds.addNamedShape3ds(np);
        dispose();
    }
    
    public void insertConflict(){
        JFrame f = new JFrame("Name already exists");
        f.setSize(300,50);
        Container c = f.getContentPane();
        c.setLayout(new GridLayout(1,1,2,2));
        c.add(new JButton(new AbstractAction("Replace"){
            public void actionPerformed(ActionEvent e){
                try{
                    insertCustomShape();
                }catch(IOException e1){
                }
                f.dispose();
            }
        }));
        c.add(new JButton(new AbstractAction("Cancel"){
            public void actionPerformed(ActionEvent e){
                f.dispose();
            }
        }));
        f.setVisible(true);
    }
    
    public void deleteCustomShape(){
        JFrame f = new JFrame("Delete "+selectedShape+"?");
        f.setSize(300,50);
        Container c = f.getContentPane();
        c.setLayout(new GridLayout(1,1,2,2));
        c.add(new JButton(new AbstractAction("Delete"){
            public void actionPerformed(ActionEvent e){
                File file = new File("CustomShape/"+selectedShape);
                file.delete();
                shapeList.setListData(shapeDirectory.list());
                f.dispose();
            }
        }));
        c.add(new JButton(new AbstractAction("Cancel"){
            public void actionPerformed(ActionEvent e){
                f.dispose();
            }
        }));
        f.setVisible(true);
    }
    
}
