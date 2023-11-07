package serverSide.sharedRegions;

import serverSide.entities.*;
import serverSide.main.*;
import clientSide.entities.*;
import clientSide.stubs.GeneralRepoStub;

/**
 * Museum (shared region)
 * 
 * This class implements the museum shared region.
 */

public class Museum {

    /**
	 * Number of entity groups requesting the shutdown
	 */
	private int nEntities;

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

    GeneralRepoStub repo;

    /**
     * Museum instantiation
     * 
     * @param repo
     * @param numRooms
     * @param canvas
     */

    public Museum(GeneralRepoStub repo, int numRooms) {
        Rooms = new int[numRooms];
        this.numRooms = numRooms;
        for (int i = 0; i < numRooms; i++) {
            Rooms[i] = 0;
        }
        this.repo = repo;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public int[] setCanvas(int[] canvas) {
        return Rooms = canvas;
    }

    /**
     * Called by the ordinary thief to roll a canvas
     * 
     * @param roomId
     * @return true if the thief has a canvas, false otherwise
     */
    public synchronized boolean rollACanvas(int roomId) {

        int thiefId = ((MuseumClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        repo.setOrdinaryThiefState(thiefId, OrdinaryThiefStates.AT_A_ROOM);
        boolean hasCanva;

        if (Rooms[roomId] == 0) {
            hasCanva = false;
            System.out.println("T" + thiefId + " didn't roll a canva at room " + roomId +" that now has " + Rooms[roomId] + " canvas");
            repo.setOrdinaryThiefCanvas(thiefId, 0, roomId);
        } else {
            Rooms[roomId]--;
            hasCanva = true;
            System.out.println("T" + thiefId + " rolled a canva at room " + roomId +" that now has " + Rooms[roomId] + " canvas");
            repo.setOrdinaryThiefCanvas(thiefId, 1, roomId);
        }

        

        return hasCanva;
    }

    /**
     * Operation to shut down the Museum server
     */

     public synchronized void shutdown(){
        ServerMuseum.waitConnection = false;
    }
}
