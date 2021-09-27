/*
* Factory for creating format objects
* Author: Michael Moschetti & John Anticev
*/

package using3DS;

import java.nio.ByteBuffer;

/**
 * Class that generates the Format3DS objects.
 */
class FormatFactory {
    public FormatFactory(){
        
    }
    
    /**
     * Return a Format3DS object with parsed data based on the provided code ID and ByteBuffer.
     * @param code Chunk ID.
     * @param data ByteBuffer belonging to the chunk.
     * @return Format3DS object of the appropriate type.
     */
    public Format3DS getFormatFactory(int code, ByteBuffer data) {
        switch(code) {
            case 0x4D4D:
                return null;
            case 0x0002:
                return null;
            case 0x3D3D:
                return null;
            case 0x4000:
                return null;
            case 0x4100:
                return null;
            case 0x4110:
                return new VerticesList(data);
            case 0x4120:
                return new FacesDescription(data);
            case 0x4130:
                return null;
            case 0x4150:
                return null;
            case 0x4140:
                return new MappingCoordinates(data);
            case 0x4160:
                return new LocalCoordinatesSystem(data);
            case 0x4600:
                return null;
            case 0x4610:
                return null;
            case 0x4700:
                return null;
            case 0xAFFF:
                return null;
            case 0xA000:
                return null;
            case 0xA010:
                return null;
            case 0xA020:
                return null;
            case 0xA030:
                return null;
            case 0xA200:
                return null;
            case 0xA230:
                return null;
            case 0xA220:
                return null;
            case 0xA300:
                return null;
            case 0xA351:
                return null;
            case 0xB000:
                return null;
            case 0xB002:
                return null;
            case 0xB007:
                return null;
            case 0xB008:
                return null;
            case 0xB010:
                return null;
            case 0xB013:
                return null;
            case 0xB020:
                return null; 
            case 0xB021:
                return null; 
            case 0xB022:
                return null;
            case 0xB030:
                return null;
            default:
                return null;
        }
    }
}
