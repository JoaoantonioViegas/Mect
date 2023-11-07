package sharedRegions;

import main.*;
import entities.*;
import commInfra.*;

/**
 * Collection Site (shared region)
 * 
 * This class implements the collection site shared region.
 */

public class ControlCollectionSite {
    /**
     * Number of stolen paintings
     */
    private int numStolenPaintings;

    /**
     * Reference to the rooms that still have paintings
     */

    private boolean[] roomsWithPaintings;

    /**
     * Reference to the number of paintings in each room
     */

    private int[] numPaintingsRoom;

    /**
     * Reference to the thieves that have a canvas
     */

    private boolean[] thievesWithCanvas;

    /**
     * Waiting thiefs
     */

    private MemFIFO<Integer> waitingThieves;

    /**
     * Reference to the rooms where each thief is
     */

    private int[] thiefRoom;

    /**
     * Reference to the assault party where each thief is
     */

    private int[] thiefAssaultParty;

    /**
     * Reference to the General Repository of Information
     */

    private GeneralRepo generalRepo;

    /**
     * Reference to the assault party in operation (number of thieves in the assault
     * party)
     */

    private int[] numAssaultPartyThieves;

    /**
     * Reference to the assault party target room
     */

    private int[] assaultPartyTargetRoom;

    /*
     * Reference to the rooms occupied
     */

    private boolean[] roomsOccupied;

    /**
     * Collection Site instantiation
     * 
     * @param generalRepo reference to the general repository of information
     */

    public ControlCollectionSite(GeneralRepo generalRepo) {
        this.generalRepo = generalRepo;
        this.numStolenPaintings = 0;
        numAssaultPartyThieves = new int[2];
        numAssaultPartyThieves[0] = 0;
        numAssaultPartyThieves[1] = 0;

        assaultPartyTargetRoom = new int[2];
        assaultPartyTargetRoom[0] = -1;
        assaultPartyTargetRoom[1] = -1;
        roomsWithPaintings = new boolean[SimulPar.N];

        numPaintingsRoom = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++) {
            numPaintingsRoom[i] = 0;
        }

        this.roomsOccupied = new boolean[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++) {
            roomsOccupied[i] = false;
        }

        for (int i = 0; i < SimulPar.N; i++) {
            roomsWithPaintings[i] = true;
        }

        try {
            waitingThieves = new MemFIFO<Integer>(SimulPar.M - 1);
        } catch (MemException e) {
            System.err.println("MemException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        thievesWithCanvas = new boolean[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            thievesWithCanvas[i] = false;
        }

        thiefRoom = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            thiefRoom[i] = -1;
        }

        thiefAssaultParty = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            thiefAssaultParty[i] = -1;
        }
    }

    /*
     * Called by master thief
     * It is called by the master thief to start the operation
     */

    public synchronized void startOfOperation() {
        ((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Operation to hand a canvas to the master thief
     * It is called by the ordinary thieve when he has a canvas to hand over to the
     * master thief
     */

    public synchronized void handACanvas(boolean hasCanvas, int assaultPartyId, int roomId) {
        OrdinaryThief currentThief = (OrdinaryThief) Thread.currentThread();
        int thiefId = currentThief.getThiefId();
        thiefRoom[thiefId] = roomId;
        thiefAssaultParty[thiefId] = assaultPartyId;
        thievesWithCanvas[thiefId] = hasCanvas; // set the thief to have a canvas

        // Set the state of the ordinary thief to WAITING_TO_HAND_A_CANVAS ...
        currentThief.setThiefState(OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS);
        generalRepo.setOrdinaryThiefState(thiefId, OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS);

        try {
            waitingThieves.push(thiefId); // insert the thief into the waiting FIFO to hand over the canvas
        } catch (MemException e) {
            System.exit(1);
        }

        // notify the master thief that there is a thief waiting to hand over a canvas
        notifyAll();

        // wait while the master thief is not ready to collect the canvas
        while (thiefAssaultParty[thiefId] != -1) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Operation to collect a canvas from the ordinary thief
     * It is called by the master thief when he is ready to collect a canvas from
     * the ordinary thief
     */

    public synchronized void collectACanvas() {

        int thiefId = waitingThieves.pop(); // remove the thief from the waiting FIFO to hand over the canvas
        int roomId = thiefRoom[thiefId];
        int assaultPartyId = thiefAssaultParty[thiefId];

        thiefRoom[thiefId] = -1;
        thiefAssaultParty[thiefId] = -1;

        boolean hasCanvas = thievesWithCanvas[thiefId]; // get the thief to have a canvas

        if (hasCanvas) {
            numStolenPaintings++; // increment the number of stolen paintings
            numPaintingsRoom[roomId]++; // increment the number of paintings in the room
            thievesWithCanvas[thiefId] = false; // set the thief to not have a canvas
        } else {
            roomsWithPaintings[roomId] = false; // set the room to have a canvas
            thievesWithCanvas[thiefId] = false; // set the thief to not have a canvas
        }

        numAssaultPartyThieves[assaultPartyId]--;
        if (numAssaultPartyThieves[assaultPartyId] == 0) {
            roomsOccupied[roomId] = false;
            generalRepo.setAssaultPartyRoom(assaultPartyId, -1);
        }

        notifyAll(); // notify the thief that he can hand over the canvas
        // set the state of the master thief to COLLECTING_A_CANVAS
        ((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        generalRepo.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Function that returns a room that still has paintings and is not occupied
     * 
     * @return
     */

    public int getRoomWithCanvasID() {
        for (int i = 0; i < SimulPar.N; i++) {
            if (roomsWithPaintings[i] && roomsOccupied[i] == false) {
                roomsOccupied[i] = true;
                return i;
            }
        }
        return -1;
    }

    /**
     * Function that returns an assault party that is not occupied
     * 
     * @return
     */

    public int getAssaultPartyID() {
        if (numAssaultPartyThieves[0] == 0) {
            numAssaultPartyThieves[0] = 3;
            return 0;
        } else if (numAssaultPartyThieves[1] == 0) {
            numAssaultPartyThieves[1] = 3;
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Called by the master thief to appraise the situation
     * If he has enough thieves to form an assault party, he will form one
     * If he has stolen all the paintings, he will terminate the operation
     * If he has no thieves to form an assault party, he will take a rest
     * 
     * @return the operation to be performed
     */

    public synchronized char appraiseSit() {
        ((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        generalRepo.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        boolean inOperation = false;
        for (int i = 0; i < roomsWithPaintings.length; i++) {
            if (roomsWithPaintings[i]) {
                inOperation = true;
            }
        }

        int roomId = -1;
        for (int i = 0; i < SimulPar.N; i++) {
            if (roomsWithPaintings[i] && roomsOccupied[i] == false) {
                roomId = i;
            }
        }

        if (!inOperation && numAssaultPartyThieves[0] == 0 && numAssaultPartyThieves[1] == 0) {
            return 'E';
        }

        if (roomId != -1 && (numAssaultPartyThieves[0] == 0 || numAssaultPartyThieves[1] == 0)) {
            return 'P';
        }

        return 'R';
    }

    /*
     * Called by master thief
     * He will take a rest until there is a thief waiting to hand over a canvas
     */

    public synchronized void takeARest() {

        // set the state of the master thief to WAITING_FOR_GROUP_ARRIVAL

        ((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);
        generalRepo.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);

        while (waitingThieves.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.exit(1);
            }
        }
    }

    /**
     * Called by the master thief to get the number of stolen paintings
     * 
     * @return the number of stolen paintings
     */

    public synchronized int getCollectedCanvas() {
        return numStolenPaintings;
    }

}