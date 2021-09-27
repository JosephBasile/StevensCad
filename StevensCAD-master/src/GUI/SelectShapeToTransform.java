/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import primitives.Shape3d;

/**
 *
 * @author dov
 */
public class SelectShapeToTransform extends JFrame{

    public SelectShapeToTransform(TransformationAction ta) throws HeadlessException {
        super("Select Polyhedrons");
        setSize(300, 500);
        ArrayList<NameShape3d> shape3dList = GetNamedShape3ds.getNamedShape3ds();
        //System.out.println(polyhedronList.get(0).getName());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(NameShape3d n : shape3dList) {
            listModel.addElement(n.getName());
        }
        JList<String> list = new JList(listModel);        
        
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.setP(shape3dList.get(list.getSelectedIndex()).getShape3d());
                ta.actionPerformed(e);
            }
        });
        add(list, BorderLayout.NORTH);
        add(selectButton, BorderLayout.SOUTH);
        setVisible(true);
    }
    
    
    
}
