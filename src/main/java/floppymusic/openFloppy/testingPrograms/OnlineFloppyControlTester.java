package floppymusic.openFloppy.testingPrograms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import panel.agentGossip.FeedPanel;
import floppymusic.midi.ProcessMessage;
import fr.irit.smac.may.serial.SerialPortComp;
import fr.irit.smac.may.serial.WaitFor;
import fr.irit.smac.may.serial.iface.SendPort;
import fr.irit.smac.may.serial.iface.SerialPortStatus;
import fr.irit.smac.may.serial.impl.serial.SerialPortImpl;

/***
 *  byte directionPIN[FLOPPY_COUNT] = {
  	13,3,5,7,9,11};
 	byte motorStepPIN[FLOPPY_COUNT] = {
  	12,2,4,6,8,10};
 * @author fanf
 *
 */
public class OnlineFloppyControlTester implements ActionListener{


	static String portName = "/dev/ttyACM0";

	InputStream instream;
	BufferedReader br;

	FeedPanel fp = new FeedPanel("arduino");

	JPanel mainPanel = null;
	SerialPortStatus statusPort ;
	SendPort sendPort;	
	
	ConcurrentLinkedQueue<String> linesRead = new ConcurrentLinkedQueue<String>();

	JTextField floppyIDField = new JTextField(6);
	JTextField freq = new JTextField(6);
	JButton sendButton  = new JButton("Send");
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == sendButton){
			try {
				
				//check values
				System.out.println("checking ");
				int floppIDD = Integer.valueOf(floppyIDField.getText());
				if(floppIDD<0 || floppIDD > 5 ){
					throw new Exception("floppyID Problem");
				}
				
				int freqD = Integer.valueOf(freq.getText());
				if(freqD<0 || freqD > 65535 ){
					throw new Exception("Freq Problem");
				}
				//send values 
				
				System.out.println("floppy ID : "+floppIDD+"   freqD : "+freqD);
				sendPort.sendOneByte(floppIDD);
				sendPort.sendOneByte(freqD/256);
				sendPort.sendOneByte(freqD);
			} catch(Exception e){
				System.err.println(e.getMessage());
			}
			
			
		}
	}
	
	
	public void init(){
		try {
			JFrame f = new JFrame();
			mainPanel = new JPanel(new BorderLayout());
			
			mainPanel.add(fp,BorderLayout.CENTER);
			
			JPanel sendFramePanel = new JPanel();
			
			JPanel labelsPanel = new JPanel();
			JPanel valuesPanel = new JPanel();
			labelsPanel.add(new JLabel("FloppyID"));
			labelsPanel.add(new JLabel("freq"));
			valuesPanel.add(floppyIDField);
			valuesPanel.add(freq);
			
			sendFramePanel.add(labelsPanel,BorderLayout.NORTH);
			sendFramePanel.add(valuesPanel,BorderLayout.SOUTH);
			mainPanel.add(sendFramePanel,BorderLayout.NORTH);
			
			sendButton.addActionListener(this);
			mainPanel.add(sendButton,BorderLayout.EAST);
			
			f.add(mainPanel);
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

			InputStreamReader inputStreamReader = new InputStreamReader(instream, Charset.forName("US-ASCII"));
			br = new BufferedReader(inputStreamReader);

			
		} catch (Exception e) {
			System.err.println("ERROR DURING INIT");
			e.printStackTrace();
		}

	}

	public void doLoop(){
		while(true){
			try {
				String line = WaitFor.line(br);

				linesRead.add(line);
				if(line == null){
					System.err.println("ERROR null line read ");
				}else{
					SwingUtilities.invokeLater(new ProcessMessage(line, System.currentTimeMillis(), this));
					

				}
			}catch (IOException e) {
				System.err.println("ERROR DURING  LOOP ");
				e.printStackTrace();
			}

		}
	}
	
	boolean initFinished = false;
	
	public void processMessageCallBack(ProcessMessage pm){
		String line = linesRead.poll();
		fp.println("openFloppy >"+line);
		
		if(!initFinished && line.equals("#") ){
			fp.println("floppyctl  >"+"2");
			
			sendPort.sendOneByte(42);
			initFinished = true;
		}
		
		//si la lignes est "s" alors faire des trucs
		/**
		 * 

openFloppy >10 354 472
openFloppy >0 354 777
openFloppy >10 354 472
openFloppy >0 354 777
openFloppy >10 354 472
openFloppy >0 354 777
openFloppy >10 354 472
openFloppy >0 354 777
openFloppy >10 354 472
openFloppy >0 453 777

		 */
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OnlineFloppyControlTester useOpenFloppy =  new OnlineFloppyControlTester();

		useOpenFloppy.init();
		useOpenFloppy.doLoop();

	}

	 

}
