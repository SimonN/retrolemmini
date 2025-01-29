/*
 * FILE MODIFIED BY RYAN SAKOWSKI
 * 
 * 
 * Copyright 2009 Volker Oth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lemmini.gui;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import lemmini.LemminiFrame;

/**
 * Frame with legal information.
 * @author Volker Oth
 */
public class LegalFrame extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean ok = false;
    private URL thisURL;
    
    /**
     * Creates new form LegalFrame
     */
    public LegalFrame() {
        initComponents();
        setMinimumSize(getSize());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneLegal = new javax.swing.JScrollPane();
        jEditorPaneLegal = new javax.swing.JEditorPane();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RetroLemmini - Disclaimer");
        setIconImage(Toolkit.getDefaultToolkit().getImage(LemminiFrame.class.getClassLoader().getResource("icon_32.png")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        ClassLoader loader = LegalFrame.class.getClassLoader();
        thisURL = loader.getResource("disclaimer.htm");
        jEditorPaneLegal.setEditable(false);
        try {
            jEditorPaneLegal.setPage(thisURL);
        } catch (java.io.IOException e1) {
            e1.printStackTrace();
        }
        jEditorPaneLegal.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jEditorPaneLegalHyperlinkUpdate(evt);
            }
        });
        jScrollPaneLegal.setViewportView(jEditorPaneLegal);

        jButtonOK.setText("I agree");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jButtonCancel.setText("I disagree");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneLegal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 466, Short.MAX_VALUE)
                        .addComponent(jButtonOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneLegal, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOK)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        synchronized (this) {
            notifyAll();
        }
    }//GEN-LAST:event_formWindowClosed
    
    private void jEditorPaneLegalHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jEditorPaneLegalHyperlinkUpdate
        URL url = evt.getURL();
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {                            
                if (url.sameFile(thisURL)) {
                    jEditorPaneLegal.setPage(url);
                } else {
                    Desktop.getDesktop().browse(url.toURI());
                }
            } catch (IOException | URISyntaxException ex) {
            }
        } else if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            if (url.sameFile(thisURL)) {
                jEditorPaneLegal.setToolTipText(url.getRef());
            } else {
                jEditorPaneLegal.setToolTipText(url.toExternalForm());
            }
        } else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
            jEditorPaneLegal.setToolTipText(null);
        }
    }//GEN-LAST:event_jEditorPaneLegalHyperlinkUpdate
    
    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        ok = true;
        dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed
    
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        ok = false;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    /**
     * OK button was pressed.
     * @return true: OK button was pressed.
     */
    public boolean isOK() {
        return ok;
    }
    
    /**
     * Blocks until the window is closed. If this window is already closed,
     * then this method returns immediately.
     */
    public synchronized void waitUntilClosed() {
        while (isVisible()) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JEditorPane jEditorPaneLegal;
    private javax.swing.JScrollPane jScrollPaneLegal;
    // End of variables declaration//GEN-END:variables
}
