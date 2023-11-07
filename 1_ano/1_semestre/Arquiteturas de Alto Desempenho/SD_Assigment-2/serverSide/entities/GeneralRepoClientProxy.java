package serverSide.entities;

import serverSide.sharedRegions.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Service provider agent for access to the General Repository of Information.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralRepoClientProxy extends Thread{
    /**
	 *  Number of instantiated threads.
	 */
	private static int nProxy = 0;

	/**
	 *  Communication channel.
	 */
	private ServerCom sconi;

	/**
	 *  Interface to the General Repository of Information.
	 */
	private GeneralRepoInterface reposInter;

    /**
     * Master Thief state
     */

    private int masterThiefState;

    /**
     * Ordinary Thief state
     */

    private int ordinaryThiefState;

    /**
     * Ordinary Thief situation
     */

    private char ordinaryThiefSituation;

    /**
     * Ordinary Thief Canvas
     */

    private int ordinaryThiefCanvas;

    /**
     * Ordinary Thief ID
     */

    private int ordinaryThiefID;

    /**
     * Ordinary Thief Agility
     */

    private int ordinaryThiefAgility;

    /**
     * Ordinary Thief Room ID
     */

    private int ordinaryThiefRoomID;

    /**
     * Ordinary Thief Assault Party ID
     */

    private int ordinaryThiefAssaultPartyID;

    /**
     * Instantiation of a client proxy.
     * 
     * @param sconi communication channel
     * @param reposInter interface to the General Repository of Information
     */

    public GeneralRepoClientProxy(ServerCom sconi, GeneralRepoInterface reposInter) {
        super("Proxy_" + GeneralRepoClientProxy.getProxyId());
        this.sconi = sconi;
        this.reposInter = reposInter;
    }

    /**
	 *  Generation of the instantiation identifier.
	 *
	 *     @return instantiation identifier
	 */
	private static int getProxyId () {
		Class<?> cl = null;		// representation of the GeneralReposClientProxy object in JVM
		int proxyId;				// instantiation identifier

		try
		{ cl = Class.forName ("serverSide.entities.GeneralRepoClientProxy");
		}
		catch (ClassNotFoundException e)
		{ GenericIO.writelnString ("Data type GeneralRepoClientProxy was not found!");
		e.printStackTrace ();
		System.exit (1);
		}
		synchronized (cl)
		{ proxyId = nProxy;
		nProxy += 1;
		}
		return proxyId;
	}

    /**
     * Set Master Thief State
     */

    public void setMasterThiefState(int masterThiefState){
        this.masterThiefState = masterThiefState;
    }

    /**
     * Set Ordinary Thief State
     */

    public void setOrdinaryThiefState(int ordinaryThiefState){
        this.ordinaryThiefState = ordinaryThiefState;
    }

    /**
     * Set Ordinary Thief Situation
     */

    public void setOrdinaryThiefSituation(char ordinaryThiefSituation){
        this.ordinaryThiefSituation = ordinaryThiefSituation;
    }

    /**
     * Set Ordinary Thief Canvas
     */

    public void setOrdinaryThiefCanvas(int ordinaryThiefCanvas){
        this.ordinaryThiefCanvas = ordinaryThiefCanvas;
    }

    /**
     * Set Ordinary Thief ID
     */

    public void setOrdinaryThiefID(int ordinaryThiefID){
        this.ordinaryThiefID = ordinaryThiefID;
    }

    /**
     * Set Ordinary Thief Agility
     */

    public void setOrdinaryThiefAgility(int ordinaryThiefAgility){
        this.ordinaryThiefAgility = ordinaryThiefAgility;
    }

    /**
     * Set Ordinary Thief Room ID
     */

    public void setOrdinaryThiefRoomID(int ordinaryThiefRoomID){
        this.ordinaryThiefRoomID = ordinaryThiefRoomID;
    }

    /**
     * Set Ordinary Thief Assault Party ID
     */

    public void setOrdinaryThiefAssaultPartyID(int ordinaryThiefAssaultPartyID){
        this.ordinaryThiefAssaultPartyID = ordinaryThiefAssaultPartyID;
    }

    /**
     * Get Master Thief State
     */

    public int getMasterThiefState(){
        return this.masterThiefState;
    }

    /**
     * Get Ordinary Thief State
     */

    public int getOrdinaryThiefState(){
        return this.ordinaryThiefState;
    }

    /**
     * Get Ordinary Thief Situation
     */

    public char getOrdinaryThiefSituation(){
        return this.ordinaryThiefSituation;
    }

    /**
     * Get Ordinary Thief Canvas
     */

    public int getOrdinaryThiefCanvas(){
        return this.ordinaryThiefCanvas;
    }

    /**
     * Get Ordinary Thief ID
     */

    public int getOrdinaryThiefID(){
        return this.ordinaryThiefID;
    }

    /**
     * Get Ordinary Thief Agility
     */

    public int getOrdinaryThiefAgility(){
        return this.ordinaryThiefAgility;
    }

    /**
     * Get Ordinary Thief Room ID
     */

    public int getOrdinaryThiefRoomID(){
        return this.ordinaryThiefRoomID;
    }

    /**
     * Get Ordinary Thief Assault Party ID
     */

    public int getOrdinaryThiefAssaultPartyID(){
        return this.ordinaryThiefAssaultPartyID;
    }

    /**
     *  Life cycle of the service provider agent.
     */

    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        inMessage = (Message) sconi.readObject();                      // get service request
        try {
            outMessage = reposInter.processAndReply(inMessage);        // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                 // send service reply
        sconi.close();                                                 // close the communication channel
    }

}
