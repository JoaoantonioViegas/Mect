package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Service provider agent for access to the Assault Party.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class AssaultPartyClientProxy extends Thread implements OrdinaryThiefCloning, MasterThiefCloning {
    /**
     * Number of instantiated threads
     */

    private static int nProxy = 0;

    /**
     * Communication channel
     */

    private ServerCom sconi;

     
    /**
    * Interface to the AssaultParty
    */

    private AssaultPartyInterface assaultPartyInterface;

    /**
     * Ordinary thief state
     */

    private int ordinaryThiefState;

     /**
      * Ordinary thief id
      */
 
    private int ordinaryThiefId;

    /**
     * Ordinary thief assault party
    */

    private int ordinaryThiefAssaultParty;
 
     /**
      * Master thief state
      */
 
    private int masterThiefState;

    /**
     * Ordinary thief agility
     */

    private int ordinaryThiefAgility;

    /**
     * Instantiation of a client proxy.
     */

    public AssaultPartyClientProxy (ServerCom sconi, AssaultPartyInterface assaultPartyInterface){
        
        super ("Proxy_" + AssaultPartyClientProxy.getProxyId ());

        this.sconi = sconi;
        this.assaultPartyInterface = assaultPartyInterface;
    }

    private static int getProxyId(){

        Class<?> cl = null;                                    // representation of the ClientProxyThread object in JVM
        int proxyId;                                           // identification of the proxy

        try
        { cl = Class.forName ("serverSide.entities.AssaultPartyClientProxy");
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type AssaultPartyClientProxy was not found!");
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
     * Set ordinary thief state
     * 
     * @param state state of ordinary thief
     */

     public void setOrdinaryThiefState(int state){
        this.ordinaryThiefState = state;
    }

    /*
     * Get ordinary thief state
     * 
     * @return state of ordinary thief
     */

    public int getOrdinaryThiefState(){
        return this.ordinaryThiefState;
    }

    /*
     * Set ordinary thief id
     * 
     * @param id id of ordinary thief
     */

    public void setOrdinaryThiefId(int id){
        this.ordinaryThiefId = id;
    }

    /*
     * Get ordinary thief id
     * 
     * @return id of ordinary thief
     */

    public int getOrdinaryThiefId(){
        return this.ordinaryThiefId;
    }

    /*
     * Set master thief state
     * 
     * @param state state of master thief
     */

    public void setMasterThiefState(int state){
        this.masterThiefState = state;
    }

    /*
     * Get master thief state
     * 
     * @return state of master thief
     */

    public int getMasterThiefState(){
        return this.masterThiefState;
    }

    /*
     * Set ordinary thief agility
     * 
     * @param agility agility of ordinary thief
     */

    public void setOrdinaryThiefAgility(int agility){
        this.ordinaryThiefAgility = agility;
    }

    /*
     * Get ordinary thief agility
     * 
     * @return agility of ordinary thief
     */

    public int getOrdinaryThiefAgility(){
        return this.ordinaryThiefAgility;
    }

    /*
     * Set ordinary thief assault party
     * 
     * @param assaultParty assault party of ordinary thief
     */

    public void setOrdinaryThiefAssaultParty(int assaultParty){
        this.ordinaryThiefAssaultParty = assaultParty;
    }

    /*
     * Get ordinary thief assault party
     * 
     * @return assault party of ordinary thief
     */

    public int getOrdinaryThiefAssaultParty(){
        return this.ordinaryThiefAssaultParty;
    }

    /**
     *  Life cycle of the service provider agent.
     */

    @Override
    public void run ()
    {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        inMessage = (Message) sconi.readObject ();                     // get service request
        try
        { outMessage = assaultPartyInterface.processAndReply (inMessage);         // process it
        }
        catch (MessageException e)
        { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
            GenericIO.writelnString (e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
