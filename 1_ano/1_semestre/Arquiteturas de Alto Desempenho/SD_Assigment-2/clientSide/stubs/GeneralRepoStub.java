package clientSide.stubs;

import commInfra.*;
import genclass.GenericIO;


/**
 * General Repository.
 *
 * It is responsible to keep the visible internal state of the problem and to
 * provide means for it
 * to be printed in the logging file.
 * It is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 */

public class GeneralRepoStub {

    /**
     * Name of the platform where is located the Museum server
     */

     private String serverHostName;

     /**
      * Number of the port where the server is listening
      */
 
     private int serverPortNumb;

     /**
      * Has reportedDistance
      */

    private boolean hasReportedDistance;

    /**
     * Has reportedCanvas
     */

    private boolean hasReportedCanvas;

    /**
     * Has reportedAgility
     */

    private boolean hasReportedAgility;

    /**
     * General repository instantiation
     * 
     * @param serverHostName
     * @param serverPortNumb
     */

    public GeneralRepoStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
        this.hasReportedDistance = false;
        this.hasReportedCanvas = false;
        this.hasReportedAgility = false;

        //reportInitialStatus();
    }

    /**
     * Report the initial number of canvas
     */

    private void reportInitialNumCanvas(int[] canvas) {

        if(hasReportedCanvas) {
            return;
        }

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

        if(inMessage.getMsgType() != MessageType.SETINUMCANVASREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        hasReportedCanvas = true;
    }

    /**
     * Report the initial distance of rooms
     */

     private void reportInitialDistances(int[] distances) {

        if(hasReportedDistance) {
            return;
        }
         
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETIROOMDIST, distances);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETIROOMDISTREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        hasReportedDistance = true;

    }

    /**
     * Report the initial thief agiliti
     */

     private void reportInitialAgilities(int[] agilities) {

        if(hasReportedAgility) {
            return;
        }
         
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETITHIEFAGIL, agilities);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETITHIEFAGILREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        hasReportedAgility = true;

    }



    /**
     * Set the state of the master thief
     * 
     * @param state
     */

    public  void setMasterThiefState(int state) {
        ClientCom com;
        Message inMessage, outMessage;  
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.SETMS, state);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETMSREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the displacement of the thieves
     * @param maxDisplacement
     */

    public  void setOrdinaryThiefMaxDisplacement(int maxDisplacement[]) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETITHIEFAGIL, maxDisplacement);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETITHIEFAGILREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set distance to rooms
     * @param distanceToRoom
     */

     public  void setDistanceToRoom(int distanceToRoom[]) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETIROOMDIST, distanceToRoom);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETIROOMDISTREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the number of paintings in the room
     * @param paintingsInRoom
     */

     public  void setPaintingsInRoom(int paintingsInRoom[]) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETINUMCANVAS, paintingsInRoom);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETINUMCANVASREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the state of the ordinary thief
     * 
     * @param thiefID
     * @param state
     */

    public  void setOrdinaryThiefState(int thiefID, int state) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETOS, thiefID, state);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETOSREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the situation of the ordinary thief
     * 
     * @param thiefID
     * @param situation
     */

    public  void setOrdinaryThiefSituation(int thiefID, char situation) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETOSIT, thiefID, situation);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETOSITREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the position of the ordinary thief
     * 
     * @param thiefID
     * @param position
     */

    public  void setOrdinaryThiefPosition(int thiefID, int position) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETOPOS, thiefID, position);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETOPOSREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the canvas of the ordinary thief and the number of paintings in the room
     * 
     * @param thiefID
     * @param canvas
     * @param roomID
     */

    public  void setOrdinaryThiefCanvas(int thiefID, int canvas, int roomID) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETOCAN, thiefID, canvas, roomID);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        
        if(inMessage.getMsgType() != MessageType.SETOCANREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Sets all the variables of the ordinary thief
     * 
     * @param thiefID
     * @param state
     * @param situation
     * @param assaultPartyID
     * @param roomID
     * @param position
     * @param canvas
     */

    public  void setOrdinaryAll(int thiefID, int state, char situation, int assaultPartyID, int roomID,
            int position, int canvas) {
        
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETOALL, thiefID, state, situation, assaultPartyID, roomID, position, canvas);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETOALLREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Reset all the variables of the ordinary thief
     * 
     * @param thiefID
     * @param state
     */

    public  void resetOrdinaryAll(int thiefID, int state) {
        ClientCom com;
        Message inMessage, outMessage;  
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.RESOALL, thiefID, state);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.RESOALLREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the room of the assault party
     * 
     * @param assaultPartyID
     * @param roomID
     */

    public  void setAssaultPartyRoom(int assaultPartyID, int roomID) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        System.out.println("Creating message SETAPROOM with assautlPartyID: " + assaultPartyID + " and roomID: " + roomID);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SETAPROOM, assaultPartyID, roomID);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SETAPROOMREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Adds a thief to the assault party
     * 
     * @param assaultId
     * @param thiefId
     */

    private  void addToAssaultParty(int assaultId, int thiefId) {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.ADDTOAP, assaultId, thiefId);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ADDTOAPREP) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * End the assault
     * Prints the number of stolen canvas and the legend
     */

    public  void endAssault() {
        ClientCom com;
        Message inMessage, outMessage;
        com = new ClientCom(serverHostName, serverPortNumb);

        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SHUT);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.SHUTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

     /*
     * Operation server shutdown.
     */

     public void shutdown(){

        System.out.println("Shutdown");

        ClientCom com;
        Message inMessage, outMessage;
        
        com = new ClientCom (serverHostName, serverPortNumb);

        while (!com.open ()) {
            try {
                Thread.sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.REPSHUTDOWN);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.REPSHUTDOWNREP) {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close();

    }

}