import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements Server_itf{
	
	private HashMap<String,Integer> correspondancesNomID;
	private HashMap<Integer,ServerObject> correspondancesIDSO;
	
	private static int idauto = 0;
	
	public Server() throws RemoteException {
		super();
		this.correspondancesNomID = new HashMap<String,Integer>();
	}

	public int lookup(String name) throws java.rmi.RemoteException {
		return correspondancesNomID.get(name);
	}

	public void register(String name, int id) throws java.rmi.RemoteException {
		this.correspondancesNomID.put(name,id);
	}

	public int create(Object o) throws java.rmi.RemoteException {
		idauto++;
		ServerObject so = new ServerObject(o,idauto);
		this.correspondancesIDSO.put(idauto,so);
		return idauto;
	}

	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException {
		return this.correspondancesIDSO.get(id).lock_read(client);
	}

	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException {
		return this.correspondancesIDSO.get(id).lock_write(client);
	}
	
	public static void main(String args[]){	
		Server server;
			try {
			server = new Server();
			int port = 2099;
			Registry registry = LocateRegistry.createRegistry(port);
			String url ="//"+"localhost"+":"+String.valueOf(port)+"/Server";
			Naming.bind(url,server);
			System.out.println("Server is now running ...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
