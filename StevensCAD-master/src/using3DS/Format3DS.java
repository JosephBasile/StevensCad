/*
* Parent class of all 3DS Formatted Data
* Author: Michael Moschetti & John Anticev
*/
package using3DS;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.nio.ByteOrder;

/**
 * Abstract class that all Chunk formats inherit from.
 */
abstract class Format3DS {

    private String fileName;

    private byte[] data;
    
    private ByteBuffer dataBuffer;

    public ByteBuffer getDataBuffer() {
        return dataBuffer;
    }
    
    public byte[] getData() { 
        return data; 
    }

    // Take in file from user and use to create sub objects
    public Format3DS(String fileName) { this.fileName = fileName; }

    // Super constructor for children to store data
    protected Format3DS(ByteBuffer dataBuffer) { 
        this.dataBuffer = dataBuffer;
        this.data = dataBuffer.array();
    }
    
    /**
     * Parse the data chunk that belongs to this ID.
     * @return A typed array list containing the data.
     */
    public abstract <E> ArrayList<E> parseBytes();
}
