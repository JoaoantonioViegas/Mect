package serverSide.sharedRegions;

import serverSide.main.*;
import commInfra.*;

/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Museum and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralRepoInterface {
    
    /*
     * Reference to the General Repository of Information
     */

    private final GeneralRepo generalRepo;

    /*
     * Instantiation of an interface to the General Repository of Information.
     * 
     * @param generalRepo reference to the General Repository of Information
     */

    public GeneralRepoInterface(GeneralRepo generalRepo)
    {
        this.generalRepo = generalRepo;
    }

    /**
     *  Processing of the incoming messages.
     *
     *  Validation, execution of the corresponding method and generation of the outgoing message.
     *
     *    @param inMessage service request
     *    @return service reply
     *    @throws MessageException if the incoming message is not valid
     */

     public Message processAndReply (Message inMessage) throws MessageException
     {
        Message outMessage = null;

        /* validation of the incoming message */

        switch(inMessage.getMsgType()){
            case MessageType.SETMS:
                if(inMessage.getMasterState() < 0 || inMessage.getMasterState() > 4)
                    throw new MessageException("Invalid Master State!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETOS:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() > SimulPar.M)
                    throw new MessageException("Invalid Thief Id!" + inMessage.getMsgType(), inMessage);
                if(inMessage.getThiefState() < 0 || inMessage.getThiefState() > 7)
                    throw new MessageException("Invalid Thief State!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETOSIT:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() > SimulPar.M)
                    throw new MessageException("Invalid Thief Id!" + inMessage.getMsgType(), inMessage);
                if(inMessage.getThiefState() < 0 || inMessage.getThiefState() > 7)
                    throw new MessageException("Invalid Thief State!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETOPOS:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() > SimulPar.M)
                    throw new MessageException("Invalid Thief Id!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETOCAN:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() > SimulPar.M)
                    throw new MessageException("Invalid Thief Id!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETOALL:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() > SimulPar.M)
                    throw new MessageException("Invalid Thief Id!" + inMessage.getMsgType(), inMessage);
                if(inMessage.getThiefState() < 0 || inMessage.getThiefState() > 7)
                    throw new MessageException("Invalid Thief State!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.RESOALL:
                if(inMessage.getThiefId() < 0 || inMessage.getThiefId() > SimulPar.M)
                    throw new MessageException("Invalid Thief Id!" + inMessage.getMsgType(), inMessage);
                if(inMessage.getThiefState() < 0 || inMessage.getThiefState() > 7)
                    throw new MessageException("Invalid Thief State!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SETAPROOM:
                if(inMessage.getThiefRoomId() < -1 || inMessage.getThiefRoomId() > SimulPar.N)
                    throw new MessageException("Invalid Room Id!" + inMessage.getMsgType(), inMessage);
                break;
            case MessageType.SHUT:
            case MessageType.SETIROOMDIST:
            case MessageType.SETITHIEFAGIL:
            case MessageType.SETINUMCANVAS:
            case MessageType.REPSHUTDOWN:
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }
    
        /* message processing */

        switch(inMessage.getMsgType()){
            case MessageType.SETMS:
                int masterState = inMessage.getMasterState();
                generalRepo.setMasterThiefState(masterState);
                outMessage = new Message(MessageType.SETMSREP);
                break;
            case MessageType.SETOS:
                int thiefId = inMessage.getThiefId();
                int thiefState = inMessage.getThiefState();
                generalRepo.setOrdinaryThiefState(thiefId, thiefState);
                outMessage = new Message(MessageType.SETOSREP);
                break;
            case MessageType.SETOSIT:
                int thiefId2 = inMessage.getThiefId();
                char thiefSituation = inMessage.getThiefSituation();
                generalRepo.setOrdinaryThiefSituation(thiefId2, thiefSituation);
                outMessage = new Message(MessageType.SETOSITREP);
                break;
            case MessageType.SETOPOS:
                int thiefId3 = inMessage.getThiefId();
                int thiefPosition = inMessage.getThiefPos();
                generalRepo.setOrdinaryThiefPosition(thiefId3, thiefPosition);
                outMessage = new Message(MessageType.SETOPOSREP);
                break;
            case MessageType.SETOCAN:
                int thiefId4 = inMessage.getThiefId();
                int thiefCarryingCanvas = inMessage.getThiefCanvasAlt();
                int thiefRoomId = inMessage.getThiefRoomId();
                generalRepo.setOrdinaryThiefCanvas(thiefId4, thiefCarryingCanvas, thiefRoomId);
                outMessage = new Message(MessageType.SETOCANREP);
                break;
            case MessageType.SETOALL:
                int thiefId5 = inMessage.getThiefId();
                int thiefState2 = inMessage.getThiefState();
                char thiefSituation2 = inMessage.getThiefSituation();
                int thiefAssaultParty = inMessage.getThiefAssaultPartyId();
                int thiefCarryingCanvas2 = inMessage.getThiefCanvasAlt();
                int thiefPosition2 = inMessage.getThiefPos();
                int thiefRoomId2 = inMessage.getThiefRoomId();
                generalRepo.setOrdinaryAll(thiefId5, thiefState2, thiefSituation2, thiefAssaultParty, thiefRoomId2, thiefPosition2, thiefCarryingCanvas2);
                outMessage = new Message(MessageType.SETOALLREP);
                break;
            case MessageType.RESOALL:
                int thiefId6 = inMessage.getThiefId();
                int thiefState3 = inMessage.getThiefState();
                generalRepo.resetOrdinaryAll(thiefId6, thiefState3);
                outMessage = new Message(MessageType.RESOALLREP);
                break;
            case MessageType.SETAPROOM:
                int assaultPartyId = inMessage.getThiefAssaultPartyId();
                int roomId = inMessage.getThiefRoomId();
                generalRepo.setAssaultPartyRoom(assaultPartyId, roomId);
                outMessage = new Message(MessageType.SETAPROOMREP);
                break;
            case MessageType.SHUT:
                generalRepo.endAssault();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
            case MessageType.SETIROOMDIST:
                int[] roomDist = inMessage.getDistanceToRoom();
                generalRepo.setDistanceToRoom(roomDist);
                outMessage = new Message(MessageType.SETIROOMDISTREP);
                break;
            case MessageType.SETITHIEFAGIL:
                int[] thiefAgility = inMessage.getThievesAgility();
                generalRepo.setOrdinaryThiefMaxDisplacement(thiefAgility);
                outMessage = new Message(MessageType.SETITHIEFAGILREP);
                break;
            case MessageType.SETINUMCANVAS:
                int[] numCanvas = inMessage.getCanvasInRoom();
                generalRepo.setPaintingsInRoom(numCanvas);
                outMessage = new Message(MessageType.SETINUMCANVASREP);
                break;
            case MessageType.REPSHUTDOWN:
                System.out.println("Shutting down repo");
                generalRepo.shutdown();
                outMessage = new Message(MessageType.REPSHUTDOWNREP);
                break;

        }
        return (outMessage);
    }
}
