/*
* Deprecated
* Author: Michael Moschetti & John Anticev
*/

package using3DS;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Deprecated test for some of the logic for reading values out of binary 3DS, do not use.
 * @author John & Mike
 */
class Import3DS {

    private String fileName;

    private byte[] bytes;
    
    private ByteBuffer buffer;
    
    private static HashMap<String, Integer> fileFormat;

    private Import3DS(String fileName) {
        this.fileName = fileName;
        fileFormat = new HashMap<>();
        loadFormat();
    }

    
    public static void main(String args[]) {
        Import3DS in = new Import3DS("data/dice.3ds");
        in.read();
        ByteBuffer diceVerticesBuffer = in.getChunk("Vertices List");
        VerticesList diceVerticesList = new VerticesList(diceVerticesBuffer);
    }
    /**
     * Take in an identifying key String, find the corresponding ID in the code, and return the corresponding byte array.
     * @param key
     * @return 
     */
    private ByteBuffer getChunk(String key) {
        int startOfChunk = -1;
        int startOfSize = -1;
        int startOfData = -1;
        while(buffer.position() < buffer.limit()){
            short currentShort = buffer.getShort();
            System.out.println(String.format("%02X", (currentShort)));
            if(currentShort == fileFormat.get(key)){
                startOfChunk = buffer.position()-2;
                startOfSize = startOfChunk + 2;
                startOfData = startOfSize + 4;
            }
        }
        System.out.println(String.format("%02X", (buffer.getShort(startOfChunk) & 0xFFFF)));
        
        if(startOfChunk == -1){ //key not found
                return null;
        }
        
        int size = buffer.getInt(startOfSize);
        
        //System.out.println(String.format("%08X",size));
        
        System.out.println(size);
        byte[] returnBytes = Arrays.copyOfRange(bytes, startOfData, startOfData + size);
        return ByteBuffer.wrap(returnBytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static void loadFormat() {
        fileFormat.put("Main", 0x4D4D);
        fileFormat.put("M3D Version", 0x0002);
        fileFormat.put("3D Editor", 0x3D3D);
        fileFormat.put("Object Block", 0x4000);
        fileFormat.put("Triangular Mesh", 0x4100);
        fileFormat.put("Vertices List", 0x4110);
        fileFormat.put("Faces Description", 0x4120);
        fileFormat.put("Faces Material", 0x4130);
        fileFormat.put("Smoothing Group List", 0x4150);
        fileFormat.put("Mapping Coordinates List", 0x4140);
        fileFormat.put("Local Coordinates System", 0x4160);
        fileFormat.put("Light", 0x4600);
        fileFormat.put("Spotlight", 0x4610);
        fileFormat.put("Camera", 0x4700);
        fileFormat.put("Material Block", 0xAFFF);
        fileFormat.put("Material Name", 0xA000);
        fileFormat.put("Ambient Color", 0xA010);
        fileFormat.put("Diffuse Color", 0xA020);
        fileFormat.put("Specular Color", 0xA030);
        fileFormat.put("Texture Map 1", 0xA200);
        fileFormat.put("Bump Map", 0xA230);
        fileFormat.put("Reflection Map", 0xA220);
        fileFormat.put("Mapping Filename", 0xA300);
        fileFormat.put("Mapping Parameters", 0xA351);
        fileFormat.put("KeyFramer Chunk", 0xB000);
        fileFormat.put("Mesh Information Block", 0xB002);
        fileFormat.put("Spot Light Information Block", 0xB007);
        fileFormat.put("Frames", 0xB008);
        fileFormat.put("Object Name", 0xB010);
        fileFormat.put("Object Pivot Point", 0xB013);
        fileFormat.put("Position Track", 0xB020);
        fileFormat.put("Rotation Track", 0xB021);
        fileFormat.put("Scale Track", 0xB022);
        fileFormat.put("Hierarchy Position", 0xB030);
        
    }

    private void read() {
        try {
            Path path = Paths.get(fileName);
            bytes = Files.readAllBytes(path);
            buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}