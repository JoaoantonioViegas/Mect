package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Service provider agent for access to the Museum.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class MuseumClientProxy extends Thread implements OrdinaryThiefCloning {

    /**
     * Number of instantiated threads
     */

    private static int nProxy = 0;

    /**
     * Communication channel
     */

    private ServerCom sconi;

    /**
     * Interface to the Museum
     */

    private MuseumInterface museumInterface;

    /*
     * State of ordinary thief 
     */

    private int OrdinaryThiefState;

    /**
     * Ordinary thief id
     */

    private int OrdinaryThiefId;

    /**
     *  Room id
     */

    private int roomId;

    /**
     * Instantiation of a client proxy.
     */

    public MuseumClientProxy (ServerCom sconi, MuseumInterface museumInterface)
    {
        super ("Proxy_" + MuseumClientProxy.getProxyId ());

        this.sconi = sconi;
        this.museumInterface = museumInterface;
    }

    private static int getProxyId(){

        Class<?> cl = null;                                    // representation of the ClientProxyThread object in JVM
        int proxyId;                                           // identification of the proxy

        try
        { cl = Class.forName ("serverSide.entities.MuseumClientProxy");
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type MuseumClientProxy was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        synchronized (cl)
        { proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    /*
     * Set the state of the ordinary thief
     * @param state
     */

    public void setOrdinaryThiefState(int state){
        this.OrdinaryThiefState = state;
    }

    /*
     * Get the state of the ordinary thief
     * @return state
     */

    public int getOrdinaryThiefState(){
        return this.OrdinaryThiefState;
    }

    /*
     * Set the id of the ordinary thief
     * @param id
     */

    public void setOrdinaryThiefId(int id){
        this.OrdinaryThiefId = id;
    }

    /*
     * Get the id of the ordinary thief
     * @return id
     */

    public int getOrdinaryThiefId(){
        return this.OrdinaryThiefId;
    }

    /*
     * Set the room id
     * @param roomId
     */

    public void setRoomId(int roomId){
        this.roomId = roomId;
    }

    /*
     * Get the room id
     * @return roomId
     */

    public int getRoomId(){
        return this.roomId;
    }

    /*
     * Life cycle of the service provider agent.
     */

    @Override
    public void run(){
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* Service providing */
        inMessage = (Message) sconi.readObject();                      // get service request
        try{
            outMessage = museumInterface.processAndReply(inMessage);   // process it
        } catch (MessageException e){
            GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
            GenericIO.writelnString (e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject(outMessage);                                 // send service reply
        sconi.close();                                                 // close the communication channel
    }
}