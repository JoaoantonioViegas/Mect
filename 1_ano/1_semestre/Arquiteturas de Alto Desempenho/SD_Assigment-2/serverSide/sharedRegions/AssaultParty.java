package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.stubs.GeneralRepoStub;
import serverSide.main.ServerAssaultParty;
import serverSide.main.SimulPar;
import clientSide.entities.OrdinaryThiefStates;
/**
 * Assault Party (shared region)
 * 
 * This class implements the assault party shared region.
 */

public class AssaultParty {

    /**
	 * Number of entity groups requesting the shutdown
	 */
	private int nEntities;

    /**
     * Reference to the distance to the target rooms
     */

    private int distanceToRoom[];

    /**
     * Reference to the maximum distance between thieves
     */

    private int MAXdistbetweenthieves;

    /**
     * Reference the path to the target room
     */

    private int[] path = new int[3];

    /**
     * Reference to the thieves in the assault party
     */

    private AssaultPartyClientProxy[] thieves = new AssaultPartyClientProxy[SimulPar.K];

    /**
     * Reference to all the thieves
     */

    private AssaultPartyClientProxy[] ordinaryThiefs = new AssaultPartyClientProxy[SimulPar.M - 1];

    /**
     * Reference to each thief if he can leave the current room
     */

    private boolean[] canLeave = new boolean[SimulPar.M - 1];

    /**
     * Reference to the thieves real ids
     */

    private int[] thieveIds = new int[SimulPar.K];

    /**
     * Reference to if the master thief can send the assault party
     */

    boolean masterCanSend = false;

    /**
     * Reference to the thieves waiting to crawl in
     */

    private boolean[] thievesWaitingInAssaultParty = new boolean[SimulPar.M - 1];

    /**
     * Reference to the thieves waiting outside
     */

    private boolean[] thievesWaitingOutside = new boolean[SimulPar.M - 1];

    /**
     * Reference to the number of thieves waiting outside
     */

    int numTheivesWaitingInOutside = 0;

    /**
     * Reference to General Repository
     */

    private GeneralRepoStub generalRepository;

    /**
     * Constructor for the Assault Party
     * 
     * @param MAXdistbetweenthieves
     * @param generalRepository
     */

    public AssaultParty( int MAXdistbetweenthieves, GeneralRepoStub generalRepository) {
        this.MAXdistbetweenthieves = MAXdistbetweenthieves;
        for (int i = 0; i < thievesWaitingInAssaultParty.length; i++) {
            thievesWaitingInAssaultParty[i] = false;
        }
        for (int i = 0; i < thievesWaitingOutside.length; i++) {
            thievesWaitingOutside[i] = false;
        }
        numTheivesWaitingInOutside = 0;
        for (int i = 0; i < SimulPar.M - 1; i++) {
            canLeave[i] = false;
        }
        for (int i = 0; i < thieveIds.length; i++) {
            thieveIds[i] = -1;
        }
        for (int i = 0; i < SimulPar.M - 1; i++) {
            ordinaryThiefs[i] = null;
        }
        setAssaultPartyPosition(0);
        this.generalRepository = generalRepository;
    }

    public void setDistanceToRoom(int[] distanceToRoom) {
        this.distanceToRoom = distanceToRoom;
        for (int i = 0; i < distanceToRoom.length; i++) {
            System.out.println(distanceToRoom[i]);
        }
    }

    /**
     * Method to set the assault party position
     * Sets the position of all the thieves in the assault party
     * 
     * @param value
     */

    private void setAssaultPartyPosition(int value) {
        for (int i = 0; i < path.length; i++) {
            path[i] = value;
        }
    }

    /**
     * Method to get the position of the thief closest to the target room
     * 
     * @return
     */

    private int getBiggest() {
        int biggest = 0;
        for (int i = 0; i < path.length; i++) {
            if (path[i] > biggest) {
                biggest = path[i];
            }
        }
        return biggest;
    }

