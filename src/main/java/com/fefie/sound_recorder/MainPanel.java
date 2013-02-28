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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.fefie.sound_recorder.audio.AudioException;
import com.fefie.sound_recorder.audio.AudioUtils;
import com.fefie.sound_recorder.audio.RecordableClip;
import com.fefie.sound_recorder.utils.SwingUtils;

/**
 * Panel that uses the library to implement a "sound recorder" application that loads and saves sound files.
 * 
 * @author stefie10
 *
 */
public class MainPanel {
	private final JPanel mPanel;
	private final JFileChooser mFileChooser;
	private final AudioPanel mAudioPanel;

	
	public MainPanel() throws LineUnavailableException {
		
		
		mFileChooser = new JFileChooser(System.getProperty("user.dir"));

		
		mPanel = SwingUtils.boxPanel(BoxLayout.PAGE_AXIS);
		JMenuBar mBar = new JMenuBar();
		
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ExceptionActionListener(){
			public void doActionPerformed(ActionEvent ae) throws IOException, AudioException {
				if (mFileChooser.showOpenDialog(mPanel) == JFileChooser.APPROVE_OPTION) {
					open(mFileChooser.getSelectedFile());
				}
				
			}
			
		});
		fileMenu.add(openItem);
		
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ExceptionActionListener(){
			public void doActionPerformed(ActionEvent ae) throws IOException, AudioException {
				if (mFileChooser.showSaveDialog(mPanel) == JFileChooser.APPROVE_OPTION) {
					save(mFileChooser.getSelectedFile());
				}
				
			}
			
		});
		fileMenu.add(saveItem);
		
		
		mBar.add(fileMenu);
		
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		menuPanel.add(mBar);
		
		mPanel.add(menuPanel);
		
		
		mAudioPanel = new AudioPanel(AudioPanel.SHOW_CONTROLS);
		mPanel.add(mAudioPanel.getPanel());
		
		
		
	}
	
	public void save(File f) throws IOException {
		mAudioPanel.getClip().save(f, AudioUtils.RAW_TYPE);
	}
	
	public void open(File f) throws IOException, AudioException {
		AudioFormat format  = new AudioFormat(16000, 16, 1, false, false);
		RecordableClip c = new RecordableClip(f, format);
		mAudioPanel.showClip(c);
		
	}
	
	public JPanel getPanel() {
		return mPanel;
	}
}
