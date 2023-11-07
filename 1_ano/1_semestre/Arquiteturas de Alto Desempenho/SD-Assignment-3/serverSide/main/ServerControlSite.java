package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;
/**
 *    Server side of the ControlSite.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ServerControlSite{

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
	 */

     public static void main(String[] args) {
        int portNumb = -1;                          // port number for listening to service requests
		String rmiRegHostName;                      // name of the platform where is located the RMI registering service
		int rmiRegPortNumb = -1;                    // port number where the registering service is listening to service requests

        /*Name of the objects */
        String nameEntryGeneralRepo = "GeneralRepository";
        String nameEntryControlSite = "ControlSite";

        if (args.length != 3){ 
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

        /* get a remote reference to the controlSite object */
        ControlSite controlSite = new ControlSite(repoStub);
        ControlSiteInterface controlSiteStub = null;                        // remote reference to the controlSite object

        try
        { controlSiteStub = (ControlSiteInterface) UnicastRemoteObject.exportObject (controlSite, portNumb);
        }
        catch (RemoteException e)
		{ GenericIO.writelnString ("ControlSite stub generation exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		GenericIO.writelnString ("ControlSite stub was generated!");

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
        { reg.bind (nameEntryControlSite, controlSiteStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ControlSite registration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("ControlSite already bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        GenericIO.writelnString ("ControlSite object was registered!");

        /* Wait for the service to end */

        GenericIO.writelnString ("ControlSite is in operation!");

        try
        { while (!end)
            synchronized (Class.forName ("serverSide.main.ServerControlSite"))
            { try
                { (Class.forName ("serverSide.main.ServerControlSite")).wait ();
                }
                catch (InterruptedException e)
                { GenericIO.writelnString ("ControlSite main thread was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerControlSite was not found (blocking)!");
        e.printStackTrace ();
        System.exit (1);
        }

        /* server shutdown */

        boolean shutdownDone = false;                                  // flag signalling the shutdown of the general repository service

        try
        { reg.unbind (nameEntryControlSite);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ControlSite deregistration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("ControlSite not bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        GenericIO.writelnString ("ControlSite was deregistered!");

        try
        { shutdownDone = UnicastRemoteObject.unexportObject (controlSite, true);
        }
        catch (NoSuchObjectException e)
        { GenericIO.writelnString ("ControlSite unexport exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        if (shutdownDone)
            GenericIO.writelnString ("ControlSite was shutdown!");

    }

    /**
     *  Close of operations.
     */

    public static void shutdown ()
    {
        end = true;
        try
        { synchronized (Class.forName ("serverSide.main.ServerControlSite"))
            { (Class.forName ("serverSide.main.ServerControlSite")).notify ();
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerControlSite was not found (waking up)!");
        e.printStackTrace ();
        System.exit (1);
        }
    }

}