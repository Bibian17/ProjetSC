import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {

	private int id;

	public Object obj;

	// NL : no local lock (0)
	// RLC : read lock cached (not taken) (1)
	// WLC : write lock cached (2)
	// RLT : read lock taken (3)
	// WLT : write lock taken (4)
	// RLT_WLC : read lock taken and write lock cached (5)
	private int lock;

	public SharedObject (Object o, int id) {
		this.obj=o;
		this.id=id;
		this.lock=0;
	}

	public int getID() {
		return this.id;
	}

	public int getLockType() {
		return this.lock;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		if (this.id==4) {
			this.id=5;
		} else {
			this.id=3;
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		this.id=4;
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
	}

	public synchronized Object invalidate_writer() {
	}
}
