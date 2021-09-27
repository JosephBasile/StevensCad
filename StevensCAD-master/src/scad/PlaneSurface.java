package scad;

/**
 *
 * @author Alessandro
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.PI;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import primitives.AnalyticalShape3d;
import primitives.ConvexPoly;
import viewer.CADEditor;

public class PlaneSurface  extends AnalyticalShape3d implements Surface3d
{
    int u_res, v_res;
    double height,base,min_u, min_v,max_u,max_v;
    
    public PlaneSurface(double height, double base, int u_res,int v_res){
        this.min_u = 0;
        this.min_v =0;
        this.max_u = height;
        this.max_v = base;
        this.u_res = u_res;
        this.v_res = v_res;
        initializePlane();
    }
    
    private void addSquare(int a, int b, int c, int d) {
      // poly.add(new Facet(verts.get(a), verts.get(b), verts.get(c), verts.get(d)));
      poly.add(new Facet(vertices.get(a),vertices.get(b), vertices.get(c)));
      poly.add(new Facet(vertices.get(c),vertices.get(d), vertices.get(a)));

    }
     
    public void initializePlane(){
        double u = min_u, v = min_v;
        double t_u = max_u / u_res;
        double t_v = max_v / v_res;
        for(int i = 0; i < u_res; i++, u+=t_u){
            v = min_v;
            for(int j = 0; j < v_res; j++, v+=t_v){
               vertices.add(new Vector(u, v, 0));
            }
        }
        drawPlane();
     }
    
    public void drawPlane() {
        /* Vector_counter to check which vector long v res column has been processed*/
        int cnt = 0;
        /*Column_counter to chek which v res column has been reached*/
        int f = 1;
        for (int i = 0; i < vertices.size() - v_res - 1; i++){
        /* if it is the last vector long v resolution, skip it and go to the next*/
            if(f * v_res - i == 1 || i - f * v_res == 1){ 
                ++i;}
            addSquare(i ,i + v_res, i + v_res + 1, i + 1);
//           
            /* if the last vector of the current column has been reached,
` `          * reset the counter and increse the column counter
             */
            if(cnt==v_res-1){
                ++f;
                cnt=0;}
            ++cnt;
        }
    }
  
    
    public void setMinU(double u) {
        this.min_u = u;
    }
    
   
    public void setMaxU(double u) {
        this.max_u = u;
    }
    
   
    public void setMinV(double v) {
        this.min_v = v;
    }
    
    
    public void setMaxV(double v) {
        this.max_v = v;
    }
    
    @Override
    public double getMinU() {
        return min_u;
    }
    
    @Override
    public double getMaxU() {
        return max_u;
    }
    
    @Override
    public double getMinV() {
        return min_v;
    }
    
    @Override
    public double getMaxV() {
        return max_v;
    }
    
    public Vector mapSurface(int u, int v) {
            return vertices.get(this.v_res*(u)+v);     
    }
    
    @Override
    public boolean greaterThan(Vector v) {
        //if there are no moutains        
        return poly.get(0).above(v);
    }
    
    public void setTickness(File f ) throws IOException{
        BufferedImage img = ImageIO.read(f);
        int vector = 0;
        double[] tickness = new double[255*4+1];
        int[] brightness = new int[255*4+1];
        this.u_res = img.getHeight();
        this.v_res = img.getWidth();
        
        vertices.clear();
        poly = new ConvexPoly();
        initializePlane();

     /*
        Map all the color inside the picture
     */
     for(int i = 0; i < img.getHeight() ; i++){
            for(int j = 0; j < img.getWidth(); j++){
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
             
             tickness[i]=tick;
             tick += 1;
             
         }
     }
     
     /*
       Set the new thickmess
     */
     for(int i = 0; i < img.getHeight() ;i++){
         for(int j = 0; j < img.getWidth(); j++){
             
             int rgb = img.getRGB(j, i);
             int a = (rgb >> 24) & 0xff;
             int r = (rgb>>16) & 0xff;
             int g = (rgb>>8) & 0xff;
             int b = rgb & 0xff;

              Vector temp = vertices.get(vector);
              temp.setCoordinates(temp.x, temp.y, temp.z + tickness[a+b+r+g]);
              vertices.set(vector, temp);
             vector += 1;
         }
     }
        drawPlane();
    }
      
    public static void main(String[] args) throws IOException{
        PlaneSurface e = new  PlaneSurface(200,200,5,5);
        File f = new File("stevens.png");       
        e.setTickness(f);
        CADEditor view = CADEditor.openWindow(0,20,800,400);
        view.add(e);
    }
}
