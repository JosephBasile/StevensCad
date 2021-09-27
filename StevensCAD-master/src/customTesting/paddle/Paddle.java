package customTesting.paddle;

import gutil.FileUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import primitives.Cube;
import primitives.Cylinder;
import primitives.Shape;
import primitives.Sphere;
import scad.Operations;
import scad.Vector;

/**
 *
 * @author dov
 */
public class Paddle {

    public final int resolution;
    public final static double FLAT = .0001, inch = 25.4;
    public final double width, length, loomLength, loomHeight,
            loomWidth, tipHeight, tipRounded, concavity, lengthMaxWidth,
            shoulderLength, edgeStart, wingOffCenterTip;
    public final boolean hasShoulder, winged;
    public int IDSN;
    Operations o;

    @Override
    public String toString() {
        return "The acompanying paddle stl file is the property of Dov Neimand.\n"
                + " Keep this readMe together with the paddle stl, "
                + " and the ID number with the paddle until time of sale."
                + " All rights reserved."
                + "\n\nSTL scale 1 = 1mm"
                + "\n\nIdentification Serial Number = " + IDSN
                + "\nmax blade width = " + width / inch + " inch"
                + "\nlength = " + length / inch + " inch"
                + "\nloom length = " + loomLength / inch + " inch"
                + "\nloom height = " + loomHeight / inch + " inch"
                + "\nloom width = " + loomWidth / inch + " inch"
                + "\ntip height = " + tipHeight / inch + " inch"
                + "\ntip rounded = " + tipRounded / inch + " inch"
                + "\nconcavity = " + concavity
                + "\nlength max width = " + lengthMaxWidth / inch + " inch"
                + "\nshoulder length = " + shoulderLength / inch + " inch"
                + "\nhas shoulder = " + hasShoulder
                + "\nwinged = " + winged
                + (winged?"\nwingOffCenterTip = " + wingOffCenterTip:"") 
                + "\nloom = " + loom.toString()
                + "\nedge start = " + edgeStart / inch + " inch"
                + "\nfacet ring = " + resolution;
    }

    public enum Loom {
        NO_FERRULE, DUCK_FERRULE, MY_FERRULE
    }
    final Loom loom;

    public Paddle(int facet, double maxBladeWidth, double length,
            double loomLength, double loomHeight, double loomWidth,
            double tipHeight, double tipRounded, double concavity,
            double lengthMaxWidth, double shoulderLength, boolean hasShoulder,
            boolean flatSide, double wingOffCenterTip, 
            double edgeStart, Loom loom) throws Exception {
        this.resolution = facet;
        this.width = maxBladeWidth;
        this.length = length;
        this.loomLength = loomLength;
        this.loomHeight = loomHeight;
        this.loomWidth = loomWidth;
        this.tipHeight = tipHeight;
        this.tipRounded = tipRounded;
        this.concavity = concavity;
        this.lengthMaxWidth = lengthMaxWidth;
        this.shoulderLength = shoulderLength;
        this.hasShoulder = hasShoulder;
        this.winged = flatSide;
        this.wingOffCenterTip = wingOffCenterTip;
        this.loom = loom;
        this.edgeStart = edgeStart;
      //  setIDSN();
    }
    
    public Paddle(int facet, double width, double length,
            double loomLength, double tipRounded, double lengthMaxWidth) 
            throws Exception {
        this.resolution = facet;
        this.width = width;
        this.length = length;
        this.loomLength = loomLength;
        this.loomHeight = 1.5*inch;
        this.loomWidth = inch;
        this.tipHeight = inch/8;
        this.tipRounded = tipRounded;
        this.concavity = .3;
        this.lengthMaxWidth = lengthMaxWidth;
        this.shoulderLength = 3 * width / 16;;
        this.hasShoulder = true;
        this.winged = false;
        this.loom = Loom.NO_FERRULE;
        this.edgeStart = bldl()/2;
        this.wingOffCenterTip = .4;
      //  setIDSN();
    }

