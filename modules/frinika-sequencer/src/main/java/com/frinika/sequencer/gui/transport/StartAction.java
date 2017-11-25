package com.frinika.sequencer.gui.transport;

import static com.frinika.localization.CurrentLocale.getMessage;
import com.frinika.sequencer.FrinikaSequencer;
import com.frinika.sequencer.project.SequencerProjectContainer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class StartAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FrinikaSequencer sequencer;
	private SequencerProjectContainer project;
	
	public StartAction(SequencerProjectContainer project) {
		super(getMessage("sequencer.project.start_stop"));
		this.sequencer=project.getSequencer();
		this.project=project;

	}
	
        @Override
	public void actionPerformed(ActionEvent arg0) {
			
		if (!sequencer.isRunning()) sequencer.start();	
	}
	
}
