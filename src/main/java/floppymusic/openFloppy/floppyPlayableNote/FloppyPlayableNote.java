package floppymusic.openFloppy.floppyPlayableNote;

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
import java.util.ArrayList;

public class FloppyPlayableNote implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8836330856282298224L;
	
	protected ArrayList<Boolean> notesList = new ArrayList<Boolean>();
	
	public FloppyPlayableNote() {
		super();
		for(int i = 0 ; i< 128 ; i++){
			notesList.add(true);
		}
	}
	
	public void setCanPlay (int noteID, boolean canPlay){
		notesList.set(noteID, canPlay);
	}
	
	
	public boolean canPlay(int noteID){
		if(noteID >= notesList.size()){
			return false;
		}else{
			return notesList.get(noteID);
		}
	}
	
	public int noteCount () {
		return notesList.size();
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
	public static FloppyPlayableNote getInstance(String fileName){
		FloppyPlayableNote recoveredthing = null;
		try{
			//use buffering
			InputStream file = new FileInputStream( fileName );
			InputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );
			try{
				//deserialize the things
				recoveredthing = (FloppyPlayableNote)input.readObject();
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
