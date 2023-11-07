package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Concentration Site.
 *
 *     It provides the functionality to access the Concentration Site.
 */

public interface ConcentrationSiteInterface extends Remote{

    /**
     * Operation am I needed
     * It is called by the ordinary thief to know if he is needed
     * 
     * @param thiefId thief id
     * @return true if the thief is needed, false otherwise and thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public ReturnBoolean amINeeded(int thiefId) throws RemoteException;

    /**
     * Operation prepare excursion
     * It is called by the ordinary thief to prepare the excursion
     * 
     * @param thiefId thief id
     * @return assault party id and room id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int[] prepareExcursion(int thiefId) throws RemoteException;

    /**
     * Operation prepare assault party
     * It is called by the master thief to prepare the assault party
     * 
     * @param assaultPartyId assault party id
     * @param roomId room id
     * @return master thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int prepareAssaultParty(int assaultPartyId, int roomId) throws RemoteException;
    
    /** 
     * Operation sum up results
     * It is called by the master thief to sum up the results
     * 
     * @param numStolenPaintings number of stolen paintings
     * @return master thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public int sumUpResults(int numStolenPaintings) throws RemoteException;	

    /**
     * Operation shutdown
     * 
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */

    public void shutdown() throws RemoteException;
}
