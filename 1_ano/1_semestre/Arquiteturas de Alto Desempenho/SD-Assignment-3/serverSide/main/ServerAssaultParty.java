package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;
/**
 *    Server side of the ConcentrationSite.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ServerAssaultParty{

    /**
     *  Flag signaling the service is active.
     */

    public static boolean end = false;

    /**
	 *  Main method.
	 *
	 *        args[0] - port number for listening to service requests
	 *        args[1] - name of the platform where is located the RMI registering service
	 *        args[2] - port number where the registering service is listening to service requests
     *        args[3] - id of the assault party
	 */

     public static void main(String[] args) {
        int portNumb = -1;                          // port number for listening to service requests
		String rmiRegHostName;                      // name of the platform where is located the RMI registering service
		int rmiRegPortNumb = -1;                    // port number where the registering service is listening to service requests
        int assaultPartyId = -1;                    // id of the assault party

        /*Name of the objects */
        String nameEntryGeneralRepo = "GeneralRepository";

        if (args.length != 4){ 
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }

        try { 
            portNumb = Integer.parseInt (args[0]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) { 
            GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
        }

        rmiRegHostName = args[1];
        try { 
            rmiRegPortNumb = Integer.parseInt (args[2]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) { 
            GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        try {
            assaultPartyId = Integer.parseInt (args[3]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[3] is not a number!");
            System.exit (1);
        }
        if(assaultPartyId != 0 && assaultPartyId != 1){
            GenericIO.writelnString ("args[3] is not a valid assault party id!");
            System.exit (1);
        }

        String nameEntryAssaultParty = "AssaultParty" + assaultPartyId;

        /* create and install the security manager */

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        GenericIO.writelnString ("Security manager was installed!");

        /* Locate RMI registry service */

        Registry registry = null; // remote reference for registration in the RMI registry service

		try
		{ registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
		}
		catch (RemoteException e)
		{ GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		GenericIO.writelnString ("RMI registry was created!");


        /* get a remote reference to the general repository object */
        GeneralRepoInterface repoStub = null;                        // remote reference to the general repository object

        try
		{ repoStub = (GeneralRepoInterface) registry.lookup (nameEntryGeneralRepo);
		}
		catch (RemoteException e)
		{ GenericIO.writelnString ("General Repository lookup exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		catch (NotBoundException e)
		{ GenericIO.writelnString ("General Repository not bound exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}

        /* get a remote reference to the AssaultParty0 object */
        AssaultParty assaultParty = new AssaultParty(assaultPartyId, SimulPar.S,repoStub);
        AssaultPartyInterface assaultPartyStub = null;                        // remote reference to the AssaultParty0 object

        try
        { assaultPartyStub = (AssaultPartyInterface) UnicastRemoteObject.exportObject (assaultParty, portNumb);
        }
        catch (RemoteException e)
		{ GenericIO.writelnString ("AssaultParty stub generation exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		GenericIO.writelnString ("AssaultParty stub was generated!");


        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";                      // public name of the object that enables the registration of other remote objects
        Register reg = null;                                           // remote reference to the object that enables the registration of other remote objects

        try
        { reg = (Register) registry.lookup (nameEntryBase);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject lookup exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("RegisterRemoteObject not bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        try
        { reg.bind (nameEntryAssaultParty, assaultPartyStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("assaultPartyStub registration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("assaultPartyStub already bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        GenericIO.writelnString ("assaultPartyStub object was registered!");

        GenericIO.writelnString ("Assault Party is in operation!");

        try
        { while (!end)
            synchronized (Class.forName ("serverSide.main.ServerAssaultParty"))
            { try
                { (Class.forName ("serverSide.main.ServerAssaultParty")).wait ();
                }
                catch (InterruptedException e)
                { GenericIO.writelnString ("Assault Party main thread was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerAssaultParty was not found (blocking)!");
        e.printStackTrace ();
        System.exit (1);
        }

        /* server shutdown */

        boolean shutdownDone = false;                                  // flag signalling the shutdown of the general repository service

        try
        { reg.unbind (nameEntryAssaultParty);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ConcentrationSite deregistration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("ConcentrationSite not bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        GenericIO.writelnString ("ConcentrationSite was deregistered!");

        try
        { shutdownDone = UnicastRemoteObject.unexportObject (assaultParty, true);
        }
        catch (NoSuchObjectException e)
        { GenericIO.writelnString ("ConcentrationSite unexport exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        if (shutdownDone)
            GenericIO.writelnString ("ConcentrationSite was shutdown!");

    }

    /**
     *  Close of operations.
     */

    public static void shutdown ()
    {
        end = true;
        try
        { synchronized (Class.forName ("serverSide.main.ServerAssaultParty"))
            { (Class.forName ("serverSide.main.ServerAssaultParty")).notify ();
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerAssaultParty was not found (waking up)!");
        e.printStackTrace ();
        System.exit (1);
        }
    }

}