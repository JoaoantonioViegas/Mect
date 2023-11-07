package clientSide.entities;

/**
 * Definition of the internal states of the thief during his life cycle.
 */

public final class OrdinaryThiefStates {
    /**
     * The state to wait until needed.
     */

    public static final int WAITING_UNTIL_NEEDED = 0;

    /**
     * The state to steal a canvas.
     */

    public static final int AT_A_ROOM = 1;

    /**
     * The state to wait for the master thief to decide what to do next.
     */

    public static final int CRAWLING_INWARDS = 2;

    /**
     * The state to wait for the master thief to decide what to do next.
     */

    public static final int CRAWLING_OUTWARDS = 3;

    /**
     * The state to wait to hand a canvas.
     */

    public static final int WAITING_TO_HAND_A_CANVAS = 4;

    /**
     * The state of end of operations.
     */

    public static final int END_OF_OPERATIONS = 5;

    /**
     * The state of the thief that is in the Assault Party and is waiting to crawl
     * in
     */

    public static final int WAITING_FOR_CRAWL_IN = 6;

    /**
     * The state of the thief that is in the Assault Party and is waiting to crawl
     * out
     */

    public static final int WAITING_FOR_CRAWL_OUT = 7;

    private OrdinaryThiefStates() {
    }
}
