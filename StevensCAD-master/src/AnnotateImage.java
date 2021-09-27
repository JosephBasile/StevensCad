
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 *
 * @author dkruger
 */
public class AnnotateImage {
    public static void main(String[] args) throws IOException {
        BufferedImage im = ImageIO.read(new File("earth.jpg"));
        Graphics g = im.getGraphics();
        Font f = new Font("Times", Font.BOLD, 48);
        g.setFont(f);
        g.drawString("Stevens Institute of Technology", 50, im.getHeight()/2);
        g.dispose();
        ImageIO.write(im, "png", new File("earth2.jpg"));
        
    }
    
}
