import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;

public class TestEtape1 extends Frame {
	public TextArea text;
	public TextField data1;
	public TextField data2;
	private static HashMap<Integer,SharedObject> entiers;
	public final static int nbEntiers = 5;

	public static void main(String argv[]) {
		
		// initialize the system
		Client.init();
		
		entiers = new HashMap<Integer,SharedObject>();
		
		// create 5 integers
		for (int i=1;i<=nbEntiers;i++) {
			SharedObject s = Client.lookup("Entier" + i);
			if (s == null) {
				s = Client.create(new Entier(i));
				Client.register("Entier" +  i, s);
			}
			entiers.put((Integer) i,s);
		}
        // create the graphical part
        new TestEtape1();
	}

	public TestEtape1() {
	
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data1=new TextField(20);
		add(data1);
		data2=new TextField(20);
		add(data2);
	
		Button compute_button = new Button("compute");
		compute_button.addActionListener(new computeListener(this));
		add(compute_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerTest(this));
		add(read_button);
		
		setSize(500,400);
		text.setBackground(Color.black); 
		show();
	}
	
	public static HashMap<Integer,SharedObject> getEntiers() {
		return entiers;
	}
}

class readListenerTest implements ActionListener {

	TestEtape1 te1;
	public readListenerTest (TestEtape1 t) {
		te1 = t;
	}
	
	public void actionPerformed (ActionEvent e) {
	
		try{
			int nb = Integer.parseInt(te1.data1.getText());
		
			if (nb>=1 && nb<=TestEtape1.nbEntiers) {
				SharedObject so = TestEtape1.getEntiers().get(nb);
				so.lock_read();
				int i = ((Entier) (so.obj)).read();
				so.unlock();
				te1.text.append(i + "\n");
				te1.data1.setText("");
			}
		} catch(Exception ex) {
      	System.out.println("Il faut un entier !!!");
      }
		
	}
	
}

class computeListener implements ActionListener {

	TestEtape1 te1;
	public computeListener (TestEtape1 t) {
		te1 = t;
	}
	
	public void actionPerformed (ActionEvent e) {
        
      try{
			int nb1 = Integer.parseInt(te1.data1.getText());
			int nb2 = Integer.parseInt(te1.data2.getText());

		
			if (nb1>=1 && nb1<=TestEtape1.nbEntiers && nb2>=1 && nb2<=TestEtape1.nbEntiers && nb1!=nb2) {
				SharedObject so1 = TestEtape1.getEntiers().get(nb1);
				SharedObject so2 = TestEtape1.getEntiers().get(nb2);
				so1.lock_write();
				so2.lock_write();
				int i1 = ((Entier) (so1.obj)).read();
				int i2 = ((Entier) (so2.obj)).read();
				((Entier) (so1.obj)).write(i1+i2);
				((Entier) (so2.obj)).write(i1-i2);
				so1.unlock();
				so2.unlock();
				te1.data1.setText("");
				te1.data2.setText("");
			}
   	} catch(Exception ex) {
      	System.out.println("Il faut un entier !!!");
    	}
	}
		
}
