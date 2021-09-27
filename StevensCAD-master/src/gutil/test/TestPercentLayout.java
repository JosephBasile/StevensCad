package gutil.test;
import gutil.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author dkruger
 */
public class TestPercentLayout {
    public static void main(String[] args) {
        JFrame f = new JFrame("Test Percent Layout");
        Container c = f.getContentPane();
        c.setLayout(new PercentLayout());
        c.add(new JButton("fixed 100x40"), new PercentInfo(.5f,.5f, -50,-20, .5f,.5f, +50,+20));
        c.add(new JButton("var 10% -2pix"), new PercentInfo(.9f,.9f, 0,0, 1,1, -2,-2));
        f.setSize(800,500);
        f.setVisible(true);
    }
}
