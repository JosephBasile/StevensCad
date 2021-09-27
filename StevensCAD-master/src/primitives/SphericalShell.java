package primitives;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.round;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import primitives.AnalyticalShape3d;
import primitives.ConvexPoly;
import scad.Facet;
import scad.Func2Vars;
import scad.Polyhedron;
import scad.Vector;
import transformation.Transformation;
import viewer.CADEditor;

/**
 *
 * @author dkruger, Alessandro
 *
 */

public class SphericalShell extends AnalyticalShape3d  {
    public int u_res, v_res;
    public double min_u,max_u,min_v,max_v, radius, thickness;
  // public ArrayList<Vector> verts;
    public SphereSurface s;
    public static double defaultMinV, defaultMinU, defaultMaxV, defaultMaxU, defaultRadius, defaulThickness;
    public static int defaultU_res,defaultV_res;
    static {
        defaultRadius = 20;
        defaulThickness = 1;
        defaultMinV = 0;
        defaultMinU = 0;
        defaultMaxV = 2*PI;
        defaultMaxU = PI;
        defaultU_res = 10;
        defaultV_res = 10;
    }
    
    /**
    * Default constructor 
    */
    public SphericalShell(){
        this.radius = defaultRadius;
        this.thickness = defaulThickness;
        this.u_res = defaultU_res;
        this.v_res = defaultV_res;
       // this.verts = new ArrayList<>();
        this.min_u = defaultMinU;
        this.max_u = defaultMaxU;
        this.min_v = defaultMinV;
        this.max_v = defaultMaxV;
        poly = new ConvexPoly(defaultU_res * defaultV_res);
        t = new Transformation();
        initializeSphericalShell();
    }
    
    /**
    * Constructor
     * @param r
     * @param thickness
     * @param u_res
     * @param v_res
    */
    public SphericalShell(double r, double thickness, int u_res, int v_res) {
        this.radius = r;
        this.thickness = thickness;
        this.u_res = u_res;
        this.v_res = v_res;
        this.min_u = defaultMinU;
        this.max_u = defaultMaxU;
        this.min_v = defaultMinV;
        this.max_v = defaultMaxV;
        
        poly = new ConvexPoly(u_res * v_res);
        t = new Transformation();
        
        initializeSphericalShell();
    }
    
    /**
    * Method to initialize the spherical shell. 
    */
    public void initializeSphericalShell(){
        s = new SphereSurface(radius, u_res, v_res);
        s.setMinU(String.valueOf(min_u));
        s.setMaxU(String.valueOf(max_u));
        s.setMinV(String.valueOf(min_v));
        s.setMaxV(String.valueOf(max_v));
        Vector center = Vector.ZERO;
        outer:  for(int u = 0; u < u_res; u++){
            for(int v = 0; v < v_res; v++){
                if (u == u_res -1 && v == 2){
                    continue outer; 
                }
                vertices.add(s.mapSurface(u, v));
                Vector fromCenter = s.mapSurface(u, v).minus(center);
                vertices.add(s.mapSurface(u, v).plus(fromCenter.normalized().times(thickness)));
            }
        }
               // poly.vertices = vertices;

        drawSphericalShell();

    }
    
