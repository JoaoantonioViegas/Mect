package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Assault Party.
 *
 *     It provides the functionality to access the Assault Party.
 */

public interface AssaultPartyInterface extends Remote{

    /**
     * Operation set room distances
     * 
     * @param distanceToRoom array of distances to the rooms
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void setDistanceToRoom(int[] distanceToRoom) throws RemoteException;

    /**
     * Operation crawl in
     * It is called by the ordinary thief to crawl in
     * 
     * @param thiefId thief id
     * @param roomId room id
     * @param assaultId assault party id
     * @param agility thief agility
     * @return thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int crawlIn(int thiefId, int roomId, int assaultId, int agility) throws RemoteException;

    /**
     * Operation crawl out
     * It is called by the ordinary thief to crawl out
     * 
     * @param thiefId thief id
     * @param roomId room id
     * @param assaultId assault party id
     * @param agility thief agility
     * @return thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int crawlOut(int thiefId, int roomId, int assaultId, int agility) throws RemoteException;

    /**
     * Operation send assault party
     * It is called by the master thief to send the assault party
     * 
     * @param assaultId assault party id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void sendAssaultParty(int assaultId) throws RemoteException;

    /**
     * Operation reverse direction
     * It is called by the ordinary thief to reverse direction
     * 
     * @param thiefId thief id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int reverseDirection(int thiefId) throws RemoteException;

    /**
     * Operation shut down
     * 
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void shutdown() throws RemoteException;

    
}
