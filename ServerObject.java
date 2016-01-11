import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServerObject implements ServerObject_itf {
	
	private Object objet;
	private int id;
	
	// NL : no lock (0)
	// RL : read lock (1)
	// WLC : write lock (2)
	private int lock;
	
	private ArrayList<Client_itf> lecteurs;
	private Client_itf ecrivain;
	
	public ServerObject(Object o, int id) {
		this.objet = o;
		this.id = id;
		this.lecteurs = new ArrayList<Client_itf>();
		this.ecrivain = null;
		this.lock = 0;
	}
	
	public Object lock_read(Client_itf client) {
		try {
			if (this.lock==2) {
				this.objet = this.ecrivain.reduce_lock(this.id);
				this.lecteurs.add(ecrivain);
				ecrivain=null;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.lock = 1;
		this.lecteurs.add(client);
		return this.objet;
	}

	public Object lock_write(Client_itf client) {
		switch (this.lock) {
		case 0 :
			
		break;
		case 1 : 
			for (Client_itf c : this.lecteurs) {
				try {
					c.invalidate_reader(this.id);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		break;
		case 2 : 
			try {
				this.objet = ecrivain.invalidate_writer(this.id);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		break;
		}
		this.lock = 2;
		this.ecrivain = client;;
		this.lecteurs.clear();
		return this.objet;
	}
	
}
