package sharedRegions;

import entities.*;

/**
 * Museum (shared region)
 * 
 * This class implements the museum shared region.
 */

public class Museum {

    /**
     * Reference to the number of rooms
     */

    private int numRooms;

    /**
     * Reference to the number of canvas in each room
     */

    private int[] Rooms;

    /**
     * Reference to the General Repository
     */

    GeneralRepo repo;

    /**
     * Museum instantiation
     * 
     * @param repo
     * @param numRooms
     * @param canvas
     */

    public Museum(GeneralRepo repo, int numRooms, int[] canvas) {
        Rooms = new int[numRooms];
        this.numRooms = numRooms;
        for (int i = 0; i < numRooms; i++) {
            Rooms[i] = canvas[i];
        }
        this.repo = repo;
    }

    public int getNumRooms() {
        return numRooms;
    }

    /**
     * Called by the ordinary thief to roll a canvas
     * 
     * @param roomId
     * @return true if the thief has a canvas, false otherwise
     */
    public synchronized boolean rollACanvas(int roomId) {

        int thiefId = ((OrdinaryThief) Thread.currentThread()).getThiefId();
        repo.setOrdinaryThiefState(thiefId, OrdinaryThiefStates.AT_A_ROOM);
        boolean hasCanva;

        if (Rooms[roomId] == 0) {
            hasCanva = false;
            repo.setOrdinaryThiefCanvas(thiefId, 0, roomId);
        } else {
            Rooms[roomId]--;
            hasCanva = true;
            repo.setOrdinaryThiefCanvas(thiefId, 1, roomId);
        }

        return hasCanva;
    }
}
