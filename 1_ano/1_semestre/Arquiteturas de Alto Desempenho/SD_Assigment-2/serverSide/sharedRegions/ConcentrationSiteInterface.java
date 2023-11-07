package serverSide.sharedRegions;

import serverSide.entities.ConcentrationSiteClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Concentration Site.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Museum and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ConcentrationSiteInterface{

    /*
     * Reference to the Concentration Site
     */

    private final ConcentrationSite concentrationSite;

    /*
     * Instantiation of an interface to the Concentration Site.
     * 
     * @param concentrationSite reference to the Concentration Site
     */

    public ConcentrationSiteInterface(ConcentrationSite concentrationSite)
    {
        this.concentrationSite = concentrationSite;
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
            case MessageType.AMINEEDED:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M)
                    throw new MessageException("Invalid thief id! " + inMessage.getMsgType(), inMessage);
                if(inMessage.getThiefState() != OrdinaryThiefStates.WAITING_UNTIL_NEEDED)
                    throw new MessageException("Invalid thief state! " + inMessage.getMsgType() + " with state " + inMessage.getThiefState(), inMessage);
                break;

            case MessageType.PREPAREEXC:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M)
                    throw new MessageException("Invalid thief id! " + inMessage.getMsgType(), inMessage);
                if (inMessage.getThiefState() != OrdinaryThiefStates.WAITING_UNTIL_NEEDED)
                    throw new MessageException("Invalid thief state! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.PREPAREASSAULT:
                if(inMessage.getMasterState() != MasterThiefStates.ASSEMBLING_A_GROUP && inMessage.getMasterState() != MasterThiefStates.DECIDING_WHAT_TO_DO)
                    throw new MessageException("Invalid master thief state! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SUMUPRESULTS:
                if(inMessage.getMasterState() != MasterThiefStates.DECIDING_WHAT_TO_DO && inMessage.getMasterState() != MasterThiefStates.PRESENTING_THE_REPORT)
                    throw new MessageException("Invalid master thief state! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.CONSHUTDOWN:
                break;
            default:
                throw new MessageException("Invalid message type! " + inMessage.getMsgType(), inMessage);
        }

        /* message processing */

        switch(inMessage.getMsgType()){
            case MessageType.AMINEEDED:
                int id = inMessage.getThiefId();
                System.out.println("Ordinary Thief " + id + " is checking if he is needed!");
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryThiefId(id);
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryThiefState(inMessage.getThiefState());
                boolean isNeeded = concentrationSite.amINeeded();
                System.out.println("Ordinary Thief " + id + " is needed? " + isNeeded);
                outMessage = new Message(MessageType.ISNEEDED, inMessage.getThiefId(), isNeeded);
                break;
            case MessageType.PREPAREEXC:
                int ids[] = new int[2];
                int id2 = inMessage.getThiefId();
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryThiefId(id2);
                ids = concentrationSite.prepareExcursion();
                System.out.println(ids[0] + " " + ids[1]);
                outMessage = new Message(MessageType.READYTOGO, inMessage.getThiefId(), ids[0], ids[1]);
                break;
            case MessageType.PREPAREASSAULT:
                int assaultId = inMessage.getMasterAssaultPartyId();
                int roomId = inMessage.getMasterRoomId();
                System.out.println("Master Thief is preparing the assault party " + assaultId + " to room " + roomId + "!");
                concentrationSite.prepareAssaultParty(assaultId, roomId);
                outMessage = new Message(MessageType.READYTOSEND, assaultId, roomId);
                break;
            case MessageType.SUMUPRESULTS:
                int numStolenPaintings = inMessage.getNumStolenPaintings();
                concentrationSite.sumUpResults(numStolenPaintings);
                outMessage = new Message(MessageType.TOTALCANVAS);
                break;
            case MessageType.CONSHUTDOWN:
                System.out.println("Concentration Site is shutting down...");
                concentrationSite.shutdown();
                outMessage = new Message(MessageType.CONSHUTDOWNREP);
                break;
        }
        
        return outMessage;
    }



}