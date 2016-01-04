import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {
	
	private static HashMap<Integer,SharedObject_itf> correspondances;
	
	private static Server_itf serveur;

	// l'id affecté à un SharedObject
	private static int idso = 0;

	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		correspondances = new HashMap<String,SharedObject_itf>();
		serveur = new Server(); // à compléter
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		int id = serveur.lookup(name);
		return correspondances.get(id);
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		serveur.register(name,so.getID);
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		SharedObject so = new SharedObject(o,idso);
		correspondances.put(idso,so)
		return so;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
	}
}
