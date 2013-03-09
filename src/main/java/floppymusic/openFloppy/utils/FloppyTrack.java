package floppymusic.openFloppy.utils;

import java.awt.Frame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fr.irit.smac.may.lib.components.meta.CollectionInteger;

public class FloppyTrack implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8273409707532183948L;

	//Map<Integer,Integer> allFreqs = new TreeMap<Integer,Integer>();
	Integer [] allFreqs = new Integer[500000];
	
	int maxSize = 500000;
	/**
	 * @param frameIndex
	 * @return return the freq at this index or null if unasigned.
	 */
	public Integer getFreqAt(int frameIndex){
		
		//Integer ret = allFreqs.get(frameIndex);
		Integer ret = allFreqs[frameIndex];
		return ret;
	}

	/**
	 * 
	 * @param frameIndex
	 * @param freq
	 * @return true if the frame index is empty or if the freq is the same.
	 */
	public boolean canSetFreqAt(int frameIndex,int freq){
		Integer freqAtIndex = this.getFreqAt(frameIndex);
		
		if(freqAtIndex == null || freqAtIndex == freq){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param frameIndex
	 * @param freq
	 * @return true if the freq can be added
	 */
	public boolean setFreqAt(int frameIndex,int freq){
		if(this.canSetFreqAt(frameIndex, freq)){
			forceFreqAt(frameIndex, freq);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param frameIndex
	 * @param freq
	 * @return true if the freq can be added
	 */
	public void forceFreqAt(int frameIndex,int freq){
		//allFreqs.put(frameIndex, freq);
		allFreqs[frameIndex]  = freq ;
	}
	
	/**
	 * 
	 * @param fromIndex
	 * @param toindex
	 * @return true if the WHOLE space is free.
	 */
	public boolean canSetFreqFromTo(int fromIndex,int toindex,int freq){
		int currentIndex = fromIndex ;
		while(currentIndex < toindex && canSetFreqAt(currentIndex, freq)) {
			currentIndex++;
		}
			
		if(currentIndex == toindex){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * @param fromIndex
	 * @param toindex
	 * @param freq
	 * @return
	 */
	public boolean setFreqFromTo(int fromIndex,int toindex,int freq){
		if(canSetFreqFromTo(fromIndex, toindex, freq)){
			for(int currentIndex = fromIndex ; currentIndex < toindex ; currentIndex ++){
				setFreqAt(currentIndex, freq);
			}
			return true;
			
		}else{
			return false;
			
		}
	}
	
	/**
	 * TODO
	 * @return
	 */
	public LinkedList<FloppyFrame> getAllFrames(){
		LinkedList<FloppyFrame> allFrame = new LinkedList<FloppyFrame>( );
		
		Integer length = getLength();
		
		if(length == null ) return allFrame;
		
		
		FloppyFrame fpCurrent = null;
		
		for( int i = 0; i<= length;i++ ){
			if( getFreqAt(i) == null ){
				
				if(fpCurrent == null){
					//nothing to do, go next one
				}else{
					// it's the end of this frame
					fpCurrent.end = i;
					//add the Frame to the list
					allFrame.add(fpCurrent);
					fpCurrent = null;
				}
			}else{

				if(fpCurrent == null){
					//create a new Frame
					fpCurrent = new FloppyFrame(i, i, getFreqAt(i));
				}else{
					// extend the frame if it is the same Freq;
					if( getFreqAt(i) == fpCurrent.freq){
						fpCurrent.end = i+1;
					}else{
						//freq is different
						fpCurrent.end = i;
						//add the Frame to the end of the list
						allFrame.add(fpCurrent);
						fpCurrent = new FloppyFrame(i, i, getFreqAt(i));
					}
				}
			}
		}
		
		if(fpCurrent != null){
			fpCurrent.end = length+1;
			allFrame.add(fpCurrent);
		}

		return allFrame;
	}
	
	
	/**
	 * @return
	 */
	public int getLength(){
		/**
		Set<Integer> allKey = allFreqs.keySet();
		//find the max of allKey

		Integer max = null;
		for(Integer i : allKey){
			if(max == null || i> max){
				max = i;
			}
		}
		return max==null ? 0 : max.intValue();
		
		*/
		
		for(int i = maxSize-1 ; i>= 0 ; --i){
			Integer truc = allFreqs[i];
			
			if(truc != null){
				return i;
			}
		}
		return 0 ;
	}

	
}
