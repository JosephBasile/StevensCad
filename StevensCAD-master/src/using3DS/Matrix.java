/*
* Matrix class to hold 3DS Transitional Matrix
* Author: Michael Moschetti & John Anticev
*/
package using3DS;

/**
 * Class that holds the 4x4 Transitional Matrix data from the Local Cordinates System.
 */
class Matrix {
    
    private float[] values;

    Matrix() {
        values = new float[16];
        // The right column of the transitional matrix is (0, 0, 0, 1).
        values[3] = 0;
        values[7] = 0;
        values[11] = 0;
        values[15] = 1;
    }

    /**
     * Adds a value to the matrix.
     * @param row Row for the value.
     * @param column Column for the value;
     * @param val Value to store.
     */
    public void put(int row, int column, float val) {
        values[column + row * 4] = val;
    }
    
    /**
     * Return the value at the given index.
     * @return Float value at that index.
     */
    public float get(int x, int y) {
        return values[y + x * 4];
    }

    /**
     * Print the matrix.
     * @return String object representing the 4x4 matrix.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String c;
        for(int i = 1; i < values.length + 1; i++) {
            c = (i % 4 == 0) ? ("\n") : (",");
            sb.append(values[i - 1] + c);
        }
        return sb.toString();
    }
}
