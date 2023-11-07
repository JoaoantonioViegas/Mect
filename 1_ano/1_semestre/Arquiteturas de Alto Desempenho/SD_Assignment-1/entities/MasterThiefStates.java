package entities;

public final class MasterThiefStates
{
    /**
     *  Initial state.
     */

    public static final int PLANNING_THE_HEIST = 0;

    /**
     *  The transitional state
     */

    public static final int DECIDING_WHAT_TO_DO = 1;

    /**
     *  The blocking state to the master thief to assemble a group
     */

    public static final int ASSEMBLING_A_GROUP = 2;

    /**
     * The blocking state to the master thief to wait for handACanvas
     */
     
    public static final int WAITING_FOR_GROUP_ARRIVAL = 3;

    /**
     * The final state to present the report
     */

    public static final int PRESENTING_THE_REPORT = 4;


    /**
     * Cant be instantiated
     */
    private MasterThiefStates ()
    { }

}
    