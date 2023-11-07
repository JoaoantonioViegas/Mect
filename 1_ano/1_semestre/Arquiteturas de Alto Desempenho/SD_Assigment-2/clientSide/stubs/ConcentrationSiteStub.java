package clientSide.stubs;

import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Concentration Site (shared region)
 * 
 * This class implements the concentration site shared region.
 */

public class ConcentrationSiteStub {

    /**
     * Name of the platform where is located the Museum server
     */

     private String serverHostName;

     /**
      * Number of the port where the server is listening
      */
 
     private int serverPortNumb;

    /**
     * ConcentrationSite constructor
     * 
     * @param generalRepo
     */

    public ConcentrationSiteStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Called by the ordinary thief
     * 
     * @return true if the thief is needed, false otherwise
     */

    public  boolean amINeeded() {

        System.out.println(((OrdinaryThief) Thread.currentThread()).getThiefId() + " amINeeded");

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        System.out.println(((OrdinaryThief) Thread.currentThread()).getThiefId() + " sending amINeeded message to ConcentrationSite with state: " + ((OrdinaryThief) Thread.currentThread()).getThiefState());

        outMessage = new Message(MessageType.AMINEEDED, ((OrdinaryThief) Thread.currentThread()).getThiefId(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        com.writeObject(outMessage);
        
        inMessage = (Message) com.readObject();

        //Validate response
        if (inMessage.getMsgType() != MessageType.ISNEEDED && inMessage.getMsgType() != MessageType.ENDOP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type! " + inMessage.getMsgType ());
			GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getThiefId() != ((OrdinaryThief) Thread.currentThread()).getThiefId()) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief id! " + inMessage.getThiefId ());
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefState() != OrdinaryThiefStates.WAITING_UNTIL_NEEDED && inMessage.getThiefState() != OrdinaryThiefStates.CRAWLING_INWARDS){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getThiefState());
        return inMessage.getThiefIsNeeded();


    }

    /**
     * Called by the ordinary thief
     * The thief waits for the other thieves of the party
     * If the thief is the last one to arrive, he wakes up the master thief
     * 
     * @return the assault party id and the room id
     */

    public  int[] prepareExcursion() {
        
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        System.out.println(((OrdinaryThief) Thread.currentThread()).getThiefId() + " sending prepareExcursion message to ConcentrationSite with state: " + ((OrdinaryThief) Thread.currentThread()).getThiefState());

        outMessage = new Message(MessageType.PREPAREEXC, ((OrdinaryThief) Thread.currentThread()).getThiefId(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //Validate response
        if(inMessage.getMsgType() != MessageType.READYTOGO){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type! " + inMessage.getMsgType());
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefId() != ((OrdinaryThief) Thread.currentThread()).getThiefId()){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

        System.out.println(((OrdinaryThief) Thread.currentThread()).getThiefId() + " received prepareExcursion with assaultPartyID: " + inMessage.getThiefAssaultPartyId() + " and roomID: " + inMessage.getThiefRoomId());

        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getThiefState());

        return new int[] { inMessage.getThiefAssaultPartyId(), inMessage.getThiefRoomId() };
    }

    /**
     * Called by the master thief
     * The master choses 3 thieves to form an assault party
     * He wakes up the thieves and waits for them to prepare the excursion
     * 
     * @param assaultPartyID
     * @param RoomID
     */

    public void prepareAssaultParty(int assaultPartyID, int RoomID) {

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        System.out.println("MasterThief sending prepareAssaultParty message to ConcentrationSite with state: " + ((MasterThief) Thread.currentThread()).getMasterThiefState());

        outMessage = new Message(MessageType.PREPAREASSAULT, assaultPartyID, RoomID);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //Validate response
        if(inMessage.getMsgType() != MessageType.READYTOSEND){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getMasterAssaultPartyId() != assaultPartyID){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid assault party id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getMasterRoomId() != RoomID){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid room id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getMasterState() != MasterThiefStates.ASSEMBLING_A_GROUP){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid master thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterState());

        System.out.println("MasterThief received prepareAssaultParty message from ConcentrationSite with assaultPartyID: " + assaultPartyID + " and roomID: " + RoomID);

        com.close();

    }

    /*
     * Called by master thief
     * He ends the assault and wakes up the thieves
     * 
     * @param numStolenPaintings
     * 
     * @return
     */

    public  void sumUpResults(int numStolenPaintings) {
        
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SUMUPRESULTS, numStolenPaintings);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //Validate response
        if(inMessage.getMsgType() != MessageType.TOTALCANVAS){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getMasterState() != MasterThiefStates.PRESENTING_THE_REPORT){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid master thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterState());
        com.close();


    }


    /*
     * Operation server shutdown.
     */

     public void shutdown(){

        System.out.println("ConcentrationSite shutdown...");

        ClientCom com;
        Message inMessage, outMessage;
        
        com = new ClientCom (serverHostName, serverPortNumb);

        while (!com.open ()) {
            try {
                Thread.sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.CONSHUTDOWN);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.CONSHUTDOWNREP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

    }

}