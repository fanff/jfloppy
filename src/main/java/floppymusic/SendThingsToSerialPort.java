package floppymusic;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import panel.agentGossip.FeedPanel;

import fr.irit.smac.may.serial.SerialPortComp;
import fr.irit.smac.may.serial.impl.serial.SerialPortImpl;

public class SendThingsToSerialPort {
	public static String portName = "/dev/ttyACM0";
	
	public static InputStream instream;
	
	
	static FeedPanel fp = new FeedPanel("arduino");
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.add(fp);
		f.setVisible(true);
		
		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		SerialPortComp.Component serialComp = new SerialPortComp.Component(
				new SerialPortImpl(), new SerialPortComp.Bridge() {}
				);
		
		serialComp.start();
		serialComp.statusPort().connect(portName, 115200, 0, 0, 0);
		
		
		instream = serialComp.statusPort().getInputStream();
		try {
			waitForChar('#');
			
			System.out.println("Received start");
			
			serialComp.sendingPort().sendOneByte(20);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("SendingFreqs");
			
			for(int halfperiod = 100 ; halfperiod <= 255; halfperiod ++){
				serialComp.sendingPort().sendOneByte(halfperiod);
				//try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				
				waitForChar('s');

				System.out.println("played : "+halfperiod);
			
			}
			
		} catch (IOException e) {
			System.err.println("ERROR DURING PROGRAM");
			e.printStackTrace();
		}
		
	}
	
	
	public static void printBuffer() throws IOException{
		while(instream.available() > 0){
			char i = (char) instream.read();
			fp.print(""+ i);
		}
	}
	public static void printEveryThingComming() throws IOException{
		
		boolean cont = true;
		while(cont){
			printBuffer();
		}
	}
	
	
	public static void waitForChar(char waited) throws IOException{
		boolean cont = true;
		while(cont){
			if(instream.available() > 0){
				char i = (char) instream.read();
				fp.print(""+ i);
				if(i == waited ) {
					cont = false;
				}
			}
		}
		
	}


}
