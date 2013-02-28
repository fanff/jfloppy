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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.fefie.sound_recorder.audio.AudioException;
import com.fefie.sound_recorder.utils.SwingUtils;

/**
 * A control panel.  It communicates with an AudioCtrlListener when buttons are pushed.
 * @author stefie10
 *
 */
public class AudioCtrlPanel {
	
	private final JPanel mPanel;
	private final JButton mPlayButton;
	private final JButton mPauseButton;
	private final JButton mStopButton;
	private final JButton mRecordButton;
	public AudioCtrlPanel() {
		mPanel = SwingUtils.boxPanel(BoxLayout.LINE_AXIS);
		
		mPlayButton = new JButton("Play");
		mPanel.add(mPlayButton);
		mPauseButton = new JButton("Pause");
		mPanel.add(mPauseButton);
		mStopButton = new JButton("Stop");
		mPanel.add(mStopButton);
		mRecordButton = new JButton("Record");
		mPanel.add(mRecordButton);
		
		
		mPlayButton.addActionListener(new ExceptionActionListener() {
			public void doActionPerformed(ActionEvent ae) throws AudioException {
				for (AudioCtrlListener l : mListeners) {
					l.play();
				}
			}
		});
		
		mPauseButton.addActionListener(new ExceptionActionListener() {
			public void doActionPerformed(ActionEvent ae) throws AudioException {
				for (AudioCtrlListener l : mListeners) {
					l.pause();
				}
			}
		});
		
		
		mStopButton.addActionListener(new ExceptionActionListener() {
			public void doActionPerformed(ActionEvent ae) throws AudioException {
				for (AudioCtrlListener l : mListeners) {
					l.stop();
				}
			}
		});
		
		mRecordButton.addActionListener(new ExceptionActionListener() {
			public void doActionPerformed(ActionEvent ae) throws AudioException {
				for (AudioCtrlListener l : mListeners) {
					l.record();
				}
			}
		});
	}
	public JPanel getPanel() {
		return mPanel;
	}
	
	private final List<AudioCtrlListener> mListeners = new ArrayList();
	public void addListener(AudioCtrlListener l) {
		mListeners.add(l);
	}
}
