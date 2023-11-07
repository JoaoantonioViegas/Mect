package serverSide.sharedRegions;

import serverSide.entities.ControlSiteClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Control Site.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Museum and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ControlSiteInterface {

    /*
    * Reference to the Control Site
    */

    private final ControlSite controlSite;

    /*
    * Instantiation of an interface to the Control Site.
    * 
    * @param controlSite reference to the Control Site
    */

    public ControlSiteInterface(ControlSite controlSite)
    {
        this.controlSite = controlSite;
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
            case MessageType.STARTOP:
                break;
            case MessageType.HANDACANVAS:
                if (inMessage.getThiefId() < 0 || inMessage.getThiefId() >= SimulPar.M)
                    throw new MessageException("Invalid thief id! " + inMessage.getMsgType(), inMessage);
                if (inMessage.getThiefState() != OrdinaryThiefStates.CRAWLING_OUTWARDS && inMessage.getThiefState() != OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS)
                    throw new MessageException("Invalid thief state! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.COLLECTCANVAS:
                if (inMessage.getMasterState() != MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL)
                    throw new MessageException("Invalid master thief state! " + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.GETROOMID:
                break;
            case MessageType.GETASSAULTID:
                break;
            case MessageType.APPRAISESIT:
                // if(inMessage.getMasterState() != MasterThiefStates.DECIDING_WHAT_TO_DO)
                //     throw new MessageException("Invalid master thief state!", inMessage);
                break;
            case MessageType.TAKEAREST:
                // if(inMessage.getMasterState() != MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL)
                //     throw new MessageException("Invalid master thief state!", inMessage);
                break;
            case MessageType.GETTOTALCANVAS:
                break;
            case MessageType.CONTSHUTDOWN:
                break;
            default:
                throw new MessageException("Invalid message type! " + inMessage.getMsgType(), inMessage);
        
        }

        /* message processing */
        switch(inMessage.getMsgType()){
            case MessageType.STARTOP:
                controlSite.startOfOperation();
                outMessage = new Message(MessageType.ACK);
                break;
            case MessageType.HANDACANVAS:
                int assaultId = inMessage.getThiefAssaultPartyId();
                int roomId = inMessage.getThiefRoomId();
                int id = inMessage.getThiefId();
                ((ControlSiteClientProxy) Thread.currentThread()).setOrdinaryThiefId(id);
                boolean hasCanvas = inMessage.getThiefHasCanvas();
                controlSite.handACanvas(hasCanvas, assaultId, roomId);
                outMessage = new Message(MessageType.COLLECTEDCANVAS, inMessage.getThiefId(), assaultId, roomId, hasCanvas);
                break;
            case MessageType.COLLECTCANVAS:
                controlSite.collectACanvas();
                outMessage = new Message(MessageType.HASCOLLECTED);
                break;
            case MessageType.GETROOMID:
                int roomId2 = controlSite.getRoomWithCanvasID();
                outMessage = new Message(MessageType.ROOMID, roomId2);
                break;
            case MessageType.GETASSAULTID:
                int assaultId2 = controlSite.getAssaultPartyID();
                outMessage = new Message(MessageType.ASSAULTID, assaultId2);
                break;
            case MessageType.APPRAISESIT:
                char oper = controlSite.appraiseSit();
                outMessage = new Message(MessageType.APPRAISEDONE, oper);
                break;
            case MessageType.TAKEAREST:
                controlSite.takeARest();
                outMessage = new Message(MessageType.HASRESTED);
                break;
            case MessageType.GETTOTALCANVAS:
                int totalCanvas = controlSite.getCollectedCanvas();
                outMessage = new Message(MessageType.TOTALCANVAS, totalCanvas);
                break;
            case MessageType.CONTSHUTDOWN:
                System.out.println("ControlSiteInterface: CONTSHUTDOWN");
                controlSite.shutdown();
                outMessage = new Message(MessageType.CONTSHUTDOWNREP);
                break;
            
        }

        return outMessage;
    }
    
}
