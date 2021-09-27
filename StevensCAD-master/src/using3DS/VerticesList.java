/*
* Concrete class for Vertices Lists taken from 3DS
* Author: Michael Moschetti & John Anticev
*/
package using3DS;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import scad.Vector;

/**
 * Class that holds the Vertex List data from the .3ds file.
 * @param list An ArrayList of all vertices in the object, stored as scad.Vectors.
 * @param size The total number of vertices.
 */
class VerticesList extends Format3DS {
    
    private int size;
    private ArrayList<scad.Vector> list;
    
    /**
     * Must be used with a ByteBuffer that contains the DATA segment of a Vertices List chunk.
     * @param bufferData The DATA section of a VERTICES LIST chunk.
     */
    VerticesList(ByteBuffer bufferData) { 
        super(bufferData); 
        list = new ArrayList<>();
        size = (getDataBuffer().getShort() & 0x0000FFFF);
        for(int i = 0; i < size; i++){
            double x = Double.parseDouble(new Float(getDataBuffer().getFloat()).toString());
            double y = Double.parseDouble(new Float(getDataBuffer().getFloat()).toString());
            double z = Double.parseDouble(new Float(getDataBuffer().getFloat()).toString());
            list.add(new scad.Vector(x,y,z));
        }
        /*
        System.out.println(size);
        for(scad.Vector x : list){
            System.out.println(x);
        }
        System.out.println(list.size());
        */
    }

    /**
     * Returns all vertices for an object from a ByteBuffer of vertices grabbed from 3DS.
     * @return Arraylist of scad.Vector objects.
     */
    @Override
    public ArrayList<scad.Vector> parseBytes() {
        return this.list;
    }
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(scad.Vector x : list){
            s.append(x.toString());
            s.append(System.getProperty("line.separator"));
        }
        return s.toString();
    }
    public int getSize(){
        return size;
    }
}