   /**
    * Method to create triangular facets for the shape
    * @param int 
    */
    private void addTriangle(int a, int b, int c){
        poly.add(new Facet(vertices.get(a),vertices.get(b),vertices.get(c)));
    }
     /**
    * Method to draw the spherical shell 
    */
    public void drawSphericalShell() {
        
        /*
        Draw the north pole of the spherical shell only if the  min_u = 0 (if the sphere is complete).
        */
        if(min_u == 0)
            for (int i = 2; i <= 2 * v_res  ; i+=2){
                if(min_v !=0 && i == 2*v_res)
                    continue;
                addTriangle(1,i+1,(i == (2 *  v_res)  ? 3 : i+3));
                addTriangle(0,(i == (2 *  v_res)  ? 2 : i+2),i);
            }
        else{
            for (int i = 2; i <= 2 * v_res  - 2; i+=2){
               addTriangle(i,i+1,(i == (2 *  v_res)  ? 2 : i+2));
                addTriangle((i == (2 *  v_res)  ? 2 : i+2),i+1,(i == (2 *  v_res)  ? 3 : i+3));
            }
            
            
        }
          /*
            Draw the south pole of the spherical shell only if the  max_u = PI.
        */
        if(max_u == PI){
            
            for (int i = vertices.size()-3; i >= vertices.size()- (2*v_res+ 2) ; i-=2){
                if(min_v != 0 && i == vertices.size()-(2*v_res+1)){
                    continue;
                }
                addTriangle(vertices.size()-1, i,(i==vertices.size()- (2*v_res + 1) ? vertices.size()  - 3 :  i-2));
                addTriangle(vertices.size()-2,(i==vertices.size()- (2*v_res+ 1)  ? vertices.size()  - 4 : i-3), i - 1);
            }
            
        }else{
            
            for(int i = vertices.size() - 3 ; i >=vertices.size()- (2*v_res) ; i-=2 ){                
                addTriangle( i - 1 ,i,(i==vertices.size()- (2*v_res+ 1)  ? vertices.size()  - 3 : i-2));
                addTriangle(  i -1 ,(i==vertices.size()- (2*v_res+ 1)  ? vertices.size()  - 3 : i - 2) ,(i==vertices.size()- (2*v_res+ 1)  ? vertices.size()  - 4 : i-3));
            }
        }
          /*
            Draw the spherical shell. 
        */
          if(min_v==0 && max_v==(2*PI)){
              for (int i = 2; i < vertices.size() - (2*v_res) - 2; i+=2){
                  
                  /*
                  *Internal faces
                  */
                  addTriangle( i + (2*v_res)  , i , (i % (2*v_res) == 0 ? -(2* v_res ): 0) +  i + (2*v_res) + 2);
                  addTriangle(i , (i % (2*v_res) == 0 ? -(2* v_res ): 0) + i+2, (i % (2*v_res) == 0 ? -(2* v_res ): 0) + i+ (2*v_res)+ 2);
                  /*
                  *External faces
                  */
                  addTriangle(i + 1, i + (2*v_res)+1  , (i % (2*v_res) == 0 ? -(2* v_res ): 0) +  i + (2*v_res) + 3);
                  addTriangle((i % (2*v_res) == 0 ? -(2* v_res ): 0) + i + 3, i + 1 , (i % (2*v_res) == 0 ? -(2* v_res ): 0) + i + (2*v_res)+ 3);
              }
              
          }else{
              
              int count = 0;
              
              if(min_u == 0){
                  addTriangle(0, 2, 1);
                  addTriangle(1, 2, 3);
                  addTriangle(0,1,(2*v_res));
                  addTriangle(1,(2*v_res)+1,(2*v_res));
              }
              
              if(max_u == PI){
                  addTriangle((vertices.size()-1), (vertices.size()-2), (vertices.size()-3));
                  addTriangle((vertices.size()-2),(vertices.size()-4),  (vertices.size()-3));
                  addTriangle((vertices.size()-1), (vertices.size()-(2*v_res) - 1), (vertices.size()-2));
                  addTriangle((vertices.size()-2), (vertices.size()-(2*v_res) - 1), (vertices.size()-(2*v_res) - 2));

                
              }
              
              for (int i = 2; i < vertices.size() - (2*v_res) - 2; i+=2){                
                  
                  if(count == 0){
                      addTriangle(i, i+(2*v_res),i+1);
                      addTriangle(i+1, i+(2*v_res), i+(2*v_res)+1);
                  }
                  count++;
                  if (count == v_res){
                      addTriangle(i, i+1, i+(2*v_res));
                      addTriangle( i+(2*v_res), i+1, i+(2*v_res)+1);
                      
                      count = 0;
                      continue;
                  }
                  /*
                  *Internal faces
                  */
                  addTriangle( i + (2*v_res)  ,i, (i % (2*v_res) == 0 ? -(2* v_res + 1) : 0) +  i + (2*v_res) + 2);
                  addTriangle(i , (i % (2*v_res) == 0 ? -(2* v_res + 1 ): 0) + i+2, (i % (2*v_res) == 0 ? -(2* v_res +1): 0) + i+ (2*v_res)+ 2);
                  /* 
                  *External faces
                  */
                  addTriangle(i + 1, i + (2*v_res)+1  , (i % (2*v_res) == 0 ? -(2* v_res + 3): 0) +  i + (2*v_res) + 3);
                  addTriangle((i % (2*v_res) == 0 ? -(2* v_res + 3 ): 0) + i + 3, i + 1 , (i % (2*v_res) == 0 ? -(2* v_res + 3): 0) + i + (2*v_res)+ 3);
                 
              }
          }
               poly.vertices = vertices;

    }
    
    public Polyhedron generate(Func2Vars thickness) {
        return null;
    }
    
    public Polyhedron generate(double thickness) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Polyhedron generate(double dx, double dy, double[][] thickness) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public double getMinU() {
        return this.s.getMinU();
    }
    
    public double getMaxU() {
        return this.s.getMaxU();
    }
    
    public double getMinV() {
        return this.s.getMinV();
    }
    
    public double getMaxV() {
        return this.s.getMaxV();
    }
    
    public void setMinU(String u1) {
        double u = Double.valueOf(u1);
        this.min_u = u;
        vertices.clear();
        poly = new ConvexPoly();
        initializeSphericalShell();
        
    }
    
