package serverSide.sharedRegions;

import serverSide.entities.AssaultPartyClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Assault Party.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Museum and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultPartyInterface {

    /*
     * Reference to the AssaultParty
     */

    private final AssaultParty assaultParty;

    /*
     * Instantiation of an interface to the Concentration Site.
     * 
     * @param concentrationSite reference to the Concentration Site
     */

    public AssaultPartyInterface(AssaultParty assaultParty)
    {
        this.assaultParty = assaultParty;
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
            case MessageType.CRAWLIN:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M)
                    throw new MessageException("Invalid thief id! " + inMessage.getMsgType(), inMessage);
                break;	
            case MessageType.CRAWLOUT:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M)
                    throw new MessageException("Invalid thief id! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SENDASSAULT:
                break;
            case MessageType.REVERSE:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M)
                    throw new MessageException("Invalid thief id! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETIROOMDIST:
            case MessageType.APSHUTDOWN:
                break;

            default:
                throw new MessageException("Invalid message type! " + inMessage.getMsgType(), inMessage);
        }

        /* message processing */

        switch(inMessage.getMsgType()){
            case MessageType.CRAWLIN:
                int id = inMessage.getThiefId();
                int roomId = inMessage.getThiefRoomId();
                int assaultPartyId = inMessage.getThiefAssaultPartyId();
                int agility = inMessage.getThiefAgility();
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryThiefId(id);
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryThiefAgility(agility);
                assaultParty.crawlIn(roomId, assaultPartyId, agility);
                outMessage = new Message(MessageType.HASCRAWLEDIN, id, roomId, OrdinaryThiefStates.AT_A_ROOM);
                break;
            case MessageType.CRAWLOUT:
                int id2 = inMessage.getThiefId();
                int assaultPartyId2 = inMessage.getThiefAssaultPartyId();
                int agility2 = inMessage.getThiefAgility();
                int roomId2 = inMessage.getThiefRoomId();
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryThiefId(id2);
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryThiefAgility(agility2);
                assaultParty.crawlOut(roomId2, assaultPartyId2, agility2);
                outMessage = new Message(MessageType.HASCRAWLEDOUT, id2, roomId2, OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS);
                break;
            case MessageType.SENDASSAULT:
                int assaultPartyId3 = inMessage.getMasterAssaultPartyId();
                assaultParty.sendAssaultParty();
                outMessage = new Message(MessageType.ASSAULTSENT, assaultPartyId3);
                break;
            case MessageType.REVERSE:
                int assaultPartyId4 = inMessage.getThiefAssaultPartyId();
                int id4 = inMessage.getThiefId();
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryThiefId(id4);
                assaultParty.reverseDirection();
                outMessage = new Message(MessageType.HASREVERSED, inMessage.getThiefId());
                break;
            case MessageType.SETIROOMDIST:
                int roomDistances[] = inMessage.getDistanceToRoom();
                assaultParty.setDistanceToRoom(roomDistances);
                outMessage = new Message(MessageType.SETIROOMDISTREP);
                break;
            case MessageType.APSHUTDOWN:    
                System.out.println("AssaultPartyInterface: AP shutdown");
                assaultParty.shutdown();
                outMessage = new Message(MessageType.APSHUTDOWNREP);
                break;
        }

    
    
        return outMessage;
    }
}
