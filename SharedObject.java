import java.io.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SharedObject implements Serializable, SharedObject_itf {

	private int id;

	public Object objet;

	// NL : no local lock (0)
	// RLC : read lock cached (not taken) (1)
	// WLC : write lock cached (2)
	// RLT : read lock taken (3)
	// WLT : write lock taken (4)
	// RLT_WLC : read lock taken and write lock cached (5)
	private int lock;
	
	private Client client;

	public SharedObject (Object o, int id) {
		this.objet = o;
		this.id = id;
		this.lock = 0;
	}

	public int getID() {
		return this.id;
	}

	public int getLockType() {
		return this.lock;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		switch(this.lock) {
		case 0 :
			this.lock = 3;
			this.objet = Client.lock_read(this.id);
		break;
		case 1 :
			this.lock = 3;
		break;
		case 2 :
			this.lock = 5;
		break;
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		switch(this.lock) {
		case 0 :
			this.lock = 4;
			this.objet = Client.lock_write(this.id);
		break;
		case 1 :
			this.lock = 4;
			this.objet = Client.lock_write(this.id);
		break;
		case 2 :
			this.lock = 4;
		break;
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch(this.lock) {
		case 3 :
			this.lock = 1;
		break;
		case 4 :
			this.lock = 2;
		break;
		case 5 :
			this.lock = 2;
		break;
		}
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		switch(this.lock) {
		case 2 :
			this.lock = 1;
		break;
		case 4 :
			this.lock = 1;
		break;
		case 5 :
			this.lock = 3;
		break;
		}
		return this.objet;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		switch(this.lock) {
		case 1 :
			this.lock = 0;
		break;
		case 3 :
			this.lock = 0;
		break;
		case 5 :
			this.lock = 0;
		break;
		}
	}

	public synchronized Object invalidate_writer() {
		switch(this.lock) {
		case 2 :
			this.lock = 0;
		break;
		case 4 :
			this.lock = 0;
		break;
		case 5 :
			this.lock = 0;
		break;
		}
		return this.objet;
	}
}
