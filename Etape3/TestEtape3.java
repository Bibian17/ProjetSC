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
	public TextField data;
	private static Entier_itf entier;
	private static Sentence_itf sentence;

	public static void main(String argv[]) {
		
		// initialize the system
		Client.init();
		
		sentence = (Sentence_itf) Client.lookup("Sentence");
		if (sentence == null) {
			sentence = (Sentence_itf) Client.create(new Sentence());
			Client.register("Sentence", sentence);
		}
		entier = (Entier_itf) Client.lookup("Entier");
		if (entier == null) {
			entier = (Entier_itf) Client.create(new Entier(0));
			Client.register("Entier", entier);
		}
        // create the graphical part
        new TestEtape3();
	}

	public static Entier_itf getEntier() {
		return entier;
	}

	public static Sentence_itf getSentence() {
		return sentence;
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
			Entier_itf so1 = TestEtape3.getEntier();
			Sentence_itf so2 = TestEtape3.getSentence();
			so1.lock_read();
			int i = so1.read();
			so1.unlock();
			te3.text.append("Entier : " + i + " ; ");
			so2.lock_read();
			String s = so2.read();
			so2.unlock();
			te3.text.append("Sentence : " + s + "\n");
			te3.data.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
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
			Entier_itf so = TestEtape3.getEntier();
			so.lock_write();
			so.write(nb);
			so.unlock();
			te3.data.setText("");
      } catch (NumberFormatException ex) {
			String s = te3.data.getText();
			Sentence_itf so = TestEtape3.getSentence();
			so.lock_write();
			so.write(s);
			so.unlock();
			te3.data.setText("");
      }
   }
		
}
