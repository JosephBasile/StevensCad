package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import primitives.Shape3d;
import transformation.Transformation;
import viewer.CADEditor;

/**
 *
 * @author dov
 */
public class TransformationAction implements ActionListener{

    private Shape3d p;
    private final Transformation trans;

    public void setP(Shape3d p) {
        this.p = p;
    }

    public TransformationAction(Transformation trans) {
        this.trans = trans;
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //p.transform(trans);
        //changed = true;
    }
}
