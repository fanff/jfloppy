/**
 *
 * Copyright 2007 Stefanie Tellex
 *
 * This file is part of Fefie.com Sound Recorder.
 *
 * Fefie.com Sound Recorder is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Fefie.com Sound Recorder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.fefie.sound_recorder;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.fefie.sound_recorder.audio.AudioException;
import com.fefie.sound_recorder.audio.RecordableClip;
import com.fefie.sound_recorder.utils.SwingUtils;

/**
 * Panel that displays an audio clip, and has programmatic methods to 
 * stop, play, and record data to and from the clip. 
 * 
 * @author stefie10
 *
 */
public class AudioPanel implements AudioCtrlListener {
	private final JPanel mPanel;
	private final ClipViewerPanel mClipPanel;
	private RecordableClip mClip;
	public static final boolean SHOW_CONTROLS = true;
	public static final boolean HIDE_CONTROLS = false;
	public AudioPanel(boolean showControls) {
		mPanel = SwingUtils.boxPanel(BoxLayout.PAGE_AXIS);
		if (showControls) {
			AudioCtrlPanel p = new AudioCtrlPanel();
			mPanel.add(p.getPanel());
			p.addListener(this);
		}
		mClipPanel = new ClipViewerPanel();
		mPanel.add(mClipPanel.getPanel());
		
		
	}
	
	public RecordableClip getClip() {
		return mClip;
	}
	public void showClip(RecordableClip c) {
		mClipPanel.showClip(c);
		mClip = c;
		

	}
	
	public JPanel getPanel() {
		return mPanel;
	}
	
	public void play() throws AudioException {
		if (mClip != null) {
			mClip.start();
		}
	}
	public void pause() throws AudioException {
		if (mClip != null) {
			mClip.stop();
		}
	}
	public void stop() throws AudioException {
		if (mClip != null) {
			mClip.stop();
			mClip.reset();
		}
	}
	public void record() throws AudioException {
		System.out.println("Recording");
		mClip.record();
	}

}
