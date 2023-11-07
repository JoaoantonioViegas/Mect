package clientSide.entities;

import clientSide.stubs.*;

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
    private MuseumStub museumStub;

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
    public OrdinaryThief(int ID, int agility, MuseumStub museumStub, ConcentrationSiteStub ConcentrationSiteStub,
            ControlSiteStub controlSiteStub, AssaultPartyStub[] assaultPartyStub) {
        this.ID = ID;
        this.agility = agility;
        this.thiefState = OrdinaryThiefStates.WAITING_UNTIL_NEEDED;
        this.hasCanvas = false;
        this.assaultPartyID = -1;
        this.roomId = -1;
        this.museumStub = museumStub;
        this.concentrationSiteStub = ConcentrationSiteStub;
        this.controlSiteStub = controlSiteStub;
        this.assaultPartyStub = assaultPartyStub;
    }

    /**
     * Ordinary Thief life cycle
     */

    @Override
    public void run() {
        System.out.println("Thief + " + this.ID + " is running");
        while (concentrationSiteStub.amINeeded() != false) {
            System.out.println("Thief + " + this.ID + " is needed");
            int ids[] = new int[2];
            ids = concentrationSiteStub.prepareExcursion();

            this.assaultPartyID = ids[0];
            this.roomId = ids[1];

            assaultPartyStub[assaultPartyID].crawlIn(roomId, this.assaultPartyID, this.agility);

            this.hasCanvas = museumStub.rollACanvas(this.roomId);

            assaultPartyStub[assaultPartyID].reverseDirection(this.assaultPartyID);

            assaultPartyStub[assaultPartyID].crawlOut(roomId, this.assaultPartyID, this.agility);

            controlSiteStub.handACanvas(this.hasCanvas, this.assaultPartyID, this.roomId);
        }
    }

}