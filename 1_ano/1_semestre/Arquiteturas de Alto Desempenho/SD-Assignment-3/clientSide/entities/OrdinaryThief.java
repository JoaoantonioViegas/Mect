package clientSide.entities;

import interfaces.*;
import java.rmi.RemoteException;
import genclass.GenericIO;
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
    private MuseumInterface museumStub;

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
    public OrdinaryThief(int ID, int agility, MuseumInterface museumStub, ConcentrationSiteInterface ConcentrationSiteStub,
            ControlSiteInterface controlSiteStub, AssaultPartyInterface[] assaultPartyStub) {
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
        while (amINeeded() != false) {
            int ids[] = new int[2];
            ids = prepareExcursion();

            this.assaultPartyID = ids[0];
            this.roomId = ids[1];

            crawlIn(roomId, this.assaultPartyID, this.agility);

            this.hasCanvas = rollACanvas(this.roomId);

            reverseDirection(this.assaultPartyID);

            crawlOut(roomId, this.assaultPartyID, this.agility);

            handACanvas(this.hasCanvas, this.assaultPartyID, this.roomId);
        }
    }

    /**
     * Operation am i needed
     * 
     * @return true if the thief is needed, false otherwise
     */

    private boolean amINeeded() {
        ReturnBoolean res = null; 
        try{
            res = concentrationSiteStub.amINeeded(this.ID);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        thiefState = res.getIntStateVal();
        return res.getBooleanVal();
    }

    /**
     * Operation prepare excursion
     * 
     * @return array with the assault party ID and the room ID
     */

    private int[] prepareExcursion() {
        int[] res = null;
        try{
            res = concentrationSiteStub.prepareExcursion(this.ID);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        return res;
    }

    /**
     * Operation crawl in
     * 
     * @param roomId
     * @param assaultPartyID
     * @param agility
     */

    private void crawlIn(int roomId, int assaultPartyID, int agility) {
        try{
            thiefState = assaultPartyStub[assaultPartyID].crawlIn(this.ID, roomId, assaultPartyID, agility);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Operation crawl out
     * 
     * @param roomId
     * @param assaultPartyID
     * @param agility
     */

    private void crawlOut(int roomId, int assaultPartyID, int agility) {
        try{
            thiefState = assaultPartyStub[assaultPartyID].crawlOut(this.ID, roomId, assaultPartyID, agility);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Operation reverse direction
     * 
     * @param assaultPartyID
     */

    private void reverseDirection(int assaultPartyID) {
        try{
            thiefState = assaultPartyStub[assaultPartyID].reverseDirection(this.ID);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Operation roll a canvas
     * 
     * @param roomId
     * @return true if the thief has a canvas, false otherwise
     */

    private boolean rollACanvas(int roomId) {
        boolean res = false;
        try{
            res = museumStub.rollACanvas(this.ID, roomId);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        return res;
    }

    /**
     * Operation hand a canvas
     * 
     * @param hasCanvas
     * @param assaultPartyID
     * @param roomId
     */

    private void handACanvas(boolean hasCanvas, int assaultPartyID, int roomId) {
        try{
            controlSiteStub.handACanvas(this.ID, hasCanvas, assaultPartyID, roomId);
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }


}