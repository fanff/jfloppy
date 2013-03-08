package floppymusic.openFloppy.player;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import floppymusic.openFloppy.FloppyUtilFunc;
import floppymusic.openFloppy.OnlineFloppyFreqPlayer;

public class Main_SendScale implements ActionListener{
	JFrame frame = null;
	JPanel panel = null;
	JButton buttonOK = new JButton("STOP");
	JButton buttonNOTOK = new JButton("PLAY");
	JLabel topLabel = new JLabel();
	OnlineFloppyFreqPlayer offp = null;
	
	
	int floppyID = 5;
	
	int currentNote = 0;
	
	
	public void initProcess() throws InterruptedException{
		offp = new OnlineFloppyFreqPlayer("/dev/ttyACM0", 6);
		offp.waitUntilInitFinish();

		offp.playThisDelay(floppyID, 0);
		
	}
	
	public void noteWas(boolean noteOK){
		
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
			frame.setVisible(false);
			frame.removeAll();
			frame.dispose();
		}
	}
	
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
		frame.pack();
		frame.setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonOK){
			System.out.println("OK" );
			noteWas(true);
			doStep();
			
		}else if(e.getSource() == buttonNOTOK){
			System.out.println("NOTOK" );
			noteWas(false);
			
			
			doStep();
		}

	}

}
