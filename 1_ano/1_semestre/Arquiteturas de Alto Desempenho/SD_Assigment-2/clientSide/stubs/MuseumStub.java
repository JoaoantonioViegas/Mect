package clientSide.stubs;

import commInfra.*;
import clientSide.entities.*;
import genclass.GenericIO;

/**
 * Stub to the Museum 
 * 
 *      It instantiates a remote reference to the barber shop.
*       Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */

public class MuseumStub {

    /**
     * Name of the platform where is located the Museum server
     */

    private String serverHostName;

    /**
     * Number of the port where the server is listening
     */

    private int serverPortNumb;

    /**
     * Museum instantiation
     * 
     * @param numRooms
     * @param canvas
     * @param serverHostName
     * @param serverPortNumb
     */

    public MuseumStub( int numRooms, String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Set canvas in rooms
     * @param canvas
     */

    public void setPaintingsInRoom(int canvas[]){
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETINUMCANVAS, canvas);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //Verify if the message is valid
        if(inMessage.getMsgType() != MessageType.SETINUMCANVASREP){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        //close the connection
        com.close();
    }

    /**
     * Called by the ordinary thief to roll a canvas
     * 
     * @param roomId
     * @return true if the thief has a canvas, false otherwise
     */
    public  boolean rollACanvas(int roomId) {

        ClientCom com;
        Message inMessage, outMessage;

        com = new ClientCom(serverHostName, serverPortNumb);

        //Wait while the connection is not established
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        int thiefId = ((OrdinaryThief) Thread.currentThread()).getThiefId();

        outMessage = new Message(MessageType.ROLLACANVAS, thiefId, roomId);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        
        //Verify if the message is valid
        if(inMessage.getMsgType() != MessageType.HASACANVAS){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getThiefId() != thiefId){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid thief id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if(inMessage.getThiefRoomId() != roomId){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid room id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        //close the connection
        com.close();
        return inMessage.getThiefHasCanvas();
    }

    /*
     * Operation server shutdown.
     */

     public void shutdown(){
        System.out.println("MuseumStub: shutdown()");

        ClientCom com;
        Message inMessage, outMessage;
        
        com = new ClientCom (serverHostName, serverPortNumb);

        while (!com.open ()) {
            try {
                Thread.sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.MUSEUMSHUTDOWN);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.MUSEUMSHUTDOWNREP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

    }
}
