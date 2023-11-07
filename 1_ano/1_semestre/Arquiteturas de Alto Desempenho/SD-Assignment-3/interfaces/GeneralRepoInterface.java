package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type General Repository.
 *
 *     It provides the functionality to access the General Repository.
 */

public interface GeneralRepoInterface extends Remote {

    /**
     * Report the initial distance to rooms
     * 
     * @param distanceToRoom array of distance to rooms
     */

    public void reportInitialDistances(int[] distanceToRoom) throws RemoteException;

    /**
     * Reporting the initial number of canvas in the rooms
     * 
     * @param canvasInRoom array of canvas in the rooms
     */

    public void reportRoomNumCanvas(int[] canvasInRoom) throws RemoteException;

    /**
     * Reporting the initial thieves agility
     * 
     * @param agility array of thieves agility
     */

    public void reportThievesAgility(int[] agility) throws RemoteException;

    /**
     * Set the state of the master thief
     * 
     * @param state
     */

    public void setMasterThiefState(int state) throws RemoteException;

    /**
     * Set the state of the ordinary thief
     * 
     * @param thiefID
     * @param state
     */

    public void setOrdinaryThiefState(int thiefID, int state) throws RemoteException;

    /**
     * Set ordinary thieves max displacement
     * @param maxDisplacement array of agilities
     */

    public void setOrdinaryThiefMaxDisplacement(int maxDisplacement[]) throws RemoteException;

    /**
     * Set distance to rooms
     * @param distanceToRoom
     */

    public void setDistanceToRoom(int distanceToRoom[]) throws RemoteException;

    /**
     * Set the number of paintings in the room
     * @param paintingsInRoom
     */

    public void setPaintingsInRoom(int paintingsInRoom[]) throws RemoteException;

    /**
     * Set the situation of the ordinary thief
     * 
     * @param thiefID
     * @param situation
     */

    public void setOrdinaryThiefSituation(int thiefID, char situation) throws RemoteException;
    
    /**
     * Set the position of the ordinary thief
     * 
     * @param thiefID
     * @param position
     */

    public void setOrdinaryThiefPosition(int thiefID, int position) throws RemoteException;

    /**
     * Set the canvas of the ordinary thief and the number of paintings in the room
     * 
     * @param thiefID
     * @param canvas
     * @param roomID
     */

    public void setOrdinaryThiefCanvas(int thiefID, int canvas, int roomID) throws RemoteException;

    /**
     * Sets all the variables of the ordinary thief
     * 
     * @param thiefID
     * @param state
     * @param situation
     * @param assaultPartyID
     * @param roomID
     * @param position
     * @param canvas
     */

    public void setOrdinaryAll(int thiefID, int state, char situation, int assaultPartyID, int roomID,
            int position, int canvas) throws RemoteException;

    /**
     * Reset all the variables of the ordinary thief
     * 
     * @param thiefID
     * @param state
     */

    public void resetOrdinaryAll(int thiefID, int state) throws RemoteException;

    /**
     * Set the room of the assault party
     * 
     * @param assaultPartyID
     * @param roomID
     */

    public void setAssaultPartyRoom(int assaultPartyID, int roomID) throws RemoteException;

    /**
     * End the assault
     * Prints the number of stolen canvas and the legend
     */

    public void endAssault() throws RemoteException;

    /**
     * Operation to shut down the AssaultParty server
     */

     public void shutdown() throws RemoteException;
}
