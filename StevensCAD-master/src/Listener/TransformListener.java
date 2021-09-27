package Listener;
import GUI.SelectShapeToTransform;
import GUI.TransformationAction;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import scad.Vector;
import transformation.*;
/**
 *
 * @author Tianyue Guan
 */
public class TransformListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        System.out.println(ac);
        switch(ac) {
            case "Translate":
                TranslateActionPerformed();
                break;
            case "Scale":
                ScaleActionPerformed();
                break;
            case "Rotate":
                RotateActionPerformed();
                break;
            default:
                break;
        }
    }
    
    public void TranslateActionPerformed() {
        JFrame select = new JFrame("select axis");
        select.setSize(150,40);
        Container c = select.getContentPane();
        c.setLayout(new GridBagLayout());
        JMenuBar JMB = new JMenuBar();
        select.setJMenuBar(JMB);
        JMenu a = new JMenu("Axis");
        JMB.add(a);
        JMenuItem mi1 = new JMenuItem("XTranslation");
        JMenuItem mi2 = new JMenuItem("YTranslation");
        JMenuItem mi3 = new JMenuItem("ZTranslation");
        a.add(mi1);
        a.add(mi2);
        a.add(mi3);
        select.setVisible(true); 
        mi1.addActionListener(new ActionListener1());
        mi2.addActionListener(new ActionListener2());
        mi3.addActionListener(new ActionListener3());
    }
     
    class ActionListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("XTranslation");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().translateX(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);        
        }
    }
    class ActionListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("YTranslation");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().translateY(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);                      
        }
    }
    class ActionListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("ZTranslation");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().translateZ(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);     
        }
    }
    
    public void ScaleActionPerformed() {
        JFrame select = new JFrame("select axis");
        select.setSize(150,40);
        Container c = select.getContentPane();
        c.setLayout(new GridBagLayout());
        JMenuBar JMB = new JMenuBar();
        select.setJMenuBar(JMB);
        JMenu a = new JMenu("Axis");
        JMB.add(a);
        JMenuItem mi4 = new JMenuItem("XScale");
        JMenuItem mi5 = new JMenuItem("YScale");
        JMenuItem mi6 = new JMenuItem("ZScale");
        a.add(mi4);
        a.add(mi5);
        a.add(mi6);
        select.setVisible(true); 
        mi4.addActionListener(new ActionListener4());
        mi5.addActionListener(new ActionListener5());
        mi6.addActionListener(new ActionListener6());
    }
     
    class ActionListener4 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("XScale");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().scaleX(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);        
        }
    }
    class ActionListener5 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("YScale");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().scaleY(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);                      
        }
    }
    class ActionListener6 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("ZScale");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().scaleZ(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);     
        }
    }
    
    public void RotateActionPerformed() {
        JFrame select = new JFrame("select axis");
        select.setSize(150,40);
        Container c = select.getContentPane();
        c.setLayout(new GridBagLayout());
        JMenuBar JMB = new JMenuBar();
        select.setJMenuBar(JMB);
        JMenu a = new JMenu("Axis");
        JMB.add(a);
        JMenuItem mi7 = new JMenuItem("XRotation");
        JMenuItem mi8 = new JMenuItem("YRotation");
        JMenuItem mi9 = new JMenuItem("ZRotation");
        a.add(mi7);
        a.add(mi8);
        a.add(mi9);
        select.setVisible(true); 
        mi7.addActionListener(new ActionListener7());
        mi8.addActionListener(new ActionListener8());
        mi9.addActionListener(new ActionListener9());
    }
     
    class ActionListener7 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("XRotation");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().rotX(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);        
        }
    }
    class ActionListener8 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("YRotation");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().rotY(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);                      
        }
    }
    class ActionListener9 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame f1 = new JFrame("ZRotation");
            f1.setSize(200,100);
            Container cp1 = f1.getContentPane();
            cp1.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();            
            Font attrFont = new Font("Monospaced", Font.PLAIN, 16);
            c1.gridx = 0;
            c1.gridy = GridBagConstraints.RELATIVE;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.insets = new Insets(2, 2, 2, 2);
            c1.anchor = GridBagConstraints.EAST;
            JLabel qua = new JLabel("Quantity:", SwingConstants.RIGHT);
            qua.setFont(attrFont);
            cp1.add(qua, c1);
            c1.gridx = 1;
            c1.gridy = 0;
            c1.weightx = 1.0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.anchor = GridBagConstraints.CENTER;
            c1.gridy = GridBagConstraints.RELATIVE;
            JTextField quaEnter = new JTextField(10);
            quaEnter.setFont(attrFont);
            cp1.add(quaEnter, c1);
            c1.weightx = 0.0;
            c1.fill = GridBagConstraints.BOTH;
            cp1.add(new JButton(new AbstractAction("OK"){
                public void actionPerformed(ActionEvent e){
                    double quantity = Double.parseDouble(quaEnter.getText());
                    new SelectShapeToTransform(new TransformationAction(new Transformation().rotZ(quantity)));
                    f1.dispose();
                }
            }), c1);
            f1.pack();
            f1.setVisible(true);     
        }
    }
  
}
