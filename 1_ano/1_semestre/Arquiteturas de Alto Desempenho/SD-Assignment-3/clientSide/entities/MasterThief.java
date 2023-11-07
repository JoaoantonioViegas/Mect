package clientSide.entities;

import interfaces.*;
import java.rmi.RemoteException;
import genclass.GenericIO;

public class MasterThief extends Thread {

    /**
     * Master Thief state
     */
    private int state;

    /**
     * Reference to the concentration site
     */

    private ConcentrationSiteInterface concentrationSiteStub;

    /**
     * Reference to the control collection site
     */

    private ControlSiteInterface controlSiteStub;

    /**
     * Reference to the assault party
     */

    private AssaultPartyInterface[] assaultPartyStub;



    /**
     * Master Thief instantiation
     */
    public MasterThief( ConcentrationSiteInterface concentrationSiteStub,
            ControlSiteInterface controlSiteStub, AssaultPartyInterface[] assaultPartyStub) {
        super("Master_Thief");
        this.state = MasterThiefStates.PLANNING_THE_HEIST;
        this.concentrationSiteStub = concentrationSiteStub;
        this.controlSiteStub = controlSiteStub;
        this.assaultPartyStub = assaultPartyStub;

    }

    /**
     * @param new state of the thief
     */
    public void setMasterThiefState(int state) {
        this.state = state;
    }

    /**
     * @return thief state
     */
    public int getMasterThiefState() {
        return state;
    }

    /**
     * Master Thief life cycle
     */

    @Override
    public void run() {
        char oper;
        startOfOperation();
        // repoStub.setMasterThiefState(MasterThiefStates.PLANNING_THE_HEIST);
        while ((oper = appraiseSit()) != 'E') {
            switch (oper) {
                case 'P':
                    int assaultId = getAssaultPartyID();
                    int roomId = getRoomWithCanvasID();
                    prepareAssaultParty(assaultId, roomId);
                    sendAssaultParty(assaultId);
                    break;

                case 'R':
                    takeARest();
                    collectACanvas();
                    break;

            }
        }
        int numCanvas = getCollectedCanvas();
        sumUpResults(numCanvas);
    }

    /**
     * Operation start of operations
     */

    private void startOfOperation() {
        try {
            controlSiteStub.startOfOperation();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Operation appraise situation
     * 
     * @return operation code
     */

    private char appraiseSit() {
        ReturnChar oper = null;

        try {
            oper = controlSiteStub.appraiseSit();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        state = oper.getState();
        char operation = oper.getValue();
        return operation;
    }

    /**
     * Operation get assault party id
     * 
     * @return assault party id
     */

    private int getAssaultPartyID() {
        int assaultId = 0;
        try {
            assaultId = controlSiteStub.getAssaultPartyID();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        return assaultId;
    }

    /**
     * Operation get room id
     * 
     * @return room id
     */

    private int getRoomWithCanvasID() {
        int roomId = 0;
        try {
            roomId = controlSiteStub.getRoomWithCanvasID();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        return roomId;
    }

    /**
     * Operation prepare assault party
     * 
     * @param assaultId assault party id
     * @param roomId    room id
     */

    private void prepareAssaultParty(int assaultId, int roomId) {
        try {
            concentrationSiteStub.prepareAssaultParty(assaultId, roomId);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Operation send assault party
     * 
     * @param assaultId assault party id
     */

    private void sendAssaultParty(int assaultId){
        try {
            assaultPartyStub[assaultId].sendAssaultParty(assaultId);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /*
     * Operation take a rest
     */

    private void takeARest() {
        try {
            state = controlSiteStub.takeARest();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /*
     * Operation collect a canvas
     */
    
    private void collectACanvas() {
        try {
            state = controlSiteStub.collectACanvas();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /*
     * Operation get collected canvas
     * 
     * @return number of canvas collected
     */

    private int getCollectedCanvas() {
        int numCanvas = 0;
        try {
            numCanvas = controlSiteStub.getCollectedCanvas();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        return numCanvas;
    }

    /*
     * Operation sum up results
     * 
     * @param numCanvas number of canvas collected
     */

    private void sumUpResults(int numCanvas) {
        try {
            concentrationSiteStub.sumUpResults(numCanvas);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

}
