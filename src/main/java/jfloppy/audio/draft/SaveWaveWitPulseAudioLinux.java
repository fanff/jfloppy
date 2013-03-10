package jfloppy.audio.draft;

import java.io.IOException;

public class SaveWaveWitPulseAudioLinux {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void save(String fileName,Long durationMS) throws InterruptedException {
		//run a script during somesecs.
		String command = "/bin/bash recordPulse "+fileName;
		try {
			System.out.println("Command is : "+command);
			//ProcessBuilder builder = new ProcessBuilder(command);
			//builder.redirectErrorStream(true);
			//Process process = builder.start();
			Process p = Runtime.getRuntime().exec(command);
			
			//copy(p.getInputStream(),System.out);
			long timeStart = System.currentTimeMillis();
			
			long timeNow = timeStart;
			while (timeNow <= timeStart+durationMS) {
				//int c = p.getInputStream().read();
				//if (c == -1) break;
				//System.out.write((char)c);
				Thread.sleep(10);
				
				//System.out.println("lol");
				timeNow=System.currentTimeMillis();
			}
			
			System.out.println("Destroyyyy");
			p.destroy();
			//p.exitValue();
			Runtime.getRuntime().exec("killall parec");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

}
