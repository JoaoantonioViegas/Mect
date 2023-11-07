package serverSide.objects;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import clientSide.entities.*;
import interfaces.*;
import serverSide.main.*;
import genclass.*;

/**
 * GeneralRepo (shared region)
 * 
 * This class implements the GeneralRepo shared region.
 * 
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class GeneralRepo implements GeneralRepoInterface {

    /**
     * Name of the logging file
     */

    private final String logFileName;

    /**
     * State of the master thief
     */

    private int masterThiefState;

    /**
     * State of the ordinary thief
     */

    private int[] ordinaryThiefState;

    /**
     * Position of the ordinary thief
     */

    private int[] ordinaryThiefPosition;

    /**
     * Canvas of the ordinary thief
     */

    private int[] ordinaryThiefCanvas;

    /**
     * Assault party id of the ordinary thief
     */

    private int[] ordinaryThiefAssaultPartyID;

    /**
     * Room id of the assault party
     */

    private int[] assaultPartyRoomID;

    /**
     * Situation of the ordinary thief
     */

    private char[] ordinaryThiefSituation;

    /**
     * Max displacement of the ordinary thief
     */

    private int[] ordinaryThiefMaxDisplacement;

    /**
     * Distance of the room
     */

    private int[] distanceToRoom;

    /**
     * Number of paintings in the room
     */

    private int[] paintingsInRoom;

    /**
     * Number of stolen canvas
     */

    private int stolenCanvas;

    /**
     * Ids of the thieves in the assault party 1
     */

    private int[] assaultParty1Thieves;

    /**
     * Ids of the thieves in the assault party 2
     */

    private int[] assaultParty2Thieves;

    /**
     * General repository instantiation
     * 
     * @param agility        agilities of the ordinary thieves
     */

    public GeneralRepo() {
        this.logFileName = "log.txt";
        ordinaryThiefState = new int[SimulPar.M - 1];
        ordinaryThiefPosition = new int[SimulPar.M - 1];
        ordinaryThiefCanvas = new int[SimulPar.M - 1];
        ordinaryThiefAssaultPartyID = new int[SimulPar.M - 1];
        ordinaryThiefSituation = new char[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            ordinaryThiefPosition[i] = 0;
            ordinaryThiefCanvas[i] = 0;
            ordinaryThiefAssaultPartyID[i] = 0;
            ordinaryThiefSituation[i] = 'W';
        }
        assaultPartyRoomID = new int[SimulPar.A];
        for (int i = 0; i < SimulPar.A; i++) {
            assaultPartyRoomID[i] = -1;
        }
        stolenCanvas = 0;
        assaultParty1Thieves = new int[SimulPar.K];
        assaultParty2Thieves = new int[SimulPar.K];

        for (int i = 0; i < SimulPar.K; i++) {
            assaultParty1Thieves[i] = 0;
            assaultParty2Thieves[i] = 0;
        }

        reportHeader();
    }

    /**
     * Report the initial distance to rooms
     * 
     * @param distanceToRoom array of distance to rooms
     */
    @Override
    public void reportInitialDistances(int[] distanceToRoom) throws RemoteException{
        System.out.println("Set initial distance to rooms");
        this.distanceToRoom = distanceToRoom;
    }

    /**
     * Reporting the initial number of canvas in the rooms
     * 
     * @param canvasInRoom array of canvas in the rooms
     */
    @Override
    public void reportRoomNumCanvas(int[] canvasInRoom) throws RemoteException{
        System.out.println("Set initial number of canvas in the rooms");
        this.paintingsInRoom = canvasInRoom;
    }

    /**
     * Reporting the initial thieves agility
     * 
     * @param agility array of thieves agility
     */
    @Override
    public void reportThievesAgility(int[] agility) throws RemoteException{
        System.out.println("Set initial thieves agility");
        this.ordinaryThiefMaxDisplacement = agility;
    }

    /**
     * Write the header to the log file
     */

    private void reportHeader() {
        // create a new file
        try {
            FileWriter writer = new FileWriter(logFileName);
            writer.write("                             Heist to the Museum - Description of the internal state\n\n");
            writer.write("MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6\n");
            writer.write("Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD\n");
            writer.write(
                    "                   Assault party 1                       Assault party 2                          Museum\n");
            writer.write(
                    "           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5\n");
            writer.write(
                    "    Rid  Id Pos Cv  Id Pos Cv  Id Pos Cv  Rid  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Write the current state to the log file
     */

    private void reportStatus() {
        try {
            FileWriter writer = new FileWriter(logFileName, true);
            String firstLine = "";
            String secondLine = "";

            /**
             * Formatting the first line
             */

            switch (masterThiefState) {
                case MasterThiefStates.PLANNING_THE_HEIST:
                    firstLine += "PLTH  ";
                    break;
                case MasterThiefStates.DECIDING_WHAT_TO_DO:
                    firstLine += "DWTD  ";
                    break;
                case MasterThiefStates.ASSEMBLING_A_GROUP:
                    firstLine += "ASAG  ";
                    break;
                case MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL:
                    firstLine += "WFGA  ";
                    break;
                case MasterThiefStates.PRESENTING_THE_REPORT:
                    firstLine += "PRTR  ";
                    break;
            }

            for (int i = 0; i < SimulPar.M - 1; i++) {
                switch (ordinaryThiefState[i]) {
                    case (OrdinaryThiefStates.CRAWLING_INWARDS):
                    case (OrdinaryThiefStates.WAITING_FOR_CRAWL_IN):
                        firstLine += "CRWI ";
                        break;
                    case OrdinaryThiefStates.AT_A_ROOM:
                        firstLine += "ATAR ";
                        break;
                    case OrdinaryThiefStates.CRAWLING_OUTWARDS:
                    case OrdinaryThiefStates.WAITING_FOR_CRAWL_OUT:
                        firstLine += "CRWO ";
                        break;
                    case OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS:
                        firstLine += "WTHC ";
                        break;
                    case OrdinaryThiefStates.WAITING_UNTIL_NEEDED:
                        firstLine += "WTUN ";
                        break;
                }
                firstLine += ordinaryThiefSituation[i] + "  " + ordinaryThiefMaxDisplacement[i] + "    ";
                if (i == 5)
                    firstLine += "\n";
            }

            /**
             * Formatting the second line
             */
            secondLine += "     ";

            // Assault party 1

            secondLine += (assaultPartyRoomID[0] + 1) + " ";

            for (int i = 0; i < SimulPar.K; i++) {
                if (assaultParty1Thieves[i] == 0)
                    secondLine += "   0   0  0";
                else {
                    if (ordinaryThiefPosition[assaultParty1Thieves[i] - 1] < 10) {
                        secondLine += "   " + assaultParty1Thieves[i] + "   "
                                + ordinaryThiefPosition[assaultParty1Thieves[i] - 1] + "  "
                                + ordinaryThiefCanvas[assaultParty1Thieves[i] - 1];
                    } else {
                        secondLine += "   " + assaultParty1Thieves[i] + "  "
                                + ordinaryThiefPosition[assaultParty1Thieves[i] - 1] + "  "
                                + ordinaryThiefCanvas[assaultParty1Thieves[i] - 1];
                    }

                }
            }

            // Assault party 2
            secondLine += "   " + (assaultPartyRoomID[1] + 1) + " ";

            for (int i = 0; i < SimulPar.K; i++) {
                if (assaultParty2Thieves[i] == 0) {
                    secondLine += "   0   0  0";
                } else {
                    if (ordinaryThiefPosition[assaultParty2Thieves[i] - 1] < 10) {
                        secondLine += "   " + assaultParty2Thieves[i] + "   "
                                + ordinaryThiefPosition[assaultParty2Thieves[i] - 1] + "  "
                                + ordinaryThiefCanvas[assaultParty2Thieves[i] - 1];
                    } else {
                        secondLine += "   " + assaultParty2Thieves[i] + "  "
                                + ordinaryThiefPosition[assaultParty2Thieves[i] - 1] + "  "
                                + ordinaryThiefCanvas[assaultParty2Thieves[i] - 1];
                    }
                }
            }

            // Museum
            secondLine += "   ";
            for (int i = 0; i < SimulPar.N; i++) {
                if (paintingsInRoom[i] < 10)
                    secondLine += " ";
                secondLine += paintingsInRoom[i] + " " + distanceToRoom[i] + "   ";
            }

            writer.write(firstLine + secondLine + " \n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Set the state of the master thief
     * 
     * @param state
     */
    @Override
    public void setMasterThiefState(int state) throws RemoteException {
        masterThiefState = state;
        reportStatus();
    }

    /**
     * Set the state of the ordinary thief
     * 
     * @param thiefID
     * @param state
     */
    @Override
    public void setOrdinaryThiefState(int thiefID, int state) throws RemoteException {
        ordinaryThiefState[thiefID] = state;
        reportStatus();
    }

    /**
     * Set ordinary thieves max displacement
     * @param maxDisplacement array of agilities
     */
    @Override
    public synchronized void setOrdinaryThiefMaxDisplacement(int maxDisplacement[]) throws RemoteException {
        this.ordinaryThiefMaxDisplacement = maxDisplacement;
    }

    /**
     * Set distance to rooms
     * @param distanceToRoom
     */
    @Override
    public synchronized void setDistanceToRoom(int distanceToRoom[]) throws RemoteException {
        this.distanceToRoom = distanceToRoom;
    }

    /**
     * Set the number of paintings in the room
     * @param paintingsInRoom
     */
    @Override
    public synchronized void setPaintingsInRoom(int paintingsInRoom[]) throws RemoteException {
        this.paintingsInRoom = paintingsInRoom;
    }

    /**
     * Set the situation of the ordinary thief
     * 
     * @param thiefID
     * @param situation
     */
    @Override
    public synchronized void setOrdinaryThiefSituation(int thiefID, char situation) throws RemoteException {
        ordinaryThiefSituation[thiefID] = situation;
        reportStatus();
    }

    /**
     * Set the position of the ordinary thief
     * 
     * @param thiefID
     * @param position
     */
    @Override
    public synchronized void setOrdinaryThiefPosition(int thiefID, int position) throws RemoteException {
        ordinaryThiefPosition[thiefID] = position;
        reportStatus();
    }

    /**
     * Set the canvas of the ordinary thief and the number of paintings in the room
     * 
     * @param thiefID
     * @param canvas
     * @param roomID
     */
    @Override
    public synchronized void setOrdinaryThiefCanvas(int thiefID, int canvas, int roomID) throws RemoteException {
        ordinaryThiefCanvas[thiefID] = canvas;
        if (canvas == 0)
            paintingsInRoom[roomID] = 0;
        else {
            paintingsInRoom[roomID]--;
            this.stolenCanvas++;
        }
        reportStatus();
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
    @Override
    public synchronized void setOrdinaryAll(int thiefID, int state, char situation, int assaultPartyID, int roomID,
            int position, int canvas) throws RemoteException {
        ordinaryThiefState[thiefID] = state;
        ordinaryThiefSituation[thiefID] = situation;
        ordinaryThiefAssaultPartyID[thiefID] = assaultPartyID;
        assaultPartyRoomID[assaultPartyID] = roomID--;
        ordinaryThiefPosition[thiefID] = position;
        ordinaryThiefCanvas[thiefID] = canvas;
        addToAssaultParty(assaultPartyID + 1, thiefID + 1);
        reportStatus();
    }

    /**
     * Reset all the variables of the ordinary thief
     * 
     * @param thiefID
     * @param state
     */
    @Override
    public synchronized void resetOrdinaryAll(int thiefID, int state) throws RemoteException {
        ordinaryThiefState[thiefID] = state;
        ordinaryThiefSituation[thiefID] = 'W';
        ordinaryThiefAssaultPartyID[thiefID] = 0;
        ordinaryThiefPosition[thiefID] = 0;
        ordinaryThiefCanvas[thiefID] = 0;
        addToAssaultParty(0, thiefID + 1);
        reportStatus();
    }

    /**
     * Set the room of the assault party
     * 
     * @param assaultPartyID
     * @param roomID
     */
    @Override
    public synchronized void setAssaultPartyRoom(int assaultPartyID, int roomID) throws RemoteException {
        assaultPartyRoomID[assaultPartyID] = roomID;
        reportStatus();
    }

    /**
     * Adds a thief to the assault party
     * 
     * @param assaultId
     * @param thiefId
     */
    private synchronized void addToAssaultParty(int assaultId, int thiefId) throws RemoteException {
        if (assaultId == 1) {
            for (int i = 0; i < SimulPar.K; i++) {
                if (assaultParty1Thieves[i] == 0) {
                    assaultParty1Thieves[i] = thiefId;
                    return;
                }
            }
        } else if (assaultId == 2) {
            for (int i = 0; i < SimulPar.K; i++) {
                if (assaultParty2Thieves[i] == 0) {
                    assaultParty2Thieves[i] = thiefId;
                    return;
                }
            }
        } else {
            for (int i = 0; i < SimulPar.K; i++) {
                if (assaultParty1Thieves[i] == thiefId) {
                    assaultParty1Thieves[i] = 0;
                    return;
                }
            }
            for (int i = 0; i < SimulPar.K; i++) {
                if (assaultParty2Thieves[i] == thiefId) {
                    assaultParty2Thieves[i] = 0;
                    return;
                }
            }
        }
    }

    /**
     * End the assault
     * Prints the number of stolen canvas and the legend
     */
    @Override
    public synchronized void endAssault() throws RemoteException {
        try {
            FileWriter writer = new FileWriter(logFileName, true);
            writer.write("My friends, tonight's effort produced " + stolenCanvas + " priceless paintings. \n\n");
            writer.write("Legend: \n");
            writer.write("MstT Stat    - state of the master thief \n");
            writer.write("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)\n");
            writer.write(
                    "Thief # S    - situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)\n");
            writer.write(
                    "Thief # MD   - maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6\n");
            writer.write(
                    "Assault party # RId        - assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)\n");
            writer.write(
                    "Assault party # Elem # Id  - assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)\n");
            writer.write(
                    "Assault party # Elem # Pos - assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)\n");
            writer.write(
                    "Assault party # Elem # Cv  - assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)\n");
            writer.write(
                    "Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls\n");
            writer.write(
                    "Museum Room # DT - room identification (1 .. 5) distance from outside gatherind site, a random number between 15 and 30");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Operation to shut down the AssaultParty server
     */
    @Override
    public synchronized void shutdown() throws RemoteException{
        ServerGeneralRepo.shutdown();
    }
    
}
