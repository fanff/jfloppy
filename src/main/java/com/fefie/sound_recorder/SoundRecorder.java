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

import java.io.File;

import javax.swing.JFrame;

import com.fefie.sound_recorder.utils.SwingUtils;

/**
 * Example main program using the sound recorder
 * @author stefie10
 *
 */
public class SoundRecorder {
	public static void main(String[] args) throws Exception {
		MainPanel mp = new MainPanel();
		if (args.length == 1) {
			mp.open(new File(args[0]));
		} else {
			mp.open(new File("tmp.raw"));
		}
		JFrame f = SwingUtils.showPanel(mp.getPanel());
		f.setSize(1000,300);
		 
		
		Object o = new Object();
		synchronized(o) {
			o.wait();
		}

		
	}

}
