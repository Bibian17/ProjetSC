import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements Server_itf {
	
	private HashMap<String,Integer> correspondancesNomID;
	private HashMap<Integer,ServerObject> correspondancesIDSO;
	
	private static int idauto = 0;
	
	public Server() throws RemoteException {
		super();
		this.correspondancesNomID = new HashMap<String,Integer>();
		this.correspondancesIDSO = new HashMap<Integer,ServerObject>();
	}

	public int lookup(String name) throws java.rmi.RemoteException {
		System.out.println("lookup");
		int res;
		Integer tmp = correspondancesNomID.get(name);
		if (tmp == null) {
			res = -1;
		} else {
			res = tmp;
		}
		return res;
	}

	public void register(String name, int id) throws java.rmi.RemoteException {
		System.out.println("register");
		this.correspondancesNomID.put(name,id);
	}

	public int create(Object o) throws java.rmi.RemoteException {
		System.out.println("create");
		idauto++;
		ServerObject so = new ServerObject(o,idauto);
		this.correspondancesIDSO.put(idauto,so);
		return idauto;
	}

	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException {
		System.out.println("lock_read");
		return this.correspondancesIDSO.get(id).lock_read(client);
	}

	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException {
		System.out.println("lock_write");
		return this.correspondancesIDSO.get(id).lock_write(client);
	}
	
	public static void main(String args[]){	
			try {
			LocateRegistry.createRegistry(5000);
			Naming.bind("//localhost:5000/Server",new Server());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
