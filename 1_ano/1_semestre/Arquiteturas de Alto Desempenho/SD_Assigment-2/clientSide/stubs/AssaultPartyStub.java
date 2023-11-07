package clientSide.stubs;

import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Assault Party (shared region)
 * 
 * This class implements the assault party shared region.
 */

public class AssaultPartyStub {

    /**
     * Name of the platform where is located the Museum server
     */

     private String serverHostName;

     /**
      * Number of the port where the server is listening
      */
 
     private int serverPortNumb;

    /**
     * Constructor for the Assault Party
     * 
     * @param id
     * @param serverHostName
     * @param serverPortNumb
     */

    public AssaultPartyStub(int id, String serverHostName, int serverPortNumb) {

        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;

    }

    /**
     * Set roomDistances
     * @param roomDistances
     */

    public void setRoomDistances(int[] roomDistances) {

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETIROOMDIST, roomDistances);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SETIROOMDISTREP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

    }

    /**
     * Called by the ordinary thief when he is crawling in
     * 
     * @param roomId
     */

    public  void crawlIn(int roomId, int assaultPartyID, int agility) {

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CRAWLIN, ((OrdinaryThief) Thread.currentThread()).getThiefId(), roomId, assaultPartyID, agility);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.HASCRAWLEDIN) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getThiefId() != ((OrdinaryThief) Thread.currentThread()).getThiefId()) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefRoomId() != roomId){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid room id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefState() != OrdinaryThiefStates.AT_A_ROOM){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getThiefState());
        
    }

    /**
     * Called by the ordinary thief when he is crawling out
     * 
     * @param roomId
     */

    public  void crawlOut(int roomId, int assaultPartyID, int agility) {
        
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CRAWLOUT, ((OrdinaryThief) Thread.currentThread()).getThiefId(), roomId, assaultPartyID, agility);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.HASCRAWLEDOUT) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getThiefId() != ((OrdinaryThief) Thread.currentThread()).getThiefId()) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefRoomId() != roomId){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid room id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefState() != OrdinaryThiefStates.CRAWLING_OUTWARDS && inMessage.getThiefState() != OrdinaryThiefStates.WAITING_FOR_CRAWL_OUT && inMessage.getThiefState() != OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

        System.out.println("Thief " + ((OrdinaryThief) Thread.currentThread()).getThiefId() + " has crawled out of room " + roomId + " with state " + inMessage.getThiefState());
        
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getThiefState());
        
    }

    /**
     * Called by the master thief when he is ready to send the assault party
     */

    public  void sendAssaultParty(int apId) {
        
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SENDASSAULT, apId);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.ASSAULTSENT) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
			GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

        
    }

    /**
     * Called by the ordinary thief to reverse the direction
     */

    public  void reverseDirection(int id) {
        
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.REVERSE, ((OrdinaryThief) Thread.currentThread()).getThiefId(), ((OrdinaryThief) Thread.currentThread()).getThiefState(), id);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.HASREVERSED) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
			GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefState() != OrdinaryThiefStates.CRAWLING_OUTWARDS && inMessage.getThiefState() != OrdinaryThiefStates.WAITING_FOR_CRAWL_OUT){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getThiefId() != ((OrdinaryThief) Thread.currentThread()).getThiefId()){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getThiefState());
        
    }

    /*
     * Operation server shutdown.
     */

    public void shutdown(){

        System.out.println("Shutdown AssaultParty...");

        ClientCom com;
        Message inMessage, outMessage;
        
        com = new ClientCom (serverHostName, serverPortNumb);

        while (!com.open ()) {
            try {
                Thread.sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.APSHUTDOWN);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.APSHUTDOWNREP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

    }
}