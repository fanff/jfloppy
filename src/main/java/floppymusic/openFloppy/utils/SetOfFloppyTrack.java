package floppymusic.openFloppy.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SetOfFloppyTrack implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7436996881254940449L;
	FloppyTrack[] alltracks = null;

	double defaultTimeFactor = 1D;

	public SetOfFloppyTrack(int trackCount){
		super();
		this.alltracks = new FloppyTrack[trackCount];
		
		for(int i = 0 ; i< trackCount ; i++){
			alltracks[i] = new FloppyTrack();
		}
	}
	public SetOfFloppyTrack(FloppyTrack[] alltracks) {
		super();
		this.alltracks = alltracks;
	}

	public int longestTrackDuration (){
		int max = 0 ;
		for(FloppyTrack ft : alltracks){
			if(ft.getLength() > max){
				max = ft.getLength();
			}
		}

		return max;
	}


	public int getFreqForThisTrack(int floppyID,int index){
		int returnedFreq = 0;
		try{
			Integer freqI = alltracks[floppyID].getFreqAt(index);

			returnedFreq = freqI==null ? 0 : freqI.intValue();

		}catch (Exception e) {
		}
		return returnedFreq;
	}

	public FloppyTrack getTrack(int trackID){
		return alltracks[trackID];
	}
	public int getTrackCount (){
		return alltracks==null ? 0 : alltracks.length;
	}

	public double getDefaultTimeFactor() {
		return defaultTimeFactor;
	}

	public void setDefaultTimeFactor(double defaultTimeFactor) {
		this.defaultTimeFactor = defaultTimeFactor;
	}

	public void saveToFile(String fileName){
		try{
			//use buffering
			OutputStream file = new FileOutputStream( fileName );
			OutputStream buffer = new BufferedOutputStream( file );
			ObjectOutput output = new ObjectOutputStream( buffer );
			try{
				output.writeObject(this);
			}
			finally{
				output.close();
			}
		}  
		catch(IOException ex){
			System.err.println( "Cannot perform save "+ ex);
			ex.printStackTrace();
		}
	}

	public static SetOfFloppyTrack loadFromFile(String fileName){
		SetOfFloppyTrack recoveredthing = null;
		try{
			//use buffering
			InputStream file = new FileInputStream( fileName );
			InputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );
			try{
				//deserialize the List
				recoveredthing = (SetOfFloppyTrack)input.readObject();
			}
			finally{
				input.close();
			}
		}
		catch(ClassNotFoundException ex){
			System.err.println("Cannot perform input. Class not found."+ ex);
		}
		catch(IOException ex){
			System.err.println( "Cannot perform input."+ ex);
		}
		
		return recoveredthing;
	}

}
