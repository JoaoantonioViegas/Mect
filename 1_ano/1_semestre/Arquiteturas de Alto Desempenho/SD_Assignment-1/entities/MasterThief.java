package entities;

import sharedRegions.*;

public class MasterThief extends Thread {

    /**
     * Master Thief state
     */
    private int state;

    /**
     * Reference to the concentration site
     */

    private ConcentrationSite concentrationSite;

    /**
     * Reference to the control collection site
     */

    private ControlCollectionSite controlCollectionSite;

    /**
     * Reference to the assault party
     */

    private AssaultParty[] assaultParty;

    /**
     * Reference to the general repository
     */

    private GeneralRepo repo;

    /**
     * Master Thief instantiation
     */
    public MasterThief(GeneralRepo repo, ConcentrationSite concentrationSite,
            ControlCollectionSite controlCollectionSite, AssaultParty[] assaultParty) {
        super("Master_Thief");
        this.state = MasterThiefStates.PLANNING_THE_HEIST;
        this.concentrationSite = concentrationSite;
        this.controlCollectionSite = controlCollectionSite;
        this.assaultParty = assaultParty;
        this.repo = repo;
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
        controlCollectionSite.startOfOperation();
        repo.setMasterThiefState(MasterThiefStates.PLANNING_THE_HEIST);
        while ((oper = controlCollectionSite.appraiseSit()) != 'E') {
            switch (oper) {
                case 'P':
                    int assaultId = controlCollectionSite.getAssaultPartyID();
                    concentrationSite.prepareAssaultParty(assaultId, controlCollectionSite.getRoomWithCanvasID());
                    assaultParty[assaultId].sendAssaultParty();
                    break;

                case 'R':
                    controlCollectionSite.takeARest();
                    controlCollectionSite.collectACanvas();
                    break;

            }
        }
        concentrationSite.sumUpResults(controlCollectionSite.getCollectedCanvas());
    }

}
