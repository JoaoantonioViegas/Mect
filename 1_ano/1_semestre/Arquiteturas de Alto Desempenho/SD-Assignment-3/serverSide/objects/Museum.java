package serverSide.objects;

import java.rmi.RemoteException;
import genclass.*;
import clientSide.entities.*;
import interfaces.*;
import serverSide.main.*;

/**
 * Museum (shared region)
 * 
 * This class implements the museum shared region.
 * 
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */

public class Museum implements MuseumInterface{

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

    GeneralRepoInterface repo;

    /**
     * Museum instantiation
     * 
     * @param repo
     * @param numRooms
     * @param canvas
     */

    public Museum(GeneralRepoInterface repo, int numRooms) {
        Rooms = new int[numRooms];
        this.numRooms = numRooms;
        for (int i = 0; i < numRooms; i++) {
            Rooms[i] = 0;
        }
        this.repo = repo;
    }

    public int getNumRooms() throws RemoteException{
        return numRooms;
    }

    public void setCanvas(int[] canvas) throws RemoteException{
        Rooms = canvas;
    }

    /**
     * Called by the ordinary thief to roll a canvas
     * 
     * @param roomId
     * @return true if the thief has a canvas, false otherwise
     */
    @Override
    public synchronized boolean rollACanvas(int thiefId, int roomId) throws RemoteException{

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
    @Override
    public synchronized void shutdown() throws RemoteException{
        ServerMuseum.shutdown();
    }
    
}
