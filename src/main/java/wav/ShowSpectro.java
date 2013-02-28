package wav;
import javax.swing.JFrame;

import panel.MultipleTimeTrace;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.extension.Spectrogram;
public class ShowSpectro {

	JFrame f = new JFrame();
	MultipleTimeTrace mtt = new MultipleTimeTrace();
	
	
	public ShowSpectro() {
		super();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(mtt);
		f.pack();
		f.setVisible(true);
	}

	public void plotSpectrum(double [] spectrum,String name){
		int freqCount = spectrum.length;
		for(int freqid = 0 ; freqid < freqCount ; freqid++){
			mtt.addValueLater(freqid,spectrum[freqid],name,0);
		}
	}

}
