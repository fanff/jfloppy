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

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fefie.sound_recorder.audio.RecordableClip;
import com.fefie.sound_recorder.audio.RecordableClip.ClipListener;
import com.fefie.sound_recorder.utils.SwingUtils;

/**
 * A panel that displays the waveform of an audio file. 
 * @author stefie10
 *
 */
public class ClipViewerPanel {
	private final JPanel mPanel;
	private final JLabel mClipInfo;
	private final ClipDrawer mClipDrawer;
	public ClipViewerPanel() {
		mPanel = SwingUtils.boxPanel(BoxLayout.PAGE_AXIS);
		mClipInfo = new JLabel("No clip loaded.");
		mPanel.add(mClipInfo);
		mClipDrawer = new ClipDrawer();
		mPanel.add(mClipDrawer.getPane());
		

	}
	
	public JPanel getPanel() {
		return mPanel;
	}
	public void showClip(final RecordableClip c) {
		mClipDrawer.setClip(c);
		mClipInfo.setText("Length: " + c.getLengthInSeconds() + " seconds.");
		
		c.addClipListener(new ClipListener() {
			public void newData(int offset, int length) {
				mClipInfo.setText("Length: " + c.getLengthInSeconds() + " seconds.");
			}
			public void newPlayHead(int playhead) {
			}
		});

		
	}
	
	private static class ClipDrawer  {
		private static final class WaveformPanel extends JPanel {
			int playHeadPixel;
			private RecordableClip mClip;
			public WaveformPanel() {
				
			}
			public void setClip(RecordableClip clip) {
				mClip = clip;
			}
			void drawBar(Graphics g) {
				if(g!=null) {
				  g.setXORMode(Color.BLUE);
				  g.drawLine(playHeadPixel, 0, playHeadPixel, getSize().height);
				}
			}

			public void moveBar(int php) {
				//drawBar(g);
				playHeadPixel=php;
				//drawBar(g);
			}

			@Override
			public void paint(Graphics g1) {
				Graphics2D g = (Graphics2D) g1;
				g1.clearRect(0, 0, getWidth(), getHeight());
				
				if (mClip == null || mClip.getLengthInSamples() == 0) {
					return;
				} 
				
				
				g.setColor(Color.BLACK);
				int samplesPerPixel = mClip.getLengthInSamples() / getSize().width;
				
				float average_sum = 0;
				float average_ct = 0;
				
				int max_sample_height = mClip.getMaxSampleHeight() / 2; 
				int pixel = 0;
				int midY = getSize().height / 2;
				
				g.setColor(Color.BLACK);
				int lasty = 0;
				int lastx = 0;
				for (int i = 0; i < mClip.getLengthInSamples(); i++) {
					int sample = mClip.getSample(i);
					average_sum += sample;
					average_ct++;
					
					if (average_ct % samplesPerPixel == 0) {
						float average = average_sum / average_ct;
						int height = Math.round((average / max_sample_height) * getSize().height);
						int x = pixel;
						int y = midY - height;
						g.drawLine(lastx, lasty, x, y);
						lastx = x;
						lasty = y;
						pixel++;
						average_sum = 0;
						average_ct = 0;
					}
					
				}
				
			
				//g.drawRect(0, 10, 1, -10);
				g.setColor(Color.GRAY);
				g.drawLine(0, midY, getSize().width, midY);
				
				if (samplesPerPixel == 0) {
					playHeadPixel = 0;
				} else {
					playHeadPixel = mClip.getPlayHead() / samplesPerPixel;
				}
				drawBar(g);
			}
		}

		WaveformPanel jp=null;
		
		
		
		private JPanel mPanel;
		
		public JComponent getPane() { 
			return mPanel;
		}
		
		public ClipDrawer() {
			mPanel = SwingUtils.boxPanel(BoxLayout.PAGE_AXIS);
			
			
			jp=new WaveformPanel();
			
			mPanel.add(jp);
			
		}
		
		public void setClip(RecordableClip c) {
			c.addClipListener(new ClipListener() {
				public void newData(int offset, int length) {
					jp.repaint();
				}
				public void newPlayHead(int playhead) {
					jp.moveBar(playhead);
					jp.repaint();	
				}
			});

			jp.setClip(c);
				
		}
	}
}
