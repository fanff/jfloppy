package floppymusic;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Map.Entry;

import javax.swing.JFrame;

import fr.irit.smac.may.serial.SerialPortComp;
import fr.irit.smac.may.serial.WaitFor;
import fr.irit.smac.may.serial.iface.SendPort;
import fr.irit.smac.may.serial.iface.SerialPortStatus;
import fr.irit.smac.may.serial.impl.serial.SerialPortImpl;

import panel.agentGossip.FeedPanel;

/**
 * Send some data to serialPort and wait for a textLine
 * 
 * Sending byte, integer <655xx, struct.
 * 
 * 
 * @author fanf
 *
 */
public class TestSendingData {

	static String portName = "/dev/ttyACM0";
	
	InputStream instream;
	BufferedReader br;
	
	FeedPanel fp = new FeedPanel("arduino");

	SerialPortStatus statusPort ;
	SendPort sendPort;
	public void init(){
		JFrame f = new JFrame();
		f.add(fp);
		f.setVisible(true);
		f.setPreferredSize(new Dimension(500,500));
		f.pack();
		
		System.setProperty("gnu.io.rxtx.SerialPorts",portName);
		SerialPortComp.Component serialComp = new SerialPortComp.Component(
				new SerialPortImpl(), new SerialPortComp.Bridge() {}
				);
		
		serialComp.start();
		statusPort = serialComp.statusPort();
		sendPort = serialComp.sendingPort();
		statusPort.connect(portName, 115200, 0, 0, 0);
		
		
		instream = statusPort.getInputStream();
		
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(instream, Charset.forName("US-ASCII"));
			br = new BufferedReader(inputStreamReader);
			
			
			String line = WaitFor.line(br);
			
			if(line == null){
				fp.println("Line Is NULL");
			}else{
				fp.println("line is "+line);
				
			}
			
			
			for(Entry<String,Charset> e : Charset.availableCharsets().entrySet()){
				//fp.print(" -> "+e.getKey()+" -> "+e.getValue()+"\n");
			}
						
		} catch (IOException e) {
			System.err.println("ERROR DURING INIT");
			e.printStackTrace();
		}
		
	}
	
	public void test1() throws IOException{
		
		String line = WaitFor.line(br);
		
		if(line == null){
			fp.println(" test1 : Line Is NULL");
		}else{
			fp.println(" test1 : line is "+line);
			
		}
		
	}
	
	public void test2() throws IOException{
		String testID = "test2";
		
		int i = new Random().nextInt(255);
		sendPort.sendOneByte(i);
		
		String line = WaitFor.line(br);

		String shouldReceive = "test2 : "+i;
		
		if(line == null){
			fp.println(" test2 : Line Is NULL");
		}else{
			fp.println(shouldReceive + "<- "+testID+" should see ");
			fp.println(line+"<- "+testID+" received");
			
			if(line.equals(shouldReceive)){
				fp.println("OKAY "+testID);
			}else{
				fp.println("ERROR "+testID);
			}
			
		}
		
	}
	public void test3() throws IOException{
		for(int i = 0 ; i<1 ; i ++){
			String testID = "test3";
			fp.println(testID+" waitfor line");
			String line = WaitFor.line(br);
			
			String shouldReceive = "";
			
			if(line == null){
				fp.println(" test2 : Line Is NULL");
			}else{
				fp.println(shouldReceive + "<- "+testID+" should see ");
				fp.println(line+"<- "+testID+" received");
				if(line.equals(shouldReceive)){
					fp.println("OKAY "+testID);
				}else{
					fp.println("ERROR "+testID);
				}
				
			}
			
		}
		
	}
	
	public void test4() throws IOException{
		String testID = "test4";
		
		int firstbyte = new Random().nextInt(255);
		sendPort.sendOneByte(firstbyte);
		int secondbyte = new Random().nextInt(255);
		sendPort.sendOneByte(secondbyte);
		
		String line = WaitFor.line(br);

		String shouldReceive = testID+" : "+firstbyte+" and "+secondbyte;
		
		if(line == null){
			fp.println(" test2 : Line Is NULL");
		}else{
			fp.println(shouldReceive + "<- "+testID+" should see ");
			fp.println(line+"<- "+testID+" received");
			
			if(line.equals(shouldReceive)){
				fp.println("OKAY "+testID);
			}else{
				fp.println("ERROR "+testID);
			}
			
		}
		
	}
	
	public void test5() throws IOException{
		String testID = "test5";
		
		int twobyteValue = new Random().nextInt(35555)+30000;
		
		sendPort.sendOneByte(twobyteValue/256);
		sendPort.sendOneByte(twobyteValue);

		
		String line = WaitFor.line(br);

		String shouldReceive = testID+" : "+twobyteValue;
		
		if(line == null){
			fp.println(" test5 : Line Is NULL");
		}else{
			fp.println(shouldReceive + "<- "+testID+" should see ");
			fp.println(line+"<- "+testID+" received");
			
			if(line.equals(shouldReceive)){
				fp.println("OKAY "+testID);
			}else{
				fp.println("ERROR "+testID);
			}
			
		}
		
	}
	
	public void test6() throws IOException{
		String testID = "test6";
		int floppyID = new Random().nextInt(255);
		
		sendPort.sendOneByte(floppyID);
		
		int pauseTime = new Random().nextInt(64000);
		
		sendPort.sendOneByte(pauseTime/256);
		sendPort.sendOneByte(pauseTime);

		int duration = new Random().nextInt(64000);
		
		sendPort.sendOneByte(duration/256);
		sendPort.sendOneByte(duration);
		
		
		String line = WaitFor.line(br);

		String shouldReceive = testID+" : floppyID:"+floppyID+" pauseTime:"+pauseTime+" duration:"+duration;
		
		if(line == null){
			fp.println(" test6 : Line Is NULL");
		}else{
			fp.println(shouldReceive + "<- "+testID+" should see ");
			fp.println(line+"<- "+testID+" received");
			
			if(line.equals(shouldReceive)){
				fp.println("OKAY "+testID);
			}else{
				fp.println("ERROR "+testID);
			}
			
		}
		
	}
	
	
	public void start(){
		
		//init,
		init();
		fp.println("end of init");
		//do Test 1
		
		try {
			test1();
			test2();
			
			//test3();
			
			test4();
			
			test5();
			test6();
		} catch (IOException e) {
			System.err.println("ERROR During someTest");
			e.printStackTrace();
		}
		//
		
		fp.println("end Of Test");
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		TestSendingData tsd = new TestSendingData();
		tsd.start();
		

	}

}
