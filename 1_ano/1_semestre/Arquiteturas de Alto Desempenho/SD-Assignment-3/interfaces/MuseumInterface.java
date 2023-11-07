package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Musuem.
 *
 *     It provides the functionality to access the Musuem.
 */

public interface MuseumInterface extends Remote {

    /**
     *  Set the number of canvas in each room
     *
     *    @param canvas number of canvas in each room
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void setCanvas(int[] canvas) throws RemoteException;

    /**
     *  Operation roll a canvas
     *  It is called by ther ordinary thief to roll a canvas
     * 
     *    @param thiefId thief id
     *    @param roomId room id
     *    @return number of canvas in each room
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public boolean rollACanvas(int thiefId, int roomId) throws RemoteException;

    /**
     *  Operation shutdown
     *
     *    @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void shutdown() throws RemoteException;

}