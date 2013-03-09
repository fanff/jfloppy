package floppymusic.openFloppy.player;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import floppymusic.openFloppy.FloppyUtilFunc;
import floppymusic.openFloppy.OnlineFloppyFreqPlayer;
import floppymusic.openFloppy.floppyPlayableNote.FloppyPlayableNote;

public class Main_SendScale implements ActionListener{
	JFrame frame = null;
	JPanel panel = null;
	JButton buttonOK = new JButton("OK");
	JButton buttonNOTOK = new JButton("NotOK");
	JLabel topLabel = new JLabel("No notes played");
	OnlineFloppyFreqPlayer offp = null;
	JTextField filenameforFloppyDef = new JTextField("playbleNote.fpn");

	int floppyID = 3;
	int currentNote = 0;


	FloppyPlayableNote recordForThisFloppy = new FloppyPlayableNote();


	public void initProcess() throws InterruptedException{
		offp = new OnlineFloppyFreqPlayer("/dev/ttyACM0", 6);
		offp.waitUntilInitFinish();

		offp.playThisDelay(floppyID, 0);

	}

	public void noteWas(boolean noteOK){
		if(noteOK){
			System.out.println("OK" );
		}else{
			System.out.println("NOT" );
		}

		recordForThisFloppy.setCanPlay(currentNote, noteOK);
	}

	public void doStep(){
		currentNote ++;
		if(currentNote<= 120){
			System.out.println("NoteID is : "+currentNote);
			topLabel.setText("NoteID is : "+currentNote);

			int delay = FloppyUtilFunc.calcCompareFromFreq(FloppyUtilFunc.prescaler,
					FloppyUtilFunc.noteToFreq(currentNote));

			offp.playThisDelay(floppyID,delay);

		}else{
			offp.playThisDelay(floppyID, 0);
			offp.disconnect();
			System.out.println("ENDPROGRAM" );

			panel.remove(topLabel);
			filenameforFloppyDef.setText("floppy"+floppyID+".fpn");
			panel.add(filenameforFloppyDef,BorderLayout.NORTH);

			frame.pack();
			//frame.setVisible(false);
			//frame.dispose();
			savingOperation = true;

		}
	}

	boolean savingOperation = false;


	public static void main(String[] args) throws Exception {
		Main_SendScale mss = new Main_SendScale();
		mss.initProcess();
		mss.doStep();
	}

	public Main_SendScale() {
		super();
		frame = new JFrame();
		panel = new JPanel(new BorderLayout());

		buttonOK.addActionListener(this);
		buttonNOTOK.addActionListener(this);

		panel.add(buttonOK,BorderLayout.WEST);
		panel.add(buttonNOTOK,BorderLayout.EAST);
		panel.add(topLabel,BorderLayout.NORTH);
		frame.add(panel);
		frame.setVisible(true);
		frame.pack();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(savingOperation){
			if(e.getSource() == buttonOK){
				String fileName = filenameforFloppyDef.getText();
				recordForThisFloppy.saveToFile(fileName);
				frame.dispose();
				
			}else if(e.getSource() == buttonNOTOK){
				frame.dispose();
			}

			offp.disconnect();
			
			for (int noteID = 0; noteID < 128; noteID++) {
				System.out.println("write : "+noteID+" "+recordForThisFloppy.canPlay(noteID));
				
			}
			
		}else{

			if(e.getSource() == buttonOK){
				noteWas(true);
				doStep();

			}else if(e.getSource() == buttonNOTOK){
				noteWas(false);
				doStep();
			}
		}

	}


}
