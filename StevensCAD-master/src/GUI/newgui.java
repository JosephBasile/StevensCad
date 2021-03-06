package GUI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shaynamccarthy
 */
public class newgui extends javax.swing.JFrame {

    /**
     * Creates new form newgui
     */
    public newgui() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        open = new javax.swing.JMenuItem();
        save = new javax.swing.JMenuItem();
        saveAs = new javax.swing.JMenuItem();
        exportSH = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        load = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        copy = new javax.swing.JMenuItem();
        manipulate = new javax.swing.JMenuItem();
        cut = new javax.swing.JMenuItem();
        paste = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        existing = new javax.swing.JMenuItem();
        shapes = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        select = new javax.swing.JMenuItem();
        sphere = new javax.swing.JMenu();
        cube = new javax.swing.JMenuItem();
        polyhedron = new javax.swing.JMenuItem();
        cylinder = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        transformMenu = new javax.swing.JMenu();
        scale = new javax.swing.JMenuItem();
        rotate = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        union = new javax.swing.JMenu();
        intersection = new javax.swing.JMenuItem();
        difference = new javax.swing.JMenuItem();
        convexHull = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        open.setText("New");
        jMenu1.add(open);

        save.setText("Open");
        jMenu1.add(save);

        saveAs.setText("Save");
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsActionPerformed(evt);
            }
        });
        jMenu1.add(saveAs);

        exportSH.setText("Save as...");
        jMenu1.add(exportSH);

        jMenuItem3.setText("Load");
        jMenu1.add(jMenuItem3);

        load.setText("Export sH");
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        jMenu1.add(load);

        jMenuItem5.setText("Import - 3ds");
        jMenu1.add(jMenuItem5);

        jMenuItem24.setText("Exit");
        jMenu1.add(jMenuItem24);

        jMenuBar1.add(jMenu1);

        edit.setText("Edit");

        copy.setText("Copy");
        copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyActionPerformed(evt);
            }
        });
        edit.add(copy);

        manipulate.setText("Cut");
        edit.add(manipulate);

        cut.setText("Tools to");
        edit.add(cut);

        paste.setText("Paste");
        edit.add(paste);

        delete.setText("Delete");
        edit.add(delete);

        existing.setText("Manipulate");
        existing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                existingActionPerformed(evt);
            }
        });
        edit.add(existing);

        shapes.setText("Existing");
        edit.add(shapes);

        jMenuItem9.setText("Shapes");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        edit.add(jMenuItem9);

        select.setText("select");
        edit.add(select);

        jMenuBar1.add(edit);

        sphere.setText("Insert");

        cube.setText("Sphere");
        cube.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cubeActionPerformed(evt);
            }
        });
        sphere.add(cube);

        polyhedron.setText("Cylinder");
        sphere.add(polyhedron);

        cylinder.setText("Cube");
        sphere.add(cylinder);

        jMenuItem13.setText("Polyhedron");
        sphere.add(jMenuItem13);

        jMenuBar1.add(sphere);

        transformMenu.setText("Transform");

        scale.setText("Translate");
        scale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleActionPerformed(evt);
            }
        });
        transformMenu.add(scale);

        rotate.setText("Scale");
        transformMenu.add(rotate);

        jMenuItem16.setText("Rotate");
        transformMenu.add(jMenuItem16);

        jMenuBar1.add(transformMenu);

        union.setText("Binary Option");

        intersection.setText("Union");
        union.add(intersection);

        difference.setText("Intersection");
        union.add(difference);

        convexHull.setText("Difference");
        union.add(convexHull);

        jMenuItem20.setText("Convex Hull");
        union.add(jMenuItem20);

        jMenuBar1.add(union);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveAsActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loadActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_copyActionPerformed

    private void existingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_existingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_existingActionPerformed

    private void scaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scaleActionPerformed

    private void cubeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cubeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cubeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(newgui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(newgui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(newgui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(newgui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new newgui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem convexHull;
    private javax.swing.JMenuItem copy;
    private javax.swing.JMenuItem cube;
    private javax.swing.JMenuItem cut;
    private javax.swing.JMenuItem cylinder;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenuItem difference;
    private javax.swing.JMenu edit;
    private javax.swing.JMenuItem existing;
    private javax.swing.JMenuItem exportSH;
    private javax.swing.JMenuItem intersection;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem load;
    private javax.swing.JMenuItem manipulate;
    private javax.swing.JMenuItem open;
    private javax.swing.JMenuItem paste;
    private javax.swing.JMenuItem polyhedron;
    private javax.swing.JMenuItem rotate;
    private javax.swing.JMenuItem save;
    private javax.swing.JMenuItem saveAs;
    private javax.swing.JMenuItem scale;
    private javax.swing.JMenuItem select;
    private javax.swing.JMenuItem shapes;
    private javax.swing.JMenu sphere;
    private javax.swing.JMenu transformMenu;
    private javax.swing.JMenu union;
    // End of variables declaration//GEN-END:variables
}
