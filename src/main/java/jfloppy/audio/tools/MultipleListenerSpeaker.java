package jfloppy.audio.tools;

public abstract class MultipleListenerSpeaker extends AbstractAudioSpeaker {
	//public abstract class DoubleListenerSpeaker implements IfaceAudioDataSpeaker{

	IfaceAudioDataSpeaker speaker1 = null;
	IfaceAudioDataSpeaker  speaker2 = null;


	public abstract void listen(int id,double[] data);

	public void registerSpeaker(int id,IfaceAudioDataSpeaker speaker){
		PrivateListener pl = new PrivateListener(this,id);
		speaker.registerListener(pl);
	}
	
	
	public class PrivateListener implements IfaceAudioDataListener {

		MultipleListenerSpeaker boss =null;
		int id =0;

		public PrivateListener(MultipleListenerSpeaker boss, int id) {
			super();
			this.boss = boss;
			this.id = id;
		}

		@Override
		public void listen(double[] audioData) {
			boss.listen(id,audioData);

		}

	}

}
