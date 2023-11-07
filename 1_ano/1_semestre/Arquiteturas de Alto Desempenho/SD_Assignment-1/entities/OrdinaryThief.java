package entities;

import sharedRegions.*;

public class OrdinaryThief extends Thread {

    /**
     * Thief ID
     */
    private int ID;

    /**
     * Thief state
     */
    private int thiefState;

    /**
     * Thief assault group ID
     */
    private int assaultPartyID;

    /**
     * Thief maximum displacement between other thieves
     */
    private int agility;

    /**
     * If the thief as a canvas
     */
    private boolean hasCanvas;

    /**
     * Reference to the room id
     */

    private int roomId;

    /**
     * @param new state of the thief
     */
    public void setThiefState(int thiefState) {
        this.thiefState = thiefState;
    }

    /**
     * @return thief state
     */
    public int getThiefState() {
        return thiefState;
    }

    /**
     * @return agliity
     */
    public int getAgility() {
        return agility;
    }

    /**
     * @return thief ID
     */
    public int getThiefId() {
        return ID;
    }

    /**
     * @return thief assault group ID
     */
    public int getAssaultPartyId() {
        return assaultPartyID;
    }

    /**
     * Reference to the museum
     */
    private Museum museum;

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
     * Reference to the General Repository
     */

    GeneralRepo repo;

    /**
     * Ordinary Thief instantiation
     * 
     * @param ID
     * @param agility
     * @param repo
     * @param museum
     * @param ConcentrationSite
     * @param ControlCollectionSite
     * @param assaultParty
     */
    public OrdinaryThief(int ID, int agility, GeneralRepo repo, Museum museum, ConcentrationSite ConcentrationSite,
            ControlCollectionSite ControlCollectionSite, AssaultParty[] assaultParty) {
        this.ID = ID;
        this.agility = agility;
        this.thiefState = OrdinaryThiefStates.WAITING_UNTIL_NEEDED;
        this.hasCanvas = false;
        this.assaultPartyID = -1;
        this.roomId = -1;
        this.museum = museum;
        this.concentrationSite = ConcentrationSite;
        this.controlCollectionSite = ControlCollectionSite;
        this.assaultParty = assaultParty;
        this.repo = repo;
    }

    /**
     * Ordinary Thief life cycle
     */

    @Override
    public void run() {
        repo.setOrdinaryThiefState(this.ID, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);
        while (concentrationSite.amINeeded() != false) {

            int ids[] = new int[2];
            ids = concentrationSite.prepareExcursion();

            this.assaultPartyID = ids[0];
            this.roomId = ids[1];

            assaultParty[assaultPartyID].crawlIn(roomId);

            this.hasCanvas = museum.rollACanvas(this.roomId);

            assaultParty[assaultPartyID].reverseDirection();

            assaultParty[assaultPartyID].crawlOut(roomId);

            controlCollectionSite.handACanvas(this.hasCanvas, this.assaultPartyID, this.roomId);
        }
    }

}