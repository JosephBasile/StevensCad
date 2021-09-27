package customTesting.paddle;

import java.util.logging.Level;
import java.util.logging.Logger;
import primitives.Shape;
/**
 *
 * @author dov
 */
public class PartMaker implements Runnable{

    public enum Part{
        LOWER,MID,UPPER,SHOULDER,LOOM, END;
    }
    private Part part;
    private Paddle p;
    private static int threads = 0;
    private String fileName;

    public PartMaker(Part part, Paddle p, String fileName) {
        this.part = part;
        this.p = p;
        this.fileName = fileName;
        threads++;
        new Thread(this).start();
    }
    
    
    
    @Override
    public void run() {
        Shape csg;
        switch (part) {
            case LOWER:
                csg = p.lowerBlade();
                p.myWrite("lb1.stl", csg);
                p.myWrite("lb2.stl", csg.mirror(false,true,true));
            case LOOM:
                csg = p.loom();
                p.myWrite("loom.stl", csg);
            case MID:
                csg = p.midBlade();
                p.myWrite("mb1.stl", csg);
                p.myWrite("mb2.stl", csg.mirror(false,true,true));
            case SHOULDER:
                csg = p.shoulder();
                p.myWrite("sh1.stl", csg);
                p.myWrite("sh2.stl", csg.mirror(false,true,true));
            case UPPER:
                csg = p.upperBlade();
                p.myWrite("ub1.stl", csg);
                p.myWrite("ub2.stl", csg.mirror(false,true,true));
            case END:
                csg = p.bladeEnd();
                p.myWrite("be1.stl", csg);
                p.myWrite("be2.stl", csg.mirror(false,true,true));
        }
        threads--;
        
        if (threads == 0) {
            try {
                new STLMerger("sh1.stl", "sh2.stl", "ub1.stl", "ub2.stl", "mb1.stl",
                        "mb2.stl", "lb1.stl", "lb2.stl", "be1.stl", "be2.stl", "loom.stl")
                        .merge(fileName + "_ID_" + p.IDSN + ".stl", p.toString());
            } catch (Exception ex) {
                Logger.getLogger(PartMaker.class.getName()).log(Level.SEVERE, null, ex);
            }

            Paddle.myWrite(fileName + "_lamination_ID_" + p.IDSN + ".stl", p.laminationBlock());
        }
    }
}

