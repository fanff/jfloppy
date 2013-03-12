package jfloppy.audio.tools.panels;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jfloppy.audio.tools.IfaceAudioDataListener;

public class VectorPrinterPanel extends JPanel implements IfaceAudioDataListener{
	ArrayList<String> names = new ArrayList<String>();

	ArrayList<JLabel> labels = new ArrayList<JLabel>();
	public VectorPrinterPanel(String... names) {
		super();
		
		for(int i=0; i <names.length ;i++){
			String name = names[i];
			
			addLabel(name);
		}
	}

	@Override
	public void listen(double[] audioData) {
		for(int i=0; i <audioData.length ;i++){
			if(i>=labels.size() || labels.get(i)==null ){
				
				addLabel(i<names.size() ? names.get(i) : "noName");
			}
			JLabel label = labels.get(i);
			String name = names.get(i);
			label.setText(name+": "+audioData[i]);
		}
		
	}
	
	protected void addLabel(String name){
		JLabel label = new JLabel(name+":novalue");
		labels.add(label);
		
		names.add(name);
		this.add(label);
	}
	
}
