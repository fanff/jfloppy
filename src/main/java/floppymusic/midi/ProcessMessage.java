package floppymusic.midi;

import floppymusic.openFloppy.testingPrograms.OnlineFloppyControlTester;

public class ProcessMessage implements Runnable {

	String line;
	long timeReceived;
	
	OnlineFloppyControlTester source;
	
	
	public ProcessMessage(String line, long timeReceived, OnlineFloppyControlTester source) {
		super();
		this.line = line;
		this.timeReceived = timeReceived;
		this.source = source;
	}


	@Override
	public void run() {
		source.processMessageCallBack(this);
	}

}