    public void setMaxU(String u1) {
        double u = Double.valueOf(u1);
        this.max_u = u;
        vertices.clear();
        poly = new ConvexPoly();
        initializeSphericalShell();
    }
    
    public void setMinV(String v1) {
        double v = Double.valueOf(v1);    
        this.min_v = v;
        vertices.clear();
        poly = new ConvexPoly();
        initializeSphericalShell();
    }
    
    public void setMaxV(String v1) {
        double v = Double.valueOf(v1);    
        this.max_v = v;
        vertices.clear();
        poly = new ConvexPoly();
        initializeSphericalShell();
    }
    
    public boolean greaterThan(Vector v) {
        return false;
    }
    
    public Vector mapSurface(int u, int v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
    * Method to apply a image on a spherical shell. 
     * @param f
     * @throws java.io.IOException 
    */
    public void setTickness(File f ) throws IOException{
        
        BufferedImage img = ImageIO.read(f);
        int vector = 1;
        
        double min_h = this.min_u/PI;
        double max_h = this.max_u/PI;
        double min_w = this.min_v/(2*PI);
        double max_w = this.max_v/(2*PI);
        
        double[] tickness = new double[255*4+1];
        int[] brightness = new int[255*4+1];
        this.u_res = img.getHeight();
        this.v_res = img.getWidth();
        
//        System.out.println(img.getHeight() + "      " + img.getWidth());
//        System.out.println((int) round( (img.getHeight() - (img.getHeight()*this.min_u/PI)) - (img.getHeight()- (this.max_u*img.getHeight()/PI))) + "      " + (int) round( ((img.getWidth()- img.getWidth()*this.min_v/(2*PI))) - (img.getWidth()- (this.max_v*img.getWidth()/(2*PI)))));

        
        this.u_res = (int) round( (img.getHeight() - (img.getHeight()*this.min_u/PI)) - (img.getHeight()- (this.max_u*img.getHeight()/PI)));
        this.v_res = (int) round( ((img.getWidth()- img.getWidth()*this.min_v/(2*PI))) - (img.getWidth()- (this.max_v*img.getWidth()/(2*PI))));
        
        int start_h = 0;
        int start_w = 0;
        int end_h = img.getHeight();
        int end_w = img.getWidth();
        
        vertices.clear();
        poly = new ConvexPoly();
        initializeSphericalShell();
        
        poly = new ConvexPoly();
        
        if(min_u != defaultMinU)
            start_h = (int) round(img.getHeight()*min_h);
        if(min_v != defaultMinV)
            start_w = (int) round(img.getWidth()*min_w);
        if(max_u != defaultMaxU)
            end_h = (int) floor(img.getHeight()*max_h);
        if(max_v != defaultMaxV)
            end_w = (int) round(img.getWidth()*max_w);
       
     /*
        Map all the color inside the picture
     */
     for(int i = start_h; i < end_h ; i++){
            for(int j = start_w; j < end_w; j++){
               int rgb = img.getRGB(j, i);
               int a = (rgb >> 24) & 0xff;
               int r = (rgb>>16)&0xff;
               int g = (rgb>>8)&0xff;
               int b = rgb&0xff;
               brightness[a+r+b+g]++;
            }
     }
     /*
        Map all the color with a defined thickness
     */
     double tick = 0.0;
     for(int i =0; i< brightness.length;i++){
         if(brightness[i]!=0){
             if(i==255){
                 tickness[i] = -0.8;
             }else{
             tickness[i]=tick;
             tick += radius/8000;
             }
         }
     }
     
     /*
       Set the new thickmess
     */
     for(int i = start_h; i <= end_h ; i++){
         for(int j = start_w; j < end_w; j++){
             if(vector >= vertices.size())
                 break;
             
             int rgb = img.getRGB(j, i);
             int a = (rgb >> 24) & 0xff;
             int r = (rgb>>16) & 0xff;
             int g = (rgb>>8) & 0xff;
             int b = rgb & 0xff;
             Vector fromCenter = vertices.get(vector).minus(Vector.ZERO);
             vertices.set(vector, vertices.get(vector).plus(fromCenter.normalized().times(tickness[a+r+b+g])));
             vector+=2;
         }
     }
     
     drawSphericalShell();
    }
    
    
    public static void main(String[] args) throws IOException{
        SphericalShell s = new SphericalShell(50,1,10,10);
        
        File f = new File("earth400x200black.png");
        
//        s.setMinU("0.53");
//        s.setMaxU("2.58");


    //   s.setMinU("1.57");
       //s.setMinV("3.14");
       // s.setMinV("2.1");
       //s.setMaxV("4.71");

       
       
        s.setTickness(f);
       
        CADEditor view = CADEditor.openWindow();
        view.add(s);
    }
}
