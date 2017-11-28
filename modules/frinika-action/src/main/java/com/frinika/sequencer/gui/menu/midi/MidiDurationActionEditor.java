/*
 * Created on February 9, 2007
 * 
 * Copyright (c) 2007 Jens Gulden
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
package com.frinika.sequencer.gui.menu.midi;

import com.frinika.sequencer.gui.TimeFormat;
import com.frinika.sequencer.gui.TimeSelector;
import com.frinika.sequencer.project.SequencerProjectContainer;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * GUI-component for setting options of a MidiDurationAction.
 *
 * (Created with NetBeans 5.5 gui-editor, see corresponding .form file.)
 *
 * @see MidiDurationAction
 * @author Jens Gulden
 */
public class MidiDurationActionEditor extends JPanel {

    private MidiDurationAction action;

    public MidiDurationActionEditor(MidiDurationAction action, SequencerProjectContainer project) {
        super();
        this.action = action;
        initComponents();
        final TimeSelector setTimeSelector = new TimeSelector(action.setTicks, project, TimeFormat.BEAT_TICK);
        setTimeSelector.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MidiDurationActionEditor.this.action.setTicks = setTimeSelector.getTicks();
            }
        });
        setTimeSelectorPanel.add(setTimeSelector);
        final TimeSelector changeTimeSelector = new TimeSelector(action.changeTicks, true, project, TimeFormat.BEAT_TICK);
        changeTimeSelector.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MidiDurationActionEditor.this.action.changeTicks = changeTimeSelector.getTicks();
            }
        });
        changeTimeSelectorPanel.add(changeTimeSelector);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        modeButtonGroup = new javax.swing.ButtonGroup();
        setRadioButton = new javax.swing.JRadioButton();
        setTimeSelectorPanel = new javax.swing.JPanel();
        changeRadioButton = new javax.swing.JRadioButton();
        changeTimeSelectorPanel = new javax.swing.JPanel();
        legatoRadioButton = new javax.swing.JRadioButton();
        legatoSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        modeButtonGroup.add(setRadioButton);
        setRadioButton.setSelected(true);
        setRadioButton.setText("set duration to");
        setRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        setRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                setRadioButtonStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(setRadioButton, gridBagConstraints);

        setTimeSelectorPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(setTimeSelectorPanel, gridBagConstraints);

        modeButtonGroup.add(changeRadioButton);
        changeRadioButton.setText("change duration by");
        changeRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        changeRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        changeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                changeRadioButtonStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(changeRadioButton, gridBagConstraints);

        changeTimeSelectorPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(changeTimeSelectorPanel, gridBagConstraints);

        modeButtonGroup.add(legatoRadioButton);
        legatoRadioButton.setText("create legato with gap");
        legatoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        legatoRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        legatoRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                legatoRadioButtonStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(legatoRadioButton, gridBagConstraints);

        legatoSpinner.setModel(new javax.swing.SpinnerNumberModel(action.legatoGap, 0, 999, 1));
        legatoSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                legatoSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(legatoSpinner, gridBagConstraints);

        jLabel1.setText("ticks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jLabel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void setRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_setRadioButtonStateChanged
        if (setRadioButton.isSelected()) {
            action.mode = MidiDurationAction.MODE_SET;
        }
    }//GEN-LAST:event_setRadioButtonStateChanged

    private void changeRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_changeRadioButtonStateChanged
        if (changeRadioButton.isSelected()) {
            action.mode = MidiDurationAction.MODE_CHANGE;
        }
    }//GEN-LAST:event_changeRadioButtonStateChanged

    private void legatoRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_legatoRadioButtonStateChanged
        if (legatoRadioButton.isSelected()) {
            action.mode = MidiDurationAction.MODE_LEGATO;
        }
    }//GEN-LAST:event_legatoRadioButtonStateChanged

    private void legatoSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_legatoSpinnerStateChanged
        action.legatoGap = (Integer) legatoSpinner.getValue();
    }//GEN-LAST:event_legatoSpinnerStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton changeRadioButton;
    private javax.swing.JPanel changeTimeSelectorPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton legatoRadioButton;
    private javax.swing.JSpinner legatoSpinner;
    private javax.swing.ButtonGroup modeButtonGroup;
    private javax.swing.JRadioButton setRadioButton;
    private javax.swing.JPanel setTimeSelectorPanel;
    // End of variables declaration//GEN-END:variables

}
