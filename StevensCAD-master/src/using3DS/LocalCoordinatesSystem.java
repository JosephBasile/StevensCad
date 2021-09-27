/*
* Concrete class for 3DS format
* Author: Michael Moschetti & John Anticev
*/
package using3DS;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Class that holds the Local Coordinates System data from the .3ds file.
 */
class LocalCoordinatesSystem extends Format3DS {
    
    private ArrayList<Matrix> list;
    
    /**
     * Must be used with a ByteBuffer that contains the DATA segment of a Local Coordinates System chunk.
     * @param bufferData The DATA section of a Local Coordinates System chunk.
     */
    LocalCoordinatesSystem(ByteBuffer bufferData) { 
        super(bufferData); 
        list = new ArrayList<>();
        Matrix matrix = new Matrix();
        for(int row = 0; row < 4; row++) {
            matrix.put(row, 0, bufferData.getFloat());
            matrix.put(row, 1, bufferData.getFloat());
            matrix.put(row, 2, bufferData.getFloat());
        }
        list.add(matrix);
    }

    /**
     * Returns the translational matrix for an object from the .3ds file.
     * @return Arraylist holding a single 4x4 matrix.
     */
    @Override
    public ArrayList<Matrix> parseBytes() {
        return this.list;
    }
    @Override
    public String toString(){
        return list.get(0).toString();
    }
}
