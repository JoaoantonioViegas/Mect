package clientSide.entities;

import clientSide.stubs.*;

public class MasterThief extends Thread {

    /**
     * Master Thief state
     */
    private int state;

    /**
     * Reference to the concentration site
     */

    private ConcentrationSiteStub concentrationSiteStub;

    /**
     * Reference to the control collection site
     */

    private ControlSiteStub controlSiteStub;

    /**
     * Reference to the assault party
     */

    private AssaultPartyStub[] assaultPartyStub;



    /**
     * Master Thief instantiation
     */
    public MasterThief( ConcentrationSiteStub concentrationSiteStub,
            ControlSiteStub controlSiteStub, AssaultPartyStub[] assaultPartyStub) {
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
        controlSiteStub.startOfOperation();
        // repoStub.setMasterThiefState(MasterThiefStates.PLANNING_THE_HEIST);
        while ((oper = controlSiteStub.appraiseSit()) != 'E') {
            switch (oper) {
                case 'P':
                    int assaultId = controlSiteStub.getAssaultPartyID();
                    concentrationSiteStub.prepareAssaultParty(assaultId, controlSiteStub.getRoomWithCanvasID());
                    assaultPartyStub[assaultId].sendAssaultParty(assaultId);
                    break;

                case 'R':
                    controlSiteStub.takeARest();
                    controlSiteStub.collectACanvas();
                    break;

            }
        }
        concentrationSiteStub.sumUpResults(controlSiteStub.getCollectedCanvas());
    }

}
