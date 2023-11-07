package interfaces;

import java.io.Serializable;

public class ReturnChar implements Serializable{

    /**
     * Serialization key
     */

    private static final long serialVersionUID = 2023L;

    /**
     * Char value
     */

    private char value;

    /**
     * Integer state value
     */

    private int state;

    /**
     * ReturnChar instantiation
     * @param value
     * @param state
     */

    public ReturnChar(char value, int state) {
        this.value = value;
        this.state = state;
    }

    /**
     * Get char value
     * @return char value
     */

    public char getValue() {
        return value;
    }

    /**
     * Get state value
     * @return state value
     */

    public int getState() {
        return state;
    }
}
