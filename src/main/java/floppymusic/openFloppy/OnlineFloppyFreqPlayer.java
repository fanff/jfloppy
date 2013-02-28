package floppymusic.openFloppy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Hashtable;

import fr.irit.smac.may.serial.SerialPortComp;
import fr.irit.smac.may.serial.WaitFor;
import fr.irit.smac.may.serial.iface.SendPort;
import fr.irit.smac.may.serial.iface.SerialPortStatus;
import fr.irit.smac.may.serial.impl.serial.SerialPortImpl;

public class OnlineFloppyFreqPlayer {


	String portName = "/dev/ttyACM0";

	InputStream instream;
	BufferedReader br;

	SerialPortStatus statusPort ;
	SendPort sendPort;	

	Thread loopThread = null;
	public boolean threadRun = true;
	Hashtable<Integer, Integer> currentlyPlayedNote = new Hashtable<Integer, Integer>();
	int floppyCount = 6;

	public boolean initFinished = false;


	public OnlineFloppyFreqPlayer(String portName, int floppyCount) {
		super();
		this.portName = portName;
		this.floppyCount = floppyCount;

		try{

			for(int i = 0 ; i < floppyCount ; i ++){
				currentlyPlayedNote.put(i, 0);
			}

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
					OnlineFloppyFreqPlayer.this.doLoop();
				}
			});

			loopThread.start();

		} catch (Exception e) {
			System.err.println("ERROR DURING INIT");
			e.printStackTrace();
		}
	}

	public void doLoop(){
		while(threadRun){
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


	public void waitUntilInitFinish ( ) throws InterruptedException{
		while(!initFinished){
			Thread.sleep(1);
		}
		Thread.sleep(3000);
	}
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
	public void muteAllFloppy(){
		for(int i = 0 ; i < floppyCount ; i++){
			this.playThisDelay(i, 0);
		}
	}
	public void playThisDelay(int floppID,int delay){
		try {

			//if the freqID to play is already played, return;
			if(currentlyPlayedNote.get(floppID) == delay) return;

			//calculate the delay to send to the floppy
			int delayToSend = delay;

			//check values
			if(floppID<0 || floppID >= floppyCount ){
				throw new Exception("floppyID Problem");
			}

			if(delayToSend<0 || delayToSend > 65535 ){
				throw new Exception("Freq Problem as delayto send is " + delayToSend);
			}
			//send values 

			System.out.println("floppy ID : "+floppID+" delay "+delayToSend);
			sendPort.sendOneByte(floppID);
			sendPort.sendOneByte(delayToSend/256);
			sendPort.sendOneByte(delayToSend);

			currentlyPlayedNote.put(floppID, delay);
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	public void disconnect(){
		try{
			statusPort.disconnect();

			threadRun = false;
		}catch (Exception e) {
			System.err.println("ERROR DURING  LOOP ");
			e.printStackTrace();
		}
	}
}
