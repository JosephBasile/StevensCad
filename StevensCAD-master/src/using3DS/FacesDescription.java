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
 * Class that holds the Vertex List data from the .3ds file.
 * @param list is an ArrayList containing integer arrays of size 4. 
 * The first 3 integers in the array contain the index of a vertex in the vertices list for this object.
 * The last integer contains flags for the face.
 * @param size The total number of faces.
 */
class FacesDescription extends Format3DS {
    
    private int size;
    private ArrayList<int[]> list;
    
    /**
     * Must be used with a ByteBuffer that contains the DATA segment of a Faces Description chunk.
     * @param bufferData The DATA section of a FACES DESCRIPTION chunk.
     */
    FacesDescription(ByteBuffer bufferData) { 
        super(bufferData); 
        list = new ArrayList<>();
        size = (getDataBuffer().getShort() & 0x0000FFFF);
        for(int i = 0; i < size; i++){
            int[] face = new int[4];
            face[0] = (getDataBuffer().getShort() & 0x0000FFFF);
            face[1] = (getDataBuffer().getShort() & 0x0000FFFF);
            face[2] = (getDataBuffer().getShort() & 0x0000FFFF);
            face[3] = (getDataBuffer().getShort() & 0x0000FFFF);
            list.add(face);
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
            int count = 0;
            for(int y : x){
                if(count < 3){
                    s.append(Integer.toString(y));
                    s.append(",");
                    count++;
                    continue;
                }
                s.append(Integer.toBinaryString(y));
            }
            s.append(System.getProperty("line.separator"));
        }
        return s.toString();
    }
    public int getSize(){
        return size;
    }
}