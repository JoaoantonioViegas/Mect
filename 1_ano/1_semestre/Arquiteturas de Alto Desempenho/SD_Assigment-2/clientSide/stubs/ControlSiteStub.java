package clientSide.stubs;

import commInfra.*;
import clientSide.entities.*;
import genclass.GenericIO;

/**
 * Collection Site (shared region)
 * 
 * This class implements the collection site shared region.
 */

public class ControlSiteStub {

    /**
     * Name of the platform where is located the Museum server
     */

     private String serverHostName;

     /**
      * Number of the port where the server is listening
      */
 
     private int serverPortNumb;

    /**
     * Collection Site instantiation
     * 
     * @param serverHostName
     * @param serverPortNumb
     */

    public ControlSiteStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /*
     * Called by master thief
     * It is called by the master thief to start the operation
     */

    public  void startOfOperation() {
        ClientCom com;
        Message inMessage, outMessage;

        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.STARTOP, -1);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterState());

        com.close();
    }

    /**
     * Operation to hand a canvas to the master thief
     * It is called by the ordinary thief when he has a canvas to hand over to the
     * master thief
     */

    public  void handACanvas(boolean hasCanvas, int assaultPartyId, int roomId) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.HANDACANVAS, ((OrdinaryThief) Thread.currentThread()).getThiefId(),  assaultPartyId, roomId, hasCanvas);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        // validate response

        if (inMessage.getMsgType() != MessageType.COLLECTEDCANVAS && inMessage.getMsgType() != MessageType.HASCOLLECTED) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getThiefId() != ((OrdinaryThief) Thread.currentThread()).getThiefId()) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        System.out.println("Thief " + ((OrdinaryThief) Thread.currentThread()).getThiefId() + " has handed a canvas to the master thief");

        com.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getThiefState());
    }

    /**
     * Operation to collect a canvas from the ordinary thief
     * It is called by the master thief when he is ready to collect a canvas from
     * the ordinary thief
     */

    public  void collectACanvas() {

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.COLLECTCANVAS, ((MasterThief) Thread.currentThread()).getMasterThiefState());

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        // validate response

        if (inMessage.getMsgType() != MessageType.HASCOLLECTED) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        System.out.println("Master thief collected a canvas");

        com.close();
        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterState());
    }

    /**
     * Function that returns a room that still has paintings and is not occupied
     * 
     * @return
     */

    public int getRoomWithCanvasID() {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Sending message to get room with canvas id");

        outMessage = new Message(MessageType.GETROOMID, -1);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        // validate response

        if (inMessage.getMsgType() != MessageType.ROOMID) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        System.out.println("Received message with room id: " + inMessage.getMasterRoomId());

        return inMessage.getMasterRoomId();
    }

    /**
     * Function that returns an assault party that is not occupied
     * 
     * @return
     */

    public int getAssaultPartyID() {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Sending message to get assault party id");

        outMessage = new Message(MessageType.GETASSAULTID, -1);

        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        // validate response

        if (inMessage.getMsgType() != MessageType.ASSAULTID) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        System.out.println("Received assault party id: " + inMessage.getMasterAssaultPartyId());

        return inMessage.getMasterAssaultPartyId();
    }

    /**
     * Called by the master thief to appraise the situation
     * If he has enough thieves to form an assault party, he will form one
     * If he has stolen all the paintings, he will terminate the operation
     * If he has no thieves to form an assault party, he will take a rest
     * 
     * @return the operation to be performed
     */

    public  char appraiseSit() {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Master thief appraising situation...");

        outMessage = new Message(MessageType.APPRAISESIT, -1);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        // validate response
        if (inMessage.getMsgType() != MessageType.APPRAISEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        System.out.println("Master thief is done appraising situation, operation: " + inMessage.getOperation());

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterState());
        return inMessage.getOperation();
    }

    /*
     * Called by master thief
     * He will take a rest until there is a thief waiting to hand over a canvas
     */

    public  void takeARest() {

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.TAKEAREST, -1);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        // validate response
        if (inMessage.getMsgType() != MessageType.HASRESTED) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterState());

    }

    /**
     * Called by the master thief to get the number of stolen paintings
     * 
     * @return the number of stolen paintings
     */

    public  int getCollectedCanvas() {

        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.GETTOTALCANVAS, -1);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        // validate response
        if (inMessage.getMsgType() != MessageType.TOTALCANVAS) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.getNumStolenPaintings();
    }

    /*
     * Operation server shutdown.
     */

     public void shutdown(){

        System.out.println("Shutting down server...");

        ClientCom com;
        Message inMessage, outMessage;
        
        com = new ClientCom (serverHostName, serverPortNumb);

        while (!com.open ()) {
            try {
                Thread.sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.CONTSHUTDOWN);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.CONTSHUTDOWNREP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

    }

}