package floppymusic.openFloppy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.SwingUtilities;

import fr.irit.smac.may.serial.SerialPortComp;
import fr.irit.smac.may.serial.WaitFor;
import fr.irit.smac.may.serial.iface.SendPort;
import fr.irit.smac.may.serial.iface.SerialPortStatus;
import fr.irit.smac.may.serial.impl.serial.SerialPortImpl;

public class FloppyMusicPlayer {
	static String portName = "/dev/ttyACM0";

	InputStream instream;
	BufferedReader br;

	SerialPortStatus statusPort ;
	SendPort sendPort;	

	Thread loopThread = null;
	public FloppyMusicPlayer() {
		super();
		try{
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

			loopThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					FloppyMusicPlayer.this.doLoop();
				}
			});
			
			loopThread.start();
			
		} catch (Exception e) {
			System.err.println("ERROR DURING INIT");
			e.printStackTrace();
		}
	}
	
	public void doLoop(){
		while(true){
			try {
				//Thread.sleep(1);
				String line = WaitFor.line(br);

				if(line == null){
					System.err.println("ERROR null line read ");
				}else{
					this.processMessageCallBack(line);
					//SwingUtilities.invokeLater(new ProcessMessage(line, System.currentTimeMillis(), this));
				}
			}catch (Exception e) {
				System.err.println("ERROR DURING  LOOP ");
				e.printStackTrace();
			}

		}
	}
	
	
	public boolean initFinished = false;
	public void processMessageCallBack(String lineReceived){
		//fp.println("openFloppy >"+line);
		
		if(!initFinished && lineReceived.equals("#") ){
			//fp.println("floppyctl  >"+"2");
			
			sendPort.sendOneByte(42);
			initFinished = true;
		}
		
		if(initFinished){
			System.out.println("floppy say->"+lineReceived);
		}
		
		//si la lignes est "s" alors faire des trucs
	}
	
	public void playThis(int floppID,int frameEnd,int freqD){
		try {
			
			//check values
			System.out.println("checking ");
			if(floppID<0 || floppID > 5 ){
				throw new Exception("floppyID Problem");
			}
			
			if(frameEnd<0 || frameEnd > 65535 ){
				throw new Exception("durationD Problem");
			}
			if(freqD<0 || freqD > 65535 ){
				throw new Exception("Freq Problem");
			}
			//send values 
			
			System.out.println("floppy ID : "+floppID+"  durationD : "+frameEnd+"   freqD : "+freqD);
			sendPort.sendOneByte(floppID);
			sendPort.sendOneByte(frameEnd/256);
			sendPort.sendOneByte(frameEnd);
			sendPort.sendOneByte(freqD/256);
			sendPort.sendOneByte(freqD);
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	
}