    /**
     * Returns the second biggest value in the path array that is not equal to the
     * value passed as argument
     * 
     * @param value
     * @return
     */

    private int getsecondMax(int value) {
        int biggest = 0;
        for (int i = 0; i < path.length; i++) {
            if (path[i] > biggest && path[i] != value) {
                biggest = path[i];
            }
        }
        return biggest;
    }

    /**
     * Method to get the position of the thief farthest from the target room
     * 
     * @return
     */

    private int getLowest() {
        int lowest = 100;
        for (int i = 0; i < path.length; i++) {
            if (path[i] < lowest) {
                lowest = path[i];
            }
        }
        return lowest;
    }

    /**
     * Checks who is the thief in the middle of the three
     * 
     * @return
     */

    private int getMedium() {
        int a = path[0];
        int b = path[1];
        int c = path[2];

        // get the lowest of the three
        int lowest;
        if (a < b && a < c) {
            lowest = a;
        } else if (b < a && b < c) {
            lowest = b;
        } else {
            lowest = c;
        }

        // if the lowest is a, get the lowest of b and c
        if (lowest == a) {
            if (b < c) {
                return b;
            } else {
                return c;
            }
        }

        // if the lowest is b, get the lowest of a and c
        if (lowest == b) {
            if (a < c) {
                return a;
            } else {
                return c;
            }
        }

        // if the lowest is c, get the lowest of a and b
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * Method to check if the position passed as argument is in the path array
     * and if the position is not equal to the distance to the target room
     * 
     * @param pos
     * @param roomId
     * @return true if the position is in the path array
     */

    private boolean checkPath(int pos, int roomId) {
        for (int i = 0; i < this.path.length; i++) {
            if (this.path[i] == pos && pos != distanceToRoom[roomId]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if the position passed as argument is in the path array
     * and if the position is not equal to 0
     * 
     * @param pos
     * @return true if the position is in the path array
     */

    private boolean checkPathBack(int pos) {
        for (int i = 0; i < this.path.length; i++) {
            if (this.path[i] == pos && pos != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to get the relative position of the thief in the given position
     * 
     * @param value
     * @return 1 if the value is the biggest, -1 if the value is the lowest and 0 if
     *         the value is in the middle
     */

    private int relativePosition(int value) {
        int lowest = 100;
        int biggest = 0;
        for (int i = 0; i < path.length; i++) {
            if (path[i] < lowest) {
                lowest = path[i];
            }
            if (path[i] > biggest) {
                biggest = path[i];
            }
        }
        if (value == lowest) {
            return -1;
        }
        if (value == biggest) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns the closest value to the value passed as argument that is not equal
     * to
     * 
     * @param value
     * @return
     */

    private int getlowerClosest(int value) {
        int lowest = 100;
        int result = 0;
        for (int i = 0; i < path.length; i++) {
            if (path[i] - value < lowest && path[i] - value != 0) {
                lowest = path[i] - value;
                result = path[i];
            }
        }
        return result;
    }

    /**
     * Checks if the thief can move forward
     * 
     * @param thiefId
     * @param roomId
     * @return
     */
    private boolean canMoveForward(int thiefId, int roomId) {
        int position = relativePosition(path[thiefId]);
        int secondMax = getsecondMax(path[thiefId]);
        int lowest = getLowest();

        if (path[thiefId] >= distanceToRoom[roomId]) {
            return false;
        }
        if (position == 1) {
            if (path[thiefId] + MAXdistbetweenthieves == MAXdistbetweenthieves) {
            } else if (path[thiefId] - secondMax >= MAXdistbetweenthieves) {
                return false;
            } else {

                return true;
            }
        } else if (position == 0) {
            if (path[thiefId] - lowest > MAXdistbetweenthieves) {
                return false;
            }
        } else if (position == -1) {
            return true;
        }
        return true;
    }

    /**
     * Checks if the thief can move backwards
     * 
     * @param thiefId
     * @param roomId
     * @return
     */

    private boolean canMoveBackwards(int thiefId, int roomId) {
        int position = relativePosition(path[thiefId]);
        int secondMax = getMedium();
        int biggest = getBiggest();
        if (path[thiefId] <= 0) {
            return false;
        }
        if (path[thiefId] == distanceToRoom[roomId])
            return true;
        if (position == 1) {
            return true;
        } else if (position == 0) {
            if (path[thiefId] - biggest >= MAXdistbetweenthieves) {
                return false;
            }
        } else if (position == -1) {
            if (path[thiefId] - MAXdistbetweenthieves == distanceToRoom[roomId]) {
                return false;
            } else if (secondMax - path[thiefId] >= MAXdistbetweenthieves) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * Method to move the thief forward
     * 
     * @param thiefId
     * @param dist
     * @return
     */

    private int moveForward(int thiefId, int dist) {
        for (int i = 0; i < path.length; i++) {
            if (i == thiefId) {
                path[i] = path[i] + dist;
            }
        }
        return path[thiefId];
    }

    /**
     * Method to move the thief backwards
     * 
     * @param thiefId
     * @param dist
     * @return
     */

    private int moveBackwards(int thiefId, int dist) {
        for (int i = 0; i < path.length; i++) {
            if (i == thiefId) {
                path[i] = path[i] - dist;
                if (path[i] < 0) {
                    path[i] = 0;
                }
            }
        }
        return path[thiefId];
    }

    /**
     * Method to check if all the thieves are in the target room or the collection
     * site
     * 
     * @param distance
     * @return
     */

    private boolean endofpath(int distance) {
        for (int i = 0; i < path.length; i++) {
            if (path[i] != distance) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check if the thief is in the target room
     * 
     * @param thiefId
     * @param roomId
     * @return
     */

    private boolean checkThiefInTargetRoom(int thiefId, int roomId) {
        if (path[thiefId] >= distanceToRoom[roomId]) {
            return true;
        }
        return false;
    }

    /**
     * Method to check if the thief is in the collection site
     * 
     * @param thiefId
     * @return
     */

    private boolean checkThiefInCollectionSite(int thiefId) {
        if (path[thiefId] <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Method to get the maximum distance a thief can move forward
     * 
     * @param thiefId
     * @param agility
     * @return
     */

    private int getMostPossibleDistanceForward(int thiefId, int agility) {
        int position = relativePosition(path[thiefId]);
        int secondMax = getsecondMax(path[thiefId]);
        int biggest = getBiggest();
        int lowest = getLowest();
        int distBetweenFirstAndSecond = 0;
        int dist = 0;
        int closest = getlowerClosest(path[thiefId]);
        if (position == 1) {
            distBetweenFirstAndSecond = path[thiefId] - secondMax;
            dist = MAXdistbetweenthieves - distBetweenFirstAndSecond;
        } else if (position == 0) {
            int distanceBetweenSecondAndThird = path[thiefId] - lowest;
            dist = MAXdistbetweenthieves - distanceBetweenSecondAndThird;
            if (biggest - lowest <= MAXdistbetweenthieves) {
                dist = biggest + MAXdistbetweenthieves - path[thiefId];
            }

        } else if (position == -1) {
            dist = closest + MAXdistbetweenthieves - path[thiefId];
            if (path[thiefId] == 0) {
                dist = biggest + MAXdistbetweenthieves;
            }
        }
        if (dist > agility) {
            dist = agility;
        }
        return dist;

    }

    /**
     * Method to get the maximum distance a thief can move backwards
     * 
     * @param thiefId
     * @param agility
     * @param roomId
     * @return
     */

    private int getMostPossibleDistanceBackwards(int thiefId, int agility, int roomId) {
        int position = relativePosition(path[thiefId]);
        int biggest = getBiggest();
        int lowest = getLowest();
        int dist = 0;
        int closest = getlowerClosest(path[thiefId]);
        int medium = getMedium();
        if (path[thiefId] == distanceToRoom[roomId] && endofpath(distanceToRoom[roomId])) {
            return MAXdistbetweenthieves;
        }
        if (position == 1) {
            dist = path[thiefId] - medium + MAXdistbetweenthieves;
            if (path[thiefId] == distanceToRoom[roomId]) {
                dist = path[thiefId] - lowest + MAXdistbetweenthieves;
            }
        } else if (position == 0) {
            int maxPosition = biggest - MAXdistbetweenthieves;

            dist = path[thiefId] - maxPosition;
            if (biggest - lowest <= MAXdistbetweenthieves) {
                dist = path[thiefId] - lowest + MAXdistbetweenthieves;
            }
        } else if (position == -1) {
            int maxPosition = closest - MAXdistbetweenthieves;
            dist = path[thiefId] - maxPosition;
            if (path[thiefId] == 0) {
                dist = biggest + MAXdistbetweenthieves;
            }
        }
        if (dist > agility) {
            dist = agility;
        }
        return dist;

    }

    /**
     * Method to get the thief id that is in the position of the value
     * 
     * @param value
     * @return
     */

    private int getThiefId(int value) {
        for (int i = 0; i < path.length; i++) {
            if (path[i] == value) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Method to find out what thief needs to be waken up when the thiefs are
     * crawling in
     * 
     * @param thiefId
     * @return
     */

    private int wakeThiefForward(int thiefId) {
        int relativePosition = relativePosition(path[thiefId]);
        int secondMax = getsecondMax(path[thiefId]);
        int lowest = getLowest();
        int biggest = getBiggest();
        int id = 0;
        if (relativePosition == 1) {
            id = getThiefId(secondMax);
        }
        if (relativePosition == 0) {
            id = getThiefId(lowest);
        }
        if (relativePosition == -1) {
            id = getThiefId(biggest);
        }
        return id;
    }

    /**
     * Method to find out what thief needs to be waken up when the thiefs are
     * crawling out
     * 
     * @param thiefId
     * @return
     */

    private int wakeThiefbackward(int thiefId) {
        int relativePosition = relativePosition(path[thiefId]);
        int medium = getMedium();

        int lowest = getLowest();

        int biggest = getBiggest();

        int id = 0;
        if (relativePosition == 1) {
            id = getThiefId(lowest);
        }
        if (relativePosition == 0) {
            id = getThiefId(biggest);

        }
        if (relativePosition == -1) {
            if (path[thiefId] == 0) {
                id = getThiefId(biggest);
                return id;
            }
            id = getThiefId(medium);
        }
        return id;
    }

    /**
     * Method to add the thread id to the array of thread ids
     * 
     * @param thiefId
     * @return
     */

    private boolean addToIds(int thiefId) {
        for (int i = 0; i < thieveIds.length; i++) {
            if (thieveIds[i] == -1) {
                thieveIds[i] = thiefId;
                return true;
            }
        }
        return false;
    }

    /**
     * Method to transform the thief id to the thread id
     * 
     * @param thiefId
     * @return
     */

    private int transformId(int thiefId) {
        for (int i = 0; i < thieveIds.length; i++) {
            if (thieveIds[i] == thiefId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Called by the ordinary thief when he is crawling in
     * 
     * @param roomId
     */

    public synchronized void crawlIn(int roomId, int assaultId, int agility) {

        int ThreadthiefId = ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId();

        System.out.println("Thief " + ThreadthiefId + " is going to crawl in to the room " + roomId +" with agility " + agility);
        System.out.println("The distance to the room is " + distanceToRoom[roomId]);

        ordinaryThiefs[ThreadthiefId] = ((AssaultPartyClientProxy) Thread.currentThread());
        addToIds(ThreadthiefId);
        generalRepository.setOrdinaryAll(ThreadthiefId, OrdinaryThiefStates.CRAWLING_INWARDS, 'P', assaultId, roomId, 0,
                0);
        thievesWaitingInAssaultParty[ThreadthiefId] = true;

        int numWaiting = 0;
        for (int i = 0; i < thievesWaitingInAssaultParty.length; i++) {
            if (thievesWaitingInAssaultParty[i] == true) {
                numWaiting++;
            }
        }

        if (numWaiting == 3) {
            setAssaultPartyPosition(0);
            for (int i = 0; i < thievesWaitingInAssaultParty.length; i++) {
                thievesWaitingInAssaultParty[i] = false;
            }
            for (int i = 0; i < thieveIds.length; i++) {
                int thiefId = transformId(thieveIds[i]);
                thieves[thiefId] = (AssaultPartyClientProxy) Thread.currentThread();
                thieves[thiefId].setOrdinaryThiefState(OrdinaryThiefStates.WAITING_FOR_CRAWL_IN);
            }
            masterCanSend = true;
            notifyAll();
        } else {
            while (thievesWaitingInAssaultParty[ThreadthiefId] == true) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int thiefId = transformId(ThreadthiefId);

        while (true) {
            thiefId = transformId(((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId());
            if (thiefId == -1) {
                System.err.println("Thief " + ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId()
                        + " is not in the thieveIds array");
                System.exit(1);
            }

            while (ordinaryThiefs[ThreadthiefId].getOrdinaryThiefState() != OrdinaryThiefStates.CRAWLING_INWARDS
                    && ordinaryThiefs[ThreadthiefId].getOrdinaryThiefState() != OrdinaryThiefStates.AT_A_ROOM) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefState() == OrdinaryThiefStates.AT_A_ROOM) {
                break;
            }

            boolean canmove = canMoveForward(thiefId, roomId);
            while (canmove) {
                int dist = getMostPossibleDistanceForward(thiefId, agility);
                if (dist == 0)
                    break;
                Boolean check = checkPath(path[thiefId] + dist, roomId);
                while (check) {
                    dist--;
                    check = checkPath(path[thiefId] + dist, roomId);
                    if (dist == 0) {
                        break;
                    }
                }
                moveForward(thiefId, dist);
                canmove = canMoveForward(thiefId, roomId);
                if (checkThiefInTargetRoom(thiefId, roomId)) {
                    System.out.println("T" + ThreadthiefId + " -> POS: " + path[thiefId] + " agility: " + agility + " at room: " + roomId);
                    path[thiefId] = distanceToRoom[roomId];
                }
                System.out.println("T" + ThreadthiefId + " -> POS: " + path[thiefId] + " agility: " + agility + " at room: " + roomId);
                generalRepository.setOrdinaryThiefPosition(ThreadthiefId, path[thiefId]);
            }

            if (endofpath(distanceToRoom[roomId])) {
                for (int i = 0; i < SimulPar.K; i++) {
                    ordinaryThiefs[thieveIds[i]].setOrdinaryThiefState(OrdinaryThiefStates.AT_A_ROOM);
                }
                notifyAll();
                System.out.println("Thief " + ThreadthiefId + " is at a room");
                break;
            }

            int nextThiefId = wakeThiefForward(thiefId);
            ordinaryThiefs[thieveIds[nextThiefId]].setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
            ordinaryThiefs[thieveIds[thiefId]].setOrdinaryThiefState(OrdinaryThiefStates.WAITING_FOR_CRAWL_IN);
            notifyAll();
        }
    }

    /**
     * Called by the ordinary thief when he is crawling out
     * 
     * @param roomId
     */

    public synchronized void crawlOut(int roomId, int assaultId, int agility) {
        
        int ThreadthiefId = ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        ordinaryThiefs[ThreadthiefId] = ((AssaultPartyClientProxy) Thread.currentThread());

        System.out.println("Thief " + ThreadthiefId + " is going to crawl out of the room " + roomId +" with agility " + agility);
        System.out.println("The distance to the room is " + distanceToRoom[roomId]);

        generalRepository.setOrdinaryThiefState(ThreadthiefId, OrdinaryThiefStates.CRAWLING_OUTWARDS);

        addToIds(ThreadthiefId);

        thievesWaitingOutside[ThreadthiefId] = true;

        numTheivesWaitingInOutside++;

        if (numTheivesWaitingInOutside == SimulPar.K) {
            numTheivesWaitingInOutside = 0;
            for (int i = 0; i < SimulPar.M - 1; i++) {
                if (thievesWaitingOutside[i]) {
                    thievesWaitingOutside[i] = false;
                    ordinaryThiefs[i].setOrdinaryThiefState(OrdinaryThiefStates.WAITING_FOR_CRAWL_OUT);
                }
            }
            ordinaryThiefs[ThreadthiefId].setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
            notifyAll();
        } else {
            while (thievesWaitingOutside[ThreadthiefId] == true) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int thiefId = transformId(ThreadthiefId);

        while (true) {
            thiefId = transformId(((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId());
            while (ordinaryThiefs[ThreadthiefId].getOrdinaryThiefState() != OrdinaryThiefStates.CRAWLING_OUTWARDS
                    && canLeave[(((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId())] == false) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (canLeave[(((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId())] == true) {
                thieveIds[thiefId] = -1;
                canLeave[(((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId())] = false;
                return;
            }
            boolean canmove = canMoveBackwards(thiefId, roomId);

            while (canmove) {
                canLeave[thieveIds[thiefId]] = false;
                int dist = getMostPossibleDistanceBackwards(thiefId, agility, roomId);
                if (dist == 0)
                    break;
                Boolean check = checkPathBack(path[thiefId] - dist);
                while (check) {
                    dist--;
                    check = checkPathBack(path[thiefId] - dist);
                    if (dist == 0) {
                        break;
                    }
                }
                moveBackwards(thiefId, dist);
                canmove = canMoveBackwards(thiefId, roomId);
                if (checkThiefInCollectionSite(thiefId)) {
                    path[thiefId] = 0;
                    System.out.println("T" + ThreadthiefId + " -> POS: " + path[thiefId] + " agility: " + agility + " at room: " + roomId);
                    break;
                }
                System.out.println("T" + ThreadthiefId + " -> POS: " + path[thiefId] + " agility: " + agility + " at room: " + roomId);
                generalRepository.setOrdinaryThiefPosition(ThreadthiefId, path[thiefId]);

            }

            if (endofpath(0)) {
                for (int i = 0; i < SimulPar.K; i++) {
                    canLeave[thieveIds[i]] = true;
                }
                setAssaultPartyPosition(0);
                break;
            }

            int nextThiefId = wakeThiefbackward(thiefId);
            ordinaryThiefs[thieveIds[nextThiefId]].setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
            ordinaryThiefs[thieveIds[thiefId]].setOrdinaryThiefState(OrdinaryThiefStates.WAITING_FOR_CRAWL_OUT);
            notifyAll();
        }
        canLeave[thieveIds[thiefId]] = false;
        thieveIds[thiefId] = -1;
        notifyAll();
    }

    /**
     * Called by the master thief when he is ready to send the assault party
     */

    public synchronized void sendAssaultParty() {
        while (!masterCanSend) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < SimulPar.M - 1; i++) {
            thievesWaitingInAssaultParty[i] = false;
        }
        masterCanSend = false;
        thieves[0].setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
        notifyAll();
    }

    /**
     * Called by the ordinary thief to reverse the direction
     */

    public synchronized void reverseDirection() {
        int ThreadthiefId = ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        ordinaryThiefs[ThreadthiefId] = (AssaultPartyClientProxy) Thread.currentThread();
        ordinaryThiefs[ThreadthiefId].setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
    }


    /**
     * Operation to shut down the AssaultParty server
     */

    public synchronized void shutdown(){
        ServerAssaultParty.waitConnection = false;
    }
}