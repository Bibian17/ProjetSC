import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;

public class TestEtape3 extends Frame {
	public TextArea text;
	public TextField data1;
	public TextField data2;
	private static SharedObject entier;
	private static SharedObject sentence;

	public static void main(String argv[]) {
		
		// initialize the system
		Client.init();
		
		sentence = Client.lookup("Sentence");
		if (sentence == null) {
			sentence = Client.create(new Sentence());
			Client.register("Sentence", sentence);
		}
		entier = Client.lookup("Entier");
		if (entier == null) {
			entier = Client.create(new Entier());
			Client.register("Entier", entier);
		}
        // create the graphical part
        new TestEtape3();
	}

	public TestEtape3() {
	
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data=new TextField(20);
		add(data);
	
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListenerTest(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerTest(this));
		add(read_button);
		
		setSize(500,300);
		text.setBackground(Color.black); 
		show();
	}
	
}

class readListenerTest implements ActionListener {

	TestEtape3 te3;
	public readListenerTest (TestEtape3 t) {
		te3 = t;
	}
	
	public void actionPerformed (ActionEvent e) {
	
        try{
			SharedObject so1 = TestEtape3.entier;
			SharedObject so2 = TestEtape3.sentence;
			so1.lock_read();
			int i = ((Entier) (so1.obj)).read();
			so1.unlock();
			te3.text.append("Entier : " + i + " ; ");
			so2.lock_read();
			String s = ((Sentence)(so2.obj)).read();
			so2.unlock();
			te3.text.append("Sentence : " + s + "\n");
			te3.data.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
}

class writeListenerTest implements ActionListener {

	TestEtape3 te3;
	public writeListenerTest (TestEtape3 t) {
		te3 = t;
	}
	
	public void actionPerformed (ActionEvent e) {
        
        try{
			int nb = Integer.parseInt(te3.data.getText());
			SharedObject so = TestEtape3.entier;
			so.lock_write();
			((Entier) (so.obj)).write(nb);
			so.unlock();
			te3.data1.setText("");
        } catch (NumberFormatException e) {
			String s = te3.data.getText();
			SharedObject so = TestEtape3.sentence;
			so.lock_write();
			((Sentence) (so.obj)).write(s);
			so.unlock();
			te3.data1.setText("");
        }
    }
		
}