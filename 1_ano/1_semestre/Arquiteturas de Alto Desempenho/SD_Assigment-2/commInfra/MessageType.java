package commInfra;

/**
 *   Type of the exchanged messages.
 *the
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType
{

  // ----------------------------------- CONCENTRATION SITE --------------------------------------

  /**
   *  Ordinary thief goes to am I needed (service request).
   */

   public static final int AMINEEDED = 1;

  /**
   *  Ordinary thief is needed (reply).
   */

   public static final int ISNEEDED = 2;

  /**
   *  Ordinary thief prepares excursion (service request).
   */

   public static final int PREPAREEXC = 3;

  /**
   *  Ordinary thief is ready to go (reply).
   */

   public static final int READYTOGO = 4;

  /**
   * Master prepares assault party (service request).
   */

   public static final int PREPAREASSAULT = 5;

   /**
    * Master is ready to send assault party (reply).
    */

    public static final int READYTOSEND = 6;

   /**
   * Master sum up results (service request).
   */

   public static final int SUMUPRESULTS = 7;

   /**
    * Shut down the server concentration site (service request).
    */

    public static final int CONSHUTDOWN = 8;

    /**
     * Shut down the server concentration site (reply).
     */

      public static final int CONSHUTDOWNREP = 9;

   
  
  // ----------------------------------- CONTROL SITE --------------------------------------

  /**
   *  Ordinary thief hands a canvas (service request).
   */

    public static final int HANDACANVAS = 101;

  /**
   *  Master start of operations (service request).
   */

    public static final int STARTOP = 102;

  /**
   * Master appraises situation (service request).
   */

    public static final int APPRAISESIT = 103;

  /**
    * Appraisal was done (reply).
    */
 
    public static final int APPRAISEDONE = 104;

  /**
   * Master requests assault party id (service request).
   */

    public static final int GETASSAULTID = 105;

   /**
    * Master receives party id was sent (reply).
    */
 
    public static final int ASSAULTID = 106;

  /**
   * Master requests room id (service request).
   */

    public static final int GETROOMID = 107;

  /**
   * Master receives room id was sent (reply).
   */

    public static final int ROOMID = 108;

  /**
   * Master takes a rest (service request).
   */

   public static final int TAKEAREST = 109;

  /**
   * Master has taken a rest (reply).
   */

    public static final int HASRESTED = 110;

  /**
   * Master collects a canvas (service request).
   */

    public static final int COLLECTCANVAS = 111;

  /**
   * Master has collected a canvas (reply).
   */

    public static final int HASCOLLECTED = 112;

  /**
   * Master requests the total canvas stolen (service request).
   */

    public static final int GETTOTALCANVAS = 113;
   

  /**
   * Master receives the total canvas stolen (reply).
   */

    public static final int TOTALCANVAS = 114;

  /**
   * Master ends operations (service request).
   */

    public static final int ENDOP = 115;

  /**
   * Master has collected a canvas (reply).
   */

    public static final int COLLECTEDCANVAS = 116;

  /**
   * Shut down the server control site (service request).
   */

    public static final int CONTSHUTDOWN = 117;

  /**
   * Shut down the server control site (reply).
   */

    public static final int CONTSHUTDOWNREP = 118;

  
  // ----------------------------------- MUSEUM --------------------------------------

  /**
   *  Ordinary thief rolls a canvas (service request).
   */

    public static final int ROLLACANVAS = 201;

  /**
   *  Ordinary thief has a canvas (reply).
   */

    public static final int HASACANVAS = 202;

  /**
   * Shut down the server museum (service request).
   */

    public static final int MUSEUMSHUTDOWN = 203;

  /**
   * Shut down the server museum (reply).
   */

    public static final int MUSEUMSHUTDOWNREP = 204;



  // ----------------------------------- ASSAULT PARTY --------------------------------------

  /**
   * Master sends assault party (service request).
   */

    public static final int SENDASSAULT = 301;

  /**
   * Master receives assault party was sent (reply).
   */

    public static final int ASSAULTSENT = 302;


  /**
   *  Ordinary thief crawls in (service request).
   */

   public static final int CRAWLIN = 303;

  /**
   * Ordinary thief has crawled in (reply).
   */

    public static final int HASCRAWLEDIN = 304;


  /**
   *  Ordinary thief reverses direction (service request).
   */

   public static final int REVERSE = 305;

  /**
   * Ordinary thief has reversed direction (reply).
   */

    public static final int HASREVERSED = 306;

  /**
   *  Ordinary thief crawls out (service request).
   */

   public static final int CRAWLOUT = 307;

  /**
   * Ordinary thief has crawled out (reply).
   */

    public static final int HASCRAWLEDOUT = 308;

  /**
   * Shut down the server assault party (service request).
   */

    public static final int APSHUTDOWN = 309;

  /**
   * Shut down the server assault party (reply).
   */

    public static final int APSHUTDOWNREP = 310;


  // ----------------------------------- GENERAL REP --------------------------------------

  /**
   *  Initialization of the logging file name and the number of iterations (service request).
   */

   public static final int SETNFIC = 401;

  /**
   *  Logging file was initialized (reply).
   */

   public static final int NFICDONE = 402;

  /**
   *  Server shutdown (service request).
   */

   public static final int SHUT = 403;

  /**
   *  Server was shutdown (reply).
   */

   public static final int SHUTDONE = 404;

  /**
   *  Set master state (service request).
   */

   public static final int SETMS = 405;

  /**
   * Set master state (reply)
   */

    public static final int SETMSREP = 406;

  /**
   *  Set ordinary state (service request).
   */

   public static final int SETOS = 407;

  /**
   * Set ordinary state (reply)
   */

    public static final int SETOSREP = 408;

  /**
   * Set Ordinary Situation (service request).
   */

    public static final int SETOSIT = 409;

  /**
   * Set Ordinary Situation (reply)
   */

    public static final int SETOSITREP = 410;

  /**
   * Set Ordinary Position (service request)
   */

    public static final int SETOPOS = 411;

  /**
   * Set Ordinary Position (reply)
   */

    public static final int SETOPOSREP = 412;

  /**
   * Set Ordinary Canvas (service request)
   */

    public static final int SETOCAN = 413;
  
  /**
   * Set Ordinary Canvas (reply)
   */

    public static final int SETOCANREP = 414;

  /**
   * Set Ordinary All (service request)
   */

    public static final int SETOALL = 415;

  /**
   * Set Ordinary All (reply)
   */

    public static final int SETOALLREP = 416;

  /**
   * Reset Ordinary All (service request)
   */

    public static final int RESOALL = 417;

  /**
   * Reset Ordinary All (reply)
   */

    public static final int RESOALLREP = 418;

  /**
   * Set Assault Party Room (service request)
   */

    public static final int SETAPROOM = 419;
   
  /**
   * Set Assault Party Room (reply)
   */

    public static final int SETAPROOMREP = 420;
  
  /**
   * Add to Assault Party (service request)
   */

    public static final int ADDTOAP = 421;

  /**
   * Add to Assault Party (reply)
   */

    public static final int ADDTOAPREP = 422;


  /**
   *  Setting acknowledged (reply).
   */

   public static final int ACK = 423;

  /**
   * Setting initial num canvas
   */

    public static final int SETINUMCANVAS = 424;

  /**
   * Setting initial num canvas (reply)
   */

    public static final int SETINUMCANVASREP = 425;

  /**
   * Set initial room distance
   */

    public static final int SETIROOMDIST = 425;

  /**
   * Set initial room distance (reply)
   */

    public static final int SETIROOMDISTREP = 426;

  /**
   * Set initial thief agility
   */

    public static final int SETITHIEFAGIL = 427;

  /**
   * Set initial thief agility (reply)
   */

    public static final int SETITHIEFAGILREP = 428;

  /**
   * Shut down the server general repository (service request).
   */

    public static final int REPSHUTDOWN = 429;

  /**
   * Shut down the server general repository (reply).
   */

    public static final int REPSHUTDOWNREP = 430;

    
}
