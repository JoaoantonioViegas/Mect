package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Control Site.
 *
 *     It provides the functionality to access the Control Site.
 */

public interface ControlSiteInterface extends Remote{

    /**
     * Operation start of operations
     * It is called by the master thief to start the operations
     * 
     * @return state of the master thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int startOfOperation() throws RemoteException;

    /**
     * Operation hand a canvas
     * It is called by the ordinary thief to hand a canvas to the master thief
     * 
     * @param thiefId id of the thief
     * @param hasCanvas true if the thief has a canvas, false otherwise
     * @param assaultPartyId assaultParty id of the thief
     * @param roomId room id of the thief
     * @return state of the ordinary thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int handACanvas(int thiefId, boolean hasCanvas, int assaultPartyId, int roomId) throws RemoteException;
    
    /**
     * Operation collect a canvas
     * It is called by the master thief to collect a canvas from the ordinary thief
     * 
     * @return state of the master thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int collectACanvas() throws RemoteException;

    /**
     * Operation get room id with canvas
     * It is called by the master thief to get the room id with canvas
     * 
     * @return room id with canvas
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int getRoomWithCanvasID() throws RemoteException;

    /**
     * Operation get assault party id that is free
     * 
     * @return assault party id that is free
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int getAssaultPartyID() throws RemoteException;

    /**
     * Operation appraise situation
     * It is called by the master thief to appraise the situation
     * 
     * @return char with the decision of the master thief and master thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public ReturnChar appraiseSit() throws RemoteException;

    /**
     * Operation take a rest
     * It is called by the master thief to take a rest
     * 
     * @return state of the master thief
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int takeARest() throws RemoteException;

    /**
     * Operation get number of collected canvas
     * 
     * @return number of collected canvas
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int getCollectedCanvas() throws RemoteException;

    /**
     * Operation shutdown
     * 
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void shutdown() throws RemoteException;

}