    private void setIDSN() throws Exception {
        File idf = new File("ID/IDCount.txt");
        BufferedReader br = new BufferedReader(new FileReader(idf));
        IDSN = Integer.parseInt(br.readLine());
        if (resolution > 200) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(idf));
            bw.write("" + (IDSN + 1));
            bw.close();
        }
        br.close();
    }

    public Paddle(Operations o) throws Exception {
        this.o = o;
        this.resolution = 100;

        this.width = 3.5 * inch;//minimum 3 inch for duckworks carbon ferrule
        this.length = 84 * inch;//84 for Dov
        this.loomLength = 20 * inch;//minimum 14 inch for carbon ferrule, 20 for Dov
        this.loomHeight = 1.5 * inch;
        this.loomWidth = inch;

        this.tipHeight = 2 * inch / 16;// = half of the tip recomended 1/8:1/4
        /*recomended W/2.  choose W/4 forvery flat and W for very pointy.  
        Must be tipRounded <= lMaxWidth*/
        this.tipRounded = width / 2;
        this.concavity = .3;//between 0 and 1.  higher for pointier
        //recopmended min W.  
        //This is the length from the end for which the paddle holds its maximum
        //width.  Must be >= tip rounded
        this.lengthMaxWidth = 2 * width;
        this.shoulderLength = 3 * width / 16;//The lengthof the shoulder.  3*W/16 is recomended.

        loom = Loom.NO_FERRULE;
        hasShoulder = true;
        winged = false;//Doesn't work with ferrules
        wingOffCenterTip = .4;
        
        edgeStart = bldl() / 2;
       // setIDSN();
    }

    private final double bldl() {
        return (length - loomLength) / 2;
    }

    public Shape loom() {
        return loom(loom);
    }

    public Shape loom(Loom l) {
        switch (l) {
            case DUCK_FERRULE:
                Shape s = o.difference(new Cylinder(new Vector(-loomLength/2, 0.0, 0.0), new Vector(loomLength/2, 0.0, 0.0), OD / 2, resolution),
                        new Cube(2 * F_LENGTH, OD + 2, OD + 2));
                o.union(s, new Cylinder(new Vector(F_LENGTH, 0, 0),new Vector(-F_LENGTH, 0, 0),ID / 2, resolution));
                
                return o.difference(s,new Cube(F_LENGTH, OD + 2, OD + 2));
                /*return new Cylinder(new Vector(-loomLength/2, 0.0, 0.0), new Vector(loomLength/2, 0.0, 0.0), OD / 2, resolution)
                        .difference(new Cube(2 * F_LENGTH, OD + 2, OD + 2))
                        .union(new Cylinder(
                                        new Vector(F_LENGTH, 0, 0),
                                        new Vector(-F_LENGTH, 0, 0)
                                        ,ID / 2, resolution)
                                .difference(new Cube(F_LENGTH, OD + 2, OD + 2)));*/

            case MY_FERRULE:
                return woodLoomForCF();
                  
            case NO_FERRULE:
               break;
        }
        
        Shape loom = o.union(loomEnd(l),o.mirror(loomEnd(l),false,true,true)).hull();
                /*loomEnd(l).union(
                loomEnd(l).mirror(false, true, true)).
                hull();*/

        return loom;
    }

    //1 -> cylinder and 0 a square.
    private final double DEFAULT_RW = .8, DEFAULT_RH = .8;

    private Shape loomEnd(Loom loom) {
        double rw = DEFAULT_RW * (loomWidth / 2);
        double rh = DEFAULT_RH * (loomHeight / 2);
        Shape end;

        switch (loom) {

            case DUCK_FERRULE:
                end = new Cylinder(new Vector(-loomLength/2, 0, 0),
                                   new Vector(loomLength/2, 0, 0),
                                    OD / 2, FLAT, resolution);
                        
                break;
            default:
                end = o.scale(new Cylinder(new Vector(0, 0, 0),new Vector(FLAT, 0, 0),1, resolution),1, rw, rh);
                end = end.rtranslate(loomLength / 2, loomWidth / 2 - rw, loomHeight / 2 - rh);
                end = o.union(end,o.mirror(end,true,true,false));
                end = o.union(end,o.mirror(end,true,false,true));                
        }
        
        if (winged) end.translateZ(loomHeight / 2);
        return end.hull();
    }

    public final static double ID = 35.8, OD = 38.6, F_LENGTH = 7 * inch;

    private Shape innerFloom(double length) {
        return loom(Loom.NO_FERRULE).rscale(
                length / loomLength,
                (loomWidth - fThick) / loomWidth,
                (loomHeight - fThick) / loomHeight);
    }

    private Shape outerFloom(double length) {
        return (loom(Loom.NO_FERRULE).difference(innerFloom(loomLength)))
                .intersect(new Cube(length));
    }

    private Shape woodLoomForCF() {

        Shape loom = loom(Loom.NO_FERRULE).difference(new Cube(4 * fLength));
        loom = loom.union(innerFloom(4 * fLength));
        loom = loom.difference(new Cube(2 * fLength));
        return loom;
    }

    public static final int FEMALE = 0, MALE = 1, BOTH = 2;
    private final double fLength = 3.5 * inch, fThick = 3,
            fThickInner = 3 * fThick, holeR = 4.5;

    public Shape myCarbFerrule(int gender) {
        Shape hole = new Cylinder(holeR, loomHeight / 2, 50)
                .rtranslate(-2 * fLength / 3, 0, loomHeight / 4);

        if (gender == FEMALE)
            return outerFloom(2 * fLength)
                    .rtranslateX(-fLength)
                    .difference(hole);

        else if (gender == MALE) {

            Shape innerInnerTube = loom(Loom.NO_FERRULE)
                    .rscale(1,
                            (loomWidth - fThickInner) / loomWidth,
                            (loomHeight - fThickInner) / loomHeight);

            Shape fcube = new Cube(fLength);

            Shape[] sec = new Shape[3];

            sec[0] = outerFloom(fLength).rtranslateX(1.5 * fLength);

            sec[1] = loom(Loom.NO_FERRULE).difference(innerInnerTube)
                    .intersect(fcube);
//TODO:                    .rtranslateX(fLength / 2);

            sec[2] = loom(Loom.NO_FERRULE).difference(innerInnerTube).difference(outerFloom(fLength))
                    .intersect(fcube);
            sec[2]
                    .rscale(1, .98, .987)
                    .rtranslateX(-fLength / 2)
                    .difference(hole);

            return sec[0].union(sec[1]).union(sec[2]);
        }

        Shape both = myCarbFerrule(FEMALE).rtranslateY(loomWidth + 2);
        both = both.union(myCarbFerrule(MALE).rtranslateY(-loomWidth - 2));
        return both;

    }

    private static enum Dir {
        FLAT, OUT, IN
    }

    private Shape halfCircle(Dir t, double rl, double rw, double rh, double edge) {
        
        int myfn = (int) (this.resolution / (1 - edge));

        Shape topHalf = new Cube(new Vector(0, 0, 2), new Vector(4, 4, 4));

        if (t != Dir.FLAT)
            topHalf.translateX(t == Dir.OUT ? 2 : -2);

        //Note, this is mynn^2 operations 
        //to change stakc size go to project options -> run 
        //and input -Xss515m where 515 is the number of MB for the stack.
        Shape hc = topHalf.intersect((t == Dir.FLAT
                ? new Cylinder(new Vector(0, 0, 0), new Vector(FLAT, 0, 0), 1, myfn)
                : new Sphere(1, myfn, myfn)));

        hc.translateZ(-edge);

        hc = hc.intersect(topHalf);

        hc.scale(t == Dir.FLAT ? 1 : rl / Math.sqrt(1 - edge * edge),
                rw / Math.sqrt(1 - edge * edge),
                rh / (1 - edge));

        return hc;
    }

    //still needs to be implemented, different edges for the different sides?
    private Shape wingHalfCircle(Dir t, double rl, double rw, double rh, double edge, double xh) {
        
        int myfn = (int) (this.resolution / (1 - edge));

        double scX = t == Dir.FLAT ? 1 : rl / Math.sqrt(1 - edge * edge), 
               scY = rw / (Math.sqrt(1 - edge * edge)), 
               scZ = rh / (1 - edge);
        
        
        Shape topHalf = new Cube(4).rtranslate(0, 2, 2);
        if (t != Dir.FLAT) topHalf.translateX(t == Dir.OUT ? 2 : -2);

        Shape left = (t == Dir.FLAT
                ? new Cylinder(new Vector(0, 0, 0), new Vector(FLAT, 0, 0),1,myfn)
                : new Sphere(1, myfn, myfn));

        left = left.rtranslateZ(-edge).intersect(topHalf);
        left.scale(scX, scY, scZ);      
        
        Shape right = left.mirror(true,false,true);
        
        left.rscaleY(1- xh).translateY(xh*rw);
        
        right.rscaleY(1+xh).translateY(xh*rw);
        //*/
        return left.dumbUnion(right);
    }

    private Shape fullCircle(Dir t, double rl, double x, double edge) {
        return fullCircle(t, rl, x, edge, winged);
    }

    private Shape fullCircle(Dir t, double rl, double x, double edge, boolean winged) {

        Shape c = (winged?
                wingHalfCircle(t, rl, w(x), 2*h(x), edge, wingedHY(x)):
                halfCircle(t, rl, w(x), (winged ? 2 : 1) * h(x), edge)
                ).rtranslateX(x + loomLength / 2);

        
        return winged ? c.rtranslateZ(loomHeight - 2 * h(x)) : c.union(c.mirror(true,true,false));
    }

    private Shape shoulderEnd(boolean isFlat) {

        Shape sh = fullCircle(isFlat ? Dir.FLAT : Dir.IN,
                15, shoulderLength, 0, false);
        if (winged) sh.translateZ(loomHeight / 2);
        return sh;
    }

    public Shape shoulder() {
        return shoulder(loom);
    }

    public Shape shoulder(Loom type) {
        return shoulderEnd(false).hull(loomEnd(type));
    }

    private double h(double x) {
        return (loomHeight / (2 * (bldl() - shoulderLength))) * (bldl() - x)
                + (x - shoulderLength) * tipHeight / (bldl() - shoulderLength);
    }

    private double wingedHY(double x) {
        return wingOffCenterTip*x/bldl();
    }

    private double w(double x) {

        if (x > bldl() - lengthMaxWidth) return width / 2;

        return (width / (4 * (bldl() - lengthMaxWidth - shoulderLength))) * (bldl() - lengthMaxWidth - x)
                + (x - shoulderLength) * (width / 2) / (bldl() - lengthMaxWidth - shoulderLength);
    }

    private Shape upperBladeEnd() {
        return fullCircle(Dir.FLAT, FLAT, edgeStart, concavity);
    }

    private Shape midBladeEnd(boolean isFlat) {
        return fullCircle(isFlat ? Dir.FLAT : Dir.IN,
                Math.min(2 * width, lengthMaxWidth), bldl() - lengthMaxWidth, concavity);
    }

    public Shape upperBlade() {
        return shoulderEnd(true).hull(upperBladeEnd());
    }

    public Shape midBlade() {
        return upperBladeEnd().hull(midBladeEnd(false));
    }

    public Shape bladeEnd() {
        return bladeEnd(false);
    }

    private Shape bladeEnd(boolean isFlat) {
        return fullCircle(isFlat ? Dir.FLAT : Dir.OUT,
                tipRounded, bldl() - tipRounded, concavity);
    }

    public Shape lowerBlade() {
        return midBladeEnd(true).hull(bladeEnd(true));
    }
    
    private Shape skeleton(){
        return loomEnd(loom).dumbUnion(shoulderEnd(false))
                .dumbUnion(upperBladeEnd()).dumbUnion(midBladeEnd(false))
                .dumbUnion(bladeEnd());
    }
    
    private Shape blade(){
        return upperBlade().dumbUnion(midBlade()).dumbUnion(lowerBlade()).dumbUnion(bladeEnd());
    }

    public Shape full() {

        Shape full = shoulder().dumbUnion(blade());
        full = full.dumbUnion(full.mirror(false,true,true));
        return full.dumbUnion(loom(loom));

    }

    public void highDetailSTLMultiThread(String fileName) throws Exception {

        new PartMaker(PartMaker.Part.SHOULDER, this, fileName);
        new PartMaker(PartMaker.Part.UPPER, this, fileName);
        new PartMaker(PartMaker.Part.MID, this, fileName);
        new PartMaker(PartMaker.Part.LOWER, this, fileName);
        new PartMaker(PartMaker.Part.END, this, fileName);
        new PartMaker(PartMaker.Part.LOOM, this, fileName);
    }

    public void highDetailSTL(String fileName) throws Exception {
        
        if(winged) throw new UnsupportedOperationException
        ("winged paddles can not be built peacewise "
                + "since there's a hull on the entire blade.");
        Shape csg;

        csg = lowerBlade();
        myWrite("lb1.stl", csg);
        myWrite("lb2.stl", csg.mirror(false,true,true));

        csg = loom();
        myWrite("loom.stl", csg);

        csg = midBlade();
        myWrite("mb1.stl", csg);
        myWrite("mb2.stl", csg.mirror(false,true,true));

        csg = shoulder();
        myWrite("sh1.stl", csg);
        myWrite("sh2.stl", csg.mirror(false,true,true));

        csg = upperBlade();
        myWrite("ub1.stl", csg);
        myWrite("ub2.stl", csg.mirror(false,true,true));

        csg = bladeEnd();
        myWrite("be1.stl", csg);
        myWrite("be2.stl", csg.mirror(false,true,true));

        new STLMerger("sh1.stl", "sh2.stl", "ub1.stl", "ub2.stl", "mb1.stl",
                "mb2.stl", "lb1.stl", "lb2.stl", "be1.stl", "be2.stl", "loom.stl")
                .merge("Paddles/ID_" + IDSN + "_" + fileName + ".stl", toString());

        Paddle.myWrite("Paddles/ID_" + IDSN + "_" + fileName + "_lamination.stl", laminationBlock());

    }

    final double armor = inch / 4;

    private Shape laminationBlock(int i) {
        switch (i) {
            case 0:
                double width = this.width / 2 + armor;
                Shape board = new Cube(length, this.width, loomHeight);
                Shape side = new Cube(length, width, loomHeight);
                side = side.rtranslateX(length / 2)
                        .rtranslateY(-width / 2);

                side = side.rrotZ(Math.atan2(this.width / 4 - armor, bldl() - shoulderLength - lengthMaxWidth)
                        * 180 / Math.PI);

                side = side.
                        rtranslate(loomLength / 2 + shoulderLength, -this.width / 4, 0)
                        .intersect(board);

                Shape endSide = new Cube(lengthMaxWidth, armor, loomHeight)
                        .rtranslate(lengthMaxWidth / 2 + length / 2 - lengthMaxWidth, -this.width / 2 + armor / 2, 0);
                return side.union(endSide);

            case 1:
                return laminationBlock(0).mirror(true,false,true);
            case 2:
                return laminationBlock(0).mirror(false,true,true);
            case 3:
                return laminationBlock(2).mirror(true,false,true);
        }

        return laminationBlock(0).dumbUnion(laminationBlock(1)).dumbUnion(laminationBlock(2)).dumbUnion(laminationBlock(3));
    }

    public Shape laminationBlock() {
        Shape center = new Cube(length, width, loomHeight);
        center = center.difference(laminationBlock(4));
        center = center.dumbUnion(laminationBlock(0).rtranslate(10, -10, 0));
        center = center.dumbUnion(laminationBlock(1).rtranslate(10, 10, 0));
        center = center.dumbUnion(laminationBlock(2).rtranslate(-10, -10, 0));
        center = center.dumbUnion(laminationBlock(3).rtranslate(-10, 10, 0));

        return center;
    }

 

    public static void myWrite(String file, Shape csg) {
        //FileUtil.write(Paths.get(file), csg.toString());
        
        
    }

}