/*
 * Created on Apr 10, 2007
 *
 * Copyright (c) 2006-2007 P.J.Leonard
 * 
 * http://www.frinika.com
 * 
 * This file is part of Frinika.
 * 
 * Frinika is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * Frinika is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Frinika; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.frinika.toot.javasoundmultiplexed;

import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.core.ChannelFormat;
import uk.org.toot.audio.server.AudioLine;

class StereoInConnection implements AudioLine {

	private JavaSoundInDevice dev;

	private int chan[];

	ChannelFormat chanFormat;

	boolean isBigendian;

	public StereoInConnection(JavaSoundInDevice dev, int chan[]) {
		this.dev = dev;
		isBigendian = dev.getFormat().isBigEndian();
		this.chan = new int[chan.length];
		for (int i = 0; i < chan.length; i++)
			this.chan[i] = chan[i];

	}

        @Override
	public void open() {
		System.out.println(" JavaSoundConnection OPEN" + dev);
		dev.open();
	}

        @Override
	public int processAudio(AudioBuffer buffer) {
		byte bytes[] = dev.getBuffer();

		if (bytes == null) {
			int n = buffer.getSampleCount();
			for (int ch = 0; ch < chan.length; ch++) {

				float out[] = buffer.getChannel(ch);

				for (int i = 0; i < n; i++)
					out[i] = 0.0f;
			}
			return AUDIO_OK;
		}

		// System.out.println(" proc buff");

		int n = buffer.getSampleCount();
		int nchan = dev.getChannels();
		if (isBigendian) {
			for (int ch = 0; ch < chan.length; ch++) {
				int chanO = chan[ch];
				float out[] = buffer.getChannel(ch);

				for (int i = 0; i < n; i++) {
					int ib = i * 2 * nchan + chanO * 2;
					short sample = (short) ((0xff & bytes[ib + 1]) + ((0xff & bytes[ib]) * 256));
					float val = sample / 32768f;
					out[i] = val;
				}
			}
		} else {
			for (int ch = 0; ch < chan.length; ch++) {
				int chanO = chan[ch];
				float out[] = buffer.getChannel(ch);

				for (int i = 0; i < n; i++) {
					int ib = i * 2 * nchan + chanO * 2;
					short sample = (short) ((0xff & bytes[ib]) + ((0xff & bytes[ib+1]) * 256));
					float val = sample / 32768f;
					out[i] = val;
				}
			}
		}
		return AUDIO_OK;

	}

        @Override
	public void close() {
		// null op for
	}

	//
	public float getLatencyMilliseconds() {
		// TODO Auto-generated method stub
		return 0;
	}

        @Override
	public String getName() {
		return "in " + (chan[0] + 1) + "/" + (chan[1] + 1);
	}

        @Override
	public int getLatencyFrames() {
		// TODO Auto-generated method stub
		return 0;
	}

        @Override
	public ChannelFormat getChannelFormat() {
		// TODO Auto-generated method stub
		return ChannelFormat.STEREO;
	}

}
