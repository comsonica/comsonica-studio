// Copyright (C) 2007 Steve Taylor.
// Distributed under the Toot Software License, Version 1.0. (See
// accompanying file LICENSE_1_0.txt or copy at
// http://www.toot.org.uk/LICENSE_1_0.txt)

package uk.org.toot.midi.core;

import javax.sound.midi.MidiMessage;

public interface MidiTransport 
{
    public void transport(MidiMessage msg, long timestamp);
}
