package serverSide.sharedRegions;

import serverSide.main.*;
import clientSide.entities.*;
import serverSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Museum.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Museum and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class MuseumInterface {

    /*
     * Reference to the Museum
     */

    private final Museum museum;

    /**
     * Instantiation of an interface to the Museum.
     * 
     * @param museum reference to the Museum
     */

    public MuseumInterface(Museum museum)
    {
        this.museum = museum;
    }

    /**
     * Processing of messages by executing the corresponding task.
     * Generation of a reply message.
     * 
     * @param inMessage incoming message with the request
     * @return reply message
     * @throws MessageException if the message with the request is considered invalid
     */

    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;                           // outgoing message

        /* validation of the incoming message */

        switch(inMessage.getMsgType()){
            case MessageType.ROLLACANVAS:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M){
                    throw new MessageException("Invalid Thief Id! " + inMessage.getMsgType(), inMessage);
                } else if (inMessage.getThiefState() != OrdinaryThiefStates.AT_A_ROOM){
                    throw new MessageException("Invalid Ordinary Thief State! " + inMessage.getMsgType(), inMessage);
                }
                break;
            case MessageType.SETINUMCANVAS:
            case MessageType.MUSEUMSHUTDOWN:
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* message processing */
        switch(inMessage.getMsgType()){
            case MessageType.ROLLACANVAS:
                int thiefId = inMessage.getThiefId();
                int roomId = inMessage.getThiefRoomId();
                ((MuseumClientProxy) Thread.currentThread()).setOrdinaryThiefId(thiefId);
                boolean hasCanvas = museum.rollACanvas(roomId);
                outMessage = new Message(MessageType.HASACANVAS, ((MuseumClientProxy)Thread.currentThread()).getOrdinaryThiefId(), ((MuseumClientProxy)Thread.currentThread()).getOrdinaryThiefState(), roomId, hasCanvas);
                break;
            case MessageType.SETINUMCANVAS:
                int nCanvas[] = inMessage.getCanvasInRoom();
                museum.setCanvas(nCanvas);
                outMessage = new Message(MessageType.SETINUMCANVASREP);
                break;
            case MessageType.MUSEUMSHUTDOWN:
                System.out.println("Museum is shutting down...");
                museum.shutdown();
                outMessage = new Message(MessageType.MUSEUMSHUTDOWNREP);
                break;
        }

        return (outMessage);
    }
    
}
