package commInfra;

import java.io.*;

import clientSide.entities.MasterThiefStates;
import clientSide.entities.OrdinaryThiefStates;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable
{
  /**
   *  Serialization key.
   */

   private static final long serialVersionUID = 2021L;

  /**
   *  Message type.
   */

   private int msgType = -1;


  /**
   *  Ordinary Thief identification.
   */

   private int thiefId = -1;


  /**
   *  Master state.
   */

   private int masterState = -1;

   /**
    * Ordinary state.
    */

   private int thiefState = -1;

  /**
   *  End of operations (master).
   */

   private boolean endOp = false;

  /**
   *  Name of the logging file.
   */

   private String fName = null;

  /**
   *  Number of iterations of the ordinary thief life cycle.
   */

   private int nIter = -1;

   /**
    * Ordinary thief is needed
    */

   private boolean isNeeded = true;

   /**
    * Ordinary thief RoomId
    */

   private int thiefRoomId = -1;

   /**
    * Ordinary thief Assault party id
    */

   private int thiefAssaultPartyId = -1;

   /**
    * Ordinary thief Canvas
    */

   private boolean thiefCanvas = false;

   /*
    * Thief canvas alternative
    */
   private int thiefCanvasAlt = -1;

   /*
    * Ordinary thief situation
    */

   private char thiefSituation;

   /*
    * Ordinary thief position
    */

   private int thiefPos;

   /**
    * Ordinary thief agility
    */

   private int thiefAgility;

   /**
    * Master assault party id
    */

   private int masterAssaultPartyId;

   /**
    * Master room id
    */

   private int masterRoomId;

   /*
    * Master operation
    */

   private char masterOperation;

   /**
    * Number of stolen paintings
    */

   private int numStolenPaintings = -1;

   /**
    * Agility of thieves
    */

   private int[] thievesAgility;

   /**
    * Distance to rooms
    */

   private int[] distanceToRoom;

   /**
    * Canvas in each room
    */

   private int[] canvasInRoom;

  /**
   *  Message instantiation (form 1).
   *
   *     @param type message type
   */

   public Message (int type)
   {
      msgType = type;

      if(type == MessageType.TOTALCANVAS){
         this.masterState = MasterThiefStates.PRESENTING_THE_REPORT;
      } else if(type == MessageType.HASRESTED){
         this.masterState = MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL;
      }
   }

  /**
   *  Message instantiation (form 2).
   *
   *     @param type message type
   *     @param id master / ordinary identification / assault party id
   *     @param stateOrId master / ordinary state / room id
   */

   public Message (int type, int id, int stateOrId)
   {
      this.msgType = type;
      if (type == MessageType.PREPAREASSAULT || type == MessageType.READYTOSEND){
         this.masterAssaultPartyId = id;
         this.masterRoomId = stateOrId;
         this.masterState = MasterThiefStates.ASSEMBLING_A_GROUP;
      } else if (type == MessageType.ROLLACANVAS){
         this.thiefId = id;
         this.thiefRoomId = stateOrId;
         this.thiefState = OrdinaryThiefStates.AT_A_ROOM;
      } else if (type == MessageType.SETAPROOM){
         this.thiefAssaultPartyId = id;
         this.thiefRoomId = stateOrId;
      } else if (type == MessageType.SETOPOS){
         this.thiefId = id;
         this.thiefPos = stateOrId;
      } else {
         this.thiefId = id;
         this.thiefState = stateOrId;
      }
   }

  /**
   *  Message instantiation (form 3).
   *
   *     @param type message type
   *     @param value thief identification / num Canvas / room id / assault party id
   */

   public Message (int type, int value)
   {
      this.msgType = type;
      
      if(type == MessageType.SUMUPRESULTS || type == MessageType.TOTALCANVAS){
         this.numStolenPaintings = value;
         this.masterState = MasterThiefStates.PRESENTING_THE_REPORT;
      } else if(type == MessageType.ROOMID){
         this.masterRoomId = value;
      } else if (type == MessageType.ASSAULTID || type == MessageType.SENDASSAULT){
         this.masterAssaultPartyId    = value;
      } else if (type == MessageType.SENDASSAULT || type == MessageType.ASSAULTSENT){
         this.masterAssaultPartyId = value;
      } else if (type == MessageType.HASREVERSED){
         this.thiefId = value;
         this.thiefState = OrdinaryThiefStates.CRAWLING_OUTWARDS;
      } else if (type == MessageType.HASCOLLECTED || type == MessageType.STARTOP){
         this.masterState = MasterThiefStates.DECIDING_WHAT_TO_DO;
      } else if (type == MessageType.COLLECTCANVAS || type == MessageType.SETMS){
         this.masterState = value;
      }
   }

  /**
   *  Message instantiation (form 4).
   *
   *     @param type message type
   *     @param id barber identification
   *     @param endOP end of operations flag
   */

   public Message (int type, int id, boolean endOp)
   {
      msgType = type;
      thiefId= id;
      this.endOp = endOp;

      if(type == MessageType.ISNEEDED){
         this.isNeeded = endOp;
         this.thiefId = id;
         this.thiefState = OrdinaryThiefStates.WAITING_UNTIL_NEEDED;
      }

   }

  /**
   *  Message instantiation (form 5).
   *
   *     @param type message type
   *     @param thiefId barber identification
   *     @param thiefStateOrRoomId barber state (or )
   *     @param assaultPartyId customer identification
   */

   public Message (int type, int thiefId, int thiefStateOrRoomId, int assaultPartyId)
   {
      msgType = type;
      this.thiefId= thiefId;
      this.thiefState = thiefStateOrRoomId;
      
      if(type == MessageType.REVERSE){
         this.thiefAssaultPartyId = assaultPartyId;
         this.thiefState = thiefStateOrRoomId;
         this.thiefId = thiefId;
      } else if (type == MessageType.HASCRAWLEDIN || type == MessageType.HASCRAWLEDOUT){
         this.thiefId = thiefId;
         this.thiefRoomId = thiefStateOrRoomId;
         this.thiefState = assaultPartyId;
      } else if (type == MessageType.READYTOGO){
         this.msgType = type;
         this.thiefId = thiefId;
         this.thiefAssaultPartyId = thiefStateOrRoomId;
         this.thiefRoomId = assaultPartyId;
      } else if (type == MessageType.SETOCAN){
         this.thiefCanvasAlt = thiefStateOrRoomId;
         this.thiefRoomId = assaultPartyId;
         this.thiefState = OrdinaryThiefStates.AT_A_ROOM;
      }
   }

  /**
   *  Message instantiation (form 6).
   *
   *     @param type message type
   *     @param id master identification or thief identification
   *     @param masterState master state
   *     @param thiefId thief identification
   *     @param thiefState thief state
   */

   public Message (int type, int id, int masterState, int thiefId, int thiefState)
   {
      msgType = type;
      this.masterState = masterState;
      this.thiefId= thiefId;
      this.thiefState = thiefState;
      if(type == MessageType.CRAWLIN || type == MessageType.CRAWLOUT){
         this.thiefId = id;
         this.thiefRoomId = masterState;
         this.thiefAssaultPartyId = thiefId;
         this.thiefAgility = thiefState;
      }

   }

  /**
   *  Message instantiation (form 7).
   *
   *     @param type message type
   *     @param name name of the logging file
   *     @param nIter number of iterations of the customer life cycle
   */

   public Message (int type, String name, int nIter)
   {
      msgType = type;
      fName= name;
      this.nIter = nIter;
   }

   /**
   *  Message instantiation (form 8).
   *
   *     @param type message type
   *     @param barbId barber identification
   *     @param barbState barber state
   *     @param custId customer identification
   *     @param custState customer state
   */

   public Message (int type, int thiefId, int assaultPartyId, int roomId, boolean hasCanvas)
   {
      this.msgType = type;
      if(type == MessageType.HASACANVAS || type == MessageType.HANDACANVAS ){
         this.thiefId = thiefId;
         this.thiefAssaultPartyId = assaultPartyId;
         this.thiefRoomId = roomId;
         this.thiefCanvas = hasCanvas;
         this.thiefState = OrdinaryThiefStates.WAITING_TO_HAND_A_CANVAS;
      } else if (type == MessageType.COLLECTEDCANVAS){
         this.thiefId = thiefId;
         this.thiefAssaultPartyId = assaultPartyId;
         this.thiefRoomId = roomId;
         this.thiefCanvas = hasCanvas;
         this.thiefState = OrdinaryThiefStates.WAITING_UNTIL_NEEDED;
      }
   }

   /**
   *  Message instantiation (form 9).
   *
   *     @param type message type
   *     @param thiefId thief identification
   *     @param thiefState thief state
   *     @param thiefSit thief situation
   *     @param thiefAssaultId thief assault party id
   *     @param thiefRoomId thief room id
   *     @param thiefPos this position
   *     @param thiefCanvas this has canvas
   */

   public Message (int type, int thiefId, int thiefState, char thiefSit, int thiefAssaultId, int thiefRoomId, int thiefPos, int thiefCanvas)
   {
      msgType = type;
      this.thiefId= thiefId;
      this.thiefState = thiefState;
      this.thiefSituation = thiefSit;
      this.thiefAssaultPartyId = thiefAssaultId;
      this.thiefRoomId = thiefRoomId;
      this.thiefPos = thiefPos;
      this.thiefCanvasAlt = thiefCanvas;
   }

   /**
    * Message instantiation (form 10).
    *    @param type message type
    *    @param operation master operation
    */

   public Message (int type, char oper){
      msgType = type;
      this.masterOperation = oper;
      if(type == MessageType.APPRAISEDONE){
         this.masterOperation = oper;
         this.masterState = MasterThiefStates.DECIDING_WHAT_TO_DO;
      }
   }

   /**
    * Message instantiation (form 11).
    *    @param type message type
    *    @param array agilities / canvas / distances
    */

   public Message (int type, int[] array){
      msgType = type;
      if(type == MessageType.SETITHIEFAGIL){
         this.thievesAgility = array;
      } else if(type == MessageType.SETINUMCANVAS){
         this.canvasInRoom = array;
      } else if(type == MessageType.SETIROOMDIST){
         this.distanceToRoom = array;
      }
   }

  /**
   *  Getting message type.
   *
   *     @return message type
   */

   public int getMsgType ()
   {
      return (msgType);
   }

   /**
    * Getting thieves agility
    */

   public int[] getThievesAgility(){
      return this.thievesAgility;
   }

   /**
    * Getting canvas in room
    */

   public int[] getCanvasInRoom(){
      return this.canvasInRoom;
   }

   /**
    * Getting distance to room
    */

   public int[] getDistanceToRoom(){
      return this.distanceToRoom;
   }

   /**
    * Getting ordinary hasCanvas
    *
    * @return ordinary hasCanvas
    */

   public boolean getThiefHasCanvas() {
      return thiefCanvas;
   }

   /**
    * Getting ordinary isNeeded
    * @return
    */

   public boolean getThiefIsNeeded() {
      return this.isNeeded;
   }

   /**
    * Getting ordinary id
    *
    * @return ordinary id
    */

   public int getThiefId() {
      return thiefId;
   }

   /**
    * Getting ordinary assaultParty id
    * @return assaultParty id
    */

   public int getThiefAssaultPartyId() {
      return thiefAssaultPartyId;
   }

   /**
    * Getting ordinary room id
    * @return room id
    */

   public int getThiefRoomId() {
      return thiefRoomId;
   }

   /**
    * Getting ordinary position
    * @return position
    */

   public int getThiefPos() {
      return thiefPos;
   }

   /**
    * Getting ordinary canvas alternative
    * @return canvas
    */

   public int getThiefCanvasAlt() {
      return thiefCanvasAlt;
   }

   /**
    * Getting ordinary situation
    * @return situation
    */

   public char getThiefSituation() {
      return thiefSituation;
   }

   /**
    * Getting Master Assault Party id
    * @return master assault party id
    */

   public int getMasterAssaultPartyId(){
      return masterAssaultPartyId;
   }

   /**
    * Getting Master Room id
    * @return master room id
    */

   public int getMasterRoomId(){
      return masterRoomId;
   }


   /**
    * Getting operation for master to perform
    * @return operation 
    */

   public char getOperation() {
      return masterOperation;
   }

   /**
    * Get the number of stolen canvas
    * @return number of stolen canvas
    */

   public int getNumStolenPaintings() {
      return numStolenPaintings;
   }

  /**
   *  Getting ordinary state.
   *
   *     @return ordinary state
   */

   public int getThiefState ()
   {
      return thiefState;
   }

   /**
    * Getting thief agility
    * @return thief agility
    */

   public int getThiefAgility() {
      return thiefAgility;
   }


  /**
   *  Getting Master state.
   *
   *     @return Master state
   */

   public int getMasterState ()
   {
      return masterState;
   }

  /**
   *  Getting end of operations flag (barber).
   *
   *     @return end of operations flag
   */

   public boolean getEndOp ()
   {
      return (endOp);
   }

  /**
   *  Getting name of logging file.
   *
   *     @return name of the logging file
   */

   public String getLogFName ()
   {
      return (fName);
   }

  /**
   *  Getting the number of iterations of the customer life cycle.
   *
   *     @return number of iterations of the customer life cycle
   */

   public int getNIter ()
   {
      return (nIter);
   }

  /**
   *  Printing the values of the internal fields.
   *
   *  It is used for debugging purposes.
   *
   *     @return string containing, in separate lines, the pair field name - field value
   */

   @Override
   public String toString ()
   {

      String distances = "Distances: ";
      for(int i = 0; i < distanceToRoom.length; i++){
         distances += distanceToRoom[i] + " ";
      }

      String canvas = "Canvas: ";
      for(int i = 0; i < canvasInRoom.length; i++){
         canvas += canvasInRoom[i] + " ";
      }

      String agilities = "Agilities: ";
      for(int i = 0; i < thievesAgility.length; i++){
         agilities += thievesAgility[i] + " ";
      }

      return ("Message type = " + msgType +
              "\nMaster State = " + masterState +
              "\nThief Id = " + thiefId +
              "\nThief State = " + thiefState +
              "\nThief RoomId = " + thiefRoomId +
              "\nThief AssaultPartyId = " + thiefAssaultPartyId +
              "\nEnd of Operations (master) = " + endOp +
              "\nName of logging file = " + fName +
              "\nNumber of iterations = " + nIter) +
               "\n" + distances +
               "\n" + canvas +
               "\n" + agilities;

   }
}
