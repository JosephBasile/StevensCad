/*
* Generates Format3DS objects for each chunk contained in an input file.
* Author: Michael Moschetti & John Anticev
*/

package using3DS;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import scad.Vector;

/**
 * Runs factory method on all identifiable chunks in the file to create objects for each of them.
 * @author John & Mike
 */
class GenerateFormat {

    private File file;
    
    private byte[] bytes;
    
    private ByteBuffer buffer;

    private static HashMap<String,Integer> fileFormats;

    private HashMap<String,Format3DS> formats;

    public ByteBuffer getDataBuffer() {
        return buffer;
    }
    
    public HashMap getAllFormat3DS(){
        return formats;
    }
    
    /**
     * Populates HashMap formats with Format3DS objects discovered in file.
     * @param file 
     */
    public GenerateFormat(File file) {
        // Load file formats
        fileFormats = new HashMap<>();
        loadFormat();

        // Load data from input file
        try {
            Path path = file.toPath();
            bytes = Files.readAllBytes(path);
            buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        } catch(IOException e) {
            System.out.println(e);
        }

        FormatFactory factory = new FormatFactory();
        formats = new HashMap<>();

        // Hashmaps can hold both null keys and values
        //formats.put("Main",factory.getFormatFactory(fileFormats.get("Main"), getChunk("Main")));
        for(String ID : fileFormats.keySet()) {
            System.out.println(ID); 
            System.out.println(fileFormats.get(ID));
            ByteBuffer data = getChunk(ID);
            if(data != null){
                System.out.println(ID);
                formats.put(ID,factory.getFormatFactory(fileFormats.get(ID),getChunk(ID)));
            }
        }
    }

    /**
     * Search for the Chunk in the raw data.
     * @param key chunk name to search for
     * @return ByteBuffer pertaining to the ID, null if not found.
     */
    private ByteBuffer getChunk(String key) {
        buffer.position(0);
        int startOfChunk = -1;
        int startOfSize = -1;
        int startOfData = -1;
        while(buffer.position() < buffer.limit()){
            short currentShort = buffer.getShort();
            //System.out.println(String.format("%02X", (currentShort)));
            if(currentShort == fileFormats.get(key)){
                startOfChunk = buffer.position()-2;
                startOfSize = startOfChunk + 2;
                startOfData = startOfSize + 4;
            }
        }
        
        if(startOfChunk == -1){ //key not found
                return null;
        }
        
        //System.out.println(String.format("%02X", (buffer.getShort(startOfChunk) & 0xFFFF)));
        
        int size = buffer.getInt(startOfSize);
        
        //System.out.println(String.format("%08X",size));
        
        //System.out.println(size);
        byte[] returnBytes = Arrays.copyOfRange(bytes, startOfData, startOfData + size);
        return ByteBuffer.wrap(returnBytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Populates HashMap fileFormats with all known chunk names and codes in the .3DS specification.
     */
    private static void loadFormat() {
        fileFormats.put("Main", 0x4D4D);
        fileFormats.put("M3D Version", 0x0002);
        fileFormats.put("3D Editor", 0x3D3D);
        fileFormats.put("Object Block", 0x4000);
        fileFormats.put("Triangular Mesh", 0x4100);
        fileFormats.put("Vertices List", 0x4110);
        fileFormats.put("Faces Description", 0x4120);
        fileFormats.put("Faces Material", 0x4130);
        fileFormats.put("Smoothing Group List", 0x4150);
        fileFormats.put("Mapping Coordinates List", 0x4140);
        fileFormats.put("Local Coordinates System", 0x4160);
        fileFormats.put("Light", 0x4600);
        fileFormats.put("Spotlight", 0x4610);
        fileFormats.put("Camera", 0x4700);
        fileFormats.put("Material Block", 0xAFFF);
        fileFormats.put("Material Name", 0xA000);
        fileFormats.put("Ambient Color", 0xA010);
        fileFormats.put("Diffuse Color", 0xA020);
        fileFormats.put("Specular Color", 0xA030);
        fileFormats.put("Texture Map 1", 0xA200);
        fileFormats.put("Bump Map", 0xA230);
        fileFormats.put("Reflection Map", 0xA220);
        fileFormats.put("Mapping Filename", 0xA300);
        fileFormats.put("Mapping Parameters", 0xA351);
        fileFormats.put("KeyFramer Chunk", 0xB000);
        fileFormats.put("Mesh Information Block", 0xB002);
        fileFormats.put("Spot Light Information Block", 0xB007);
        fileFormats.put("Frames", 0xB008);
        fileFormats.put("Object Name", 0xB010);
        fileFormats.put("Object Pivot Point", 0xB013);
        fileFormats.put("Position Track", 0xB020);
        fileFormats.put("Rotation Track", 0xB021);
        fileFormats.put("Scale Track", 0xB022);
        fileFormats.put("Hierarchy Position", 0xB030);
    }
    
    public static void main(String[] args){
        File file = new File("data/dice.3ds");
        GenerateFormat chunks = new GenerateFormat(file);
        HashMap<String,Format3DS> formats = chunks.getAllFormat3DS();
        VerticesList diceVert = (VerticesList)formats.get("Vertices List");
        FacesDescription diceFace = (FacesDescription)formats.get("Faces Description");
        System.out.println("In Main");
        System.out.println("" + diceVert.getSize());
        ArrayList<scad.Vector> vertices = diceVert.parseBytes();
        System.out.println(diceVert);
        ArrayList<int[]> faces = diceFace.parseBytes();
        System.out.println(diceFace);
        System.out.println(formats.get("Local Coordinates System"));
    }
}
