/*
 * Created on Jul 4, 2007
 *
 * Copyright (c) 2006-2007 Jens Gulden
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

package com.frinika.gui.util;

import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * A simple properties-editor gui element.
 * 
 * @author Jens Gulden
 */
public class PropertiesEditor extends JPanel implements TableModelListener {
    
    private static final long serialVersionUID = 1L;
	
    protected Properties properties;
    
    /** Creates new form PropertiesEditor */
    public PropertiesEditor() {
        initComponents();
    }
    
    /** Creates new form PropertiesEditor */
    public PropertiesEditor(Properties p) {
        this();
        setProperties(p);
    }
    
    public void setProperties(Properties p) {
        this.properties = p;
        updateModel();
    }
    
    public Properties getProperties() {
        return properties;
    }
    
    @Override
    public void tableChanged(TableModelEvent e) {
        int index = e.getFirstRow();
        String key = propertiesTable.getModel().getValueAt(index, 0).toString();
        String value = propertiesTable.getModel().getValueAt(index, 1).toString();
        properties.setProperty(key, value);
    }
    
    protected void updateModel() {
        TableModel model = new DefaultTableModel(
            propertiesToArray(this.properties),
            new String [] {
                "Parameter", "Value"
            }
        ) {
            Class[] types = new Class [] {
                String.class, String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        model.addTableModelListener(this);
        propertiesTable.setModel(model);
    }
    
    protected Object[][] propertiesToArray(Properties p) {
        Object[][] o = new Object[p.size()][2];
        int i = 0;
        for (java.util.Map.Entry e : p.entrySet()) {
            o[i][0] = e.getKey();
            o[i][1] = e.getValue();
            i++;
        }
        return o;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonsPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        propertiesTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        addButton.setText("Add...");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonsPanel.add(addButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonsPanel.add(removeButton, gridBagConstraints);

        add(buttonsPanel, java.awt.BorderLayout.EAST);

        propertiesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Audio Parameter", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane.setViewportView(propertiesTable);

        add(scrollPane, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String newKey = JOptionPane.showInputDialog("Name of a new entry to add:").toString();
        if (newKey != null) {
            if (properties.get(newKey) == null) { // not there yet
                properties.put(newKey, "");
                updateModel();
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int index = propertiesTable.getSelectedRow();
        if (index >= 0) {
            Object o = propertiesTable.getModel().getValueAt(index, 0);
            if (o != null) {
                if (JOptionPane.showConfirmDialog(this, "Remove entry '"+o.toString()+"'?", "Remove configuration entry", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    properties.remove(o);
                    updateModel();
                }
            }
        }
    }//GEN-LAST:event_removeButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JTable propertiesTable;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    
}
