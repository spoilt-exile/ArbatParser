/**
* This file is part of ArbatParser application (check README).
* Copyright (C) 2012-2013 Stanislav Nepochatov
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
**/

package arbatparser;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * Entry editor dialog class.
 * @author Stanislav Nepochatov
 */
public class EntryEditor extends javax.swing.JFrame {
    
    /**
     * Table model for rule table.
     */
    DefaultTableModel ruleTableModel;
    
    /**
     * Current selected entry.
     */
    private FilterEntry.EntryRule currentRule;
    
    /**
     * Temp list of rules for new entries.
     */
    private List<FilterEntry.EntryRule> tempList = new ArrayList();
    
    /**
     * Flag which display is entry is new or edit mode.
     */
    private Boolean isNew = false;

    /**
     * Creates new form EntryEditor
     */
    public EntryEditor(FilterEntry filter) {
        initComponents();
        if (filter == null) {
            isNew = true;
            this.tempList = new ArrayList<FilterEntry.EntryRule>();
            this.saveBut.setText("Додати");
        } else {
            this.nameField.setText(filter.getEntryName());
            this.flagsField.setText(filter.getEntryFlags());
            this.tempList = FilterEntry.makeCopy(filter.getRuleList());
        }
        this.buildRuleTable();
        this.ruleTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (e.getFirstIndex() != -1 && e.getFirstIndex() < EntryEditor.this.tempList.size() && EntryEditor.this.tempList.size() > 0) {
                    EntryEditor.this.currentRule = EntryEditor.this.tempList.get(e.getFirstIndex());
                    EntryEditor.this.editBut.setEnabled(true);
                    EntryEditor.this.removeBut.setEnabled(true);
                    EntryEditor.this.refreshRule();
                } else {
                    EntryEditor.this.currentRule = null;
                    EntryEditor.this.editBut.setEnabled(false);
                    EntryEditor.this.removeBut.setEnabled(false);
                }
            }
        });
        if (isNew || this.tempList.isEmpty()) {
            this.autoBut.setEnabled(true);
        }
        this.ruleTable.getColumnModel().getColumn(0).setMaxWidth(50);
    }
    
    /**
     * Refresh rule ui elements by using current rule.
     */
    private void refreshRule() {
        if (this.currentRule != null) {
            this.ruleField.setText(this.currentRule.getRuleFilter());
            if (this.currentRule.getCurrRuleType().equals(FilterEntry.EntryRule.RuleTypes.DIRECTORY)) {
                this.typeBox.setSelectedIndex(0);
            } else {
                this.typeBox.setSelectedIndex(1);
            }
        }
    }
    
    /**
     * Build table with current rule list.
     */
    private void buildRuleTable() {
        ruleTableModel = new javax.swing.table.DefaultTableModel(
                        new Object [][] {},
                        new String [] {"Тип", "Текст"}
                    ) {
                        Class[] types = new Class [] {
                            java.lang.String.class, java.lang.String.class
                        };
                        boolean[] canEdit = new boolean [] {
                            false, false
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
        for (FilterEntry.EntryRule currEntry: this.tempList) {
            String tempID = "D";
            if (currEntry.getCurrRuleType().equals(FilterEntry.EntryRule.RuleTypes.TEXT)) {
                tempID = "T";
            }
            this.ruleTableModel.addRow(new Object[] {tempID, currEntry.getRuleFilter()});
        }
        this.ruleTable.setModel(ruleTableModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        flagsField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ruleTable = new javax.swing.JTable();
        ruleField = new javax.swing.JTextField();
        typeBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addBut = new javax.swing.JButton();
        editBut = new javax.swing.JButton();
        removeBut = new javax.swing.JButton();
        cancelBut = new javax.swing.JButton();
        saveBut = new javax.swing.JButton();
        autoBut = new javax.swing.JButton();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Редагування запису");

        jLabel1.setText("Назва");

        flagsField.setEditable(false);
        flagsField.setText("-nnn");

        jLabel2.setText("Ознаки");

        ruleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(ruleTable);

        typeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Напрямок (D)", "Текст (T)" }));

        jLabel3.setText("Тип");

        jLabel4.setText("Текст фільтру");

        addBut.setText("Додати");
        addBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButActionPerformed(evt);
            }
        });

        editBut.setText("Редагувати");
        editBut.setEnabled(false);
        editBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButActionPerformed(evt);
            }
        });

        removeBut.setText("Видалити");
        removeBut.setEnabled(false);
        removeBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButActionPerformed(evt);
            }
        });

        cancelBut.setText("Відмінити");
        cancelBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButActionPerformed(evt);
            }
        });

        saveBut.setText("Зберігти");
        saveBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButActionPerformed(evt);
            }
        });

        autoBut.setText("Авто");
        autoBut.setToolTipText("Створити автоматичный фільтр напрямку");
        autoBut.setEnabled(false);
        autoBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(flagsField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(editBut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(removeBut))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(typeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(ruleField)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(autoBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveBut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelBut)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flagsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ruleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBut)
                    .addComponent(editBut)
                    .addComponent(removeBut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBut)
                    .addComponent(saveBut)
                    .addComponent(autoBut))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButActionPerformed
        if (!this.ruleField.getText().isEmpty()) {
            String tempID = (this.typeBox.getSelectedIndex() == 0 ? "D" : "T");
            FilterEntry.EntryRule newRule = new FilterEntry.EntryRule(tempID, this.ruleField.getText());
            this.tempList.add(newRule);
            this.ruleTableModel.addRow(new Object[] {tempID, this.ruleField.getText()});
        }
    }//GEN-LAST:event_addButActionPerformed

    private void cancelButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButActionPerformed

    private void editButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButActionPerformed
        if (!this.ruleField.getText().isEmpty()) {
            String tempID = (this.typeBox.getSelectedIndex() == 0 ? "D" : "T");
            FilterEntry.EntryRule newRule = new FilterEntry.EntryRule(tempID, this.ruleField.getText());
            this.tempList.set(this.ruleTable.getSelectedRow(), newRule);
            //this.tempList.add(newRule);
            this.ruleTableModel.setValueAt(tempID, this.ruleTable.getSelectedRow(), 0);
            this.ruleTableModel.setValueAt(this.ruleField.getText(), this.ruleTable.getSelectedRow(), 1);
            //this.ruleTableModel.addRow(new Object[] {tempID, this.ruleField.getText()});
        }
    }//GEN-LAST:event_editButActionPerformed

    private void saveButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButActionPerformed
        FilterEntry newEntry = new FilterEntry(this.nameField.getText(), this.flagsField.getText(), this.tempList);
        if (this.isNew) {
            ArbatParser.window.addFilterEntry(newEntry);
        } else {
            ArbatParser.window.replaceFilterEntry(newEntry);
        }
        this.dispose();
    }//GEN-LAST:event_saveButActionPerformed

    private void autoButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoButActionPerformed
        this.tempList.add(new FilterEntry.EntryRule("D", this.nameField.getText()));
        this.ruleTableModel.addRow(new Object[] {"D", this.nameField.getText()});
    }//GEN-LAST:event_autoButActionPerformed

    private void removeButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButActionPerformed
        int currPos = this.ruleTable.getSelectedRow();
        this.tempList.remove(currPos);
        this.buildRuleTable();
    }//GEN-LAST:event_removeButActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EntryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EntryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EntryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EntryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new EntryEditor(null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBut;
    private javax.swing.JButton autoBut;
    private javax.swing.JButton cancelBut;
    private javax.swing.JButton editBut;
    private javax.swing.JTextField flagsField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton removeBut;
    private javax.swing.JTextField ruleField;
    private javax.swing.JTable ruleTable;
    private javax.swing.JButton saveBut;
    private javax.swing.JComboBox typeBox;
    // End of variables declaration//GEN-END:variables
}
