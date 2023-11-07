package serverSide.objects;

import java.rmi.RemoteException;
import genclass.*;
import clientSide.entities.*;
import interfaces.*;
import serverSide.main.*;
import commInfra.*;

/**
 * ControlSite (shared region)
 * 
 * This class implements the ControlSite shared region.
 * 
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ControlSite implements ControlSiteInterface{

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

    private GeneralRepoInterface generalRepo;

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
     * Reference to the current assault party id
     */

    private int currentAssaultParty;

    /**
     * Reference to the current assault party room
     */

    private int currentAssaultPartyRoom;

    /**
     * Collection Site instantiation
     * 
     * @param generalRepo reference to the general repository of information
     */

    public ControlSite(GeneralRepoInterface generalRepo) {
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

    /**
     * Operation start of operations
     * It is called by the master thief to start the operations
     * 
     * @return state of the master thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int startOfOperation() throws RemoteException {
        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }

    /**
     * Operation hand a canvas
     * It is called by the ordinary thief to hand a canvas to the master thief
     * 
     * @param hasCanvas true if the thief has a canvas, false otherwise
     * @param thiefId assaultParty id of the thief
     * @param roomId room id of the thief
     * @return state of the ordinary thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int handACanvas(int thiefId, boolean hasCanvas, int assaultPartyId, int roomId) throws RemoteException {
        thiefRoom[thiefId] = roomId;
        thiefAssaultParty[thiefId] = assaultPartyId;
        thievesWithCanvas[thiefId] = hasCanvas; // set the thief to have a canvas

        // Set the state of the ordinary thief to WAITING_TO_HAND_A_CANVAS ...
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
        return OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS;
    }

    /**
     * Operation collect a canvas
     * It is called by the master thief to collect a canvas from the ordinary thief
     * 
     * @return state of the master thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int collectACanvas() throws RemoteException {

        int thiefId = waitingThieves.pop(); // remove the thief from the waiting FIFO to hand over the canvas
        int roomId = thiefRoom[thiefId];
        int assaultPartyId = thiefAssaultParty[thiefId];

        System.out.println("Collecting canvas of T"+thiefId+" from room " + roomId + " of assault party " + assaultPartyId);

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
        generalRepo.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);

        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }

    /**
     * Operation get room id with canvas
     * It is called by the master thief to get the room id with canvas
     * 
     * @return room id with canvas
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public int getRoomWithCanvasID() throws RemoteException{
        for (int i = 0; i < SimulPar.N; i++) {
            if (roomsWithPaintings[i] && roomsOccupied[i] == false) {
                roomsOccupied[i] = true;
                this.currentAssaultPartyRoom = i;
                return i;
            }
        }
        return -1;
    }

    /**
     * Function to get the current assault party room
     * @return current assault party room
     */

    public int getCurrentAssaultPartyRoom() {
        return this.currentAssaultPartyRoom;
    }

    /**
     * Function to get the current assault party
     * @return current assault party
     */

    public int getCurrentAssaultParty() {
        return this.currentAssaultParty;
    }

    /**
     * Operation get assault party id that is free
     * 
     * @return assault party id that is free
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public int getAssaultPartyID() throws RemoteException{
        if (numAssaultPartyThieves[0] == 0) {
            numAssaultPartyThieves[0] = 3;
            this.currentAssaultParty = 0;
            return 0;
        } else if (numAssaultPartyThieves[1] == 0) {
            numAssaultPartyThieves[1] = 3;
            this.currentAssaultParty = 1;
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Operation appraise situation
     * It is called by the master thief to appraise the situation
     * 
     * @return char with the decision of the master thief and master thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public ReturnChar appraiseSit() throws RemoteException{
        int state = MasterThiefStates.DECIDING_WHAT_TO_DO;
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
            return new ReturnChar('E', state);
        }

        if (roomId != -1 && (numAssaultPartyThieves[0] == 0 || numAssaultPartyThieves[1] == 0)) {
            return new ReturnChar('P', state);
        }

        return new ReturnChar('R', state);
    }

    /**
     * Operation take a rest
     * It is called by the master thief to take a rest
     * 
     * @return state of the master thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int takeARest() throws RemoteException {

        System.out.println("Master Thief is taking a rest");

        // set the state of the master thief to WAITING_FOR_GROUP_ARRIVAL
        generalRepo.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);

        while (waitingThieves.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.exit(1);
            }
        }
        return MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL;
    }

    /**
     * Operation get number of collected canvas
     * 
     * @return number of collected canvas
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int getCollectedCanvas() throws RemoteException {
        return numStolenPaintings;
    }

    /**
     * Operation shutdown
     * 
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void shutdown() throws RemoteException{
        ServerControlSite.shutdown();
    }
}
