/*
* Concrete class for Faces Descriptions taken from 3DS
* Author: John Anticev & Mike Moschetti
*/
package using3DS;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import scad.Vector;
import scad.Triangle;

/**
 * Class that holds the Mapping Coordinates List data from the .3ds file.
 * @param list is an ArrayList containing integer arrays of size 2. 
 * Each integer array holds u and v values for the corresponding vertex in the Vertices List, in order.
 * @param size The total number of faces.
 */
class MappingCoordinates extends Format3DS {
    
    private int size;
    private ArrayList<int[]> list;
    
    /**
     * Must be used with a ByteBuffer that contains the DATA segment of a Mapping Coordinates List chunk.
     * @param bufferData The DATA section of a MAPPING COORDINATES LIST chunk.
     */
    MappingCoordinates(ByteBuffer bufferData) { 
        super(bufferData); 
        list = new ArrayList<>();
        size = (getDataBuffer().getShort() & 0x0000FFFF);
        for(int i = 0; i < size; i++){
            int[] map = new int[2];
            map[0] = (getDataBuffer().getShort() & 0x0000FFFF);
            map[1] = (getDataBuffer().getShort() & 0x0000FFFF);
            list.add(map);
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
    public ArrayList<int[]> parseBytes() {
        return this.list;
    }
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int[] x : list){
            for(int y : x){
                s.append(Integer.toString(y));
            }
            s.append(System.getProperty("line.separator"));
        }
        return s.toString();
    }
    public int getSize(){
        return size;
    }
}