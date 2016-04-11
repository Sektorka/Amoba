package amoba;

import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;

public class Chat extends javax.swing.JDialog {
    private final Stage stage;
    
    public Chat(java.awt.Dialog parent, boolean modal) {
        super(((Stage)parent).getMenu(), modal);
        
        stage = (Stage)parent;
        
        initComponents();
        ((DefaultCaret)taMessages.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        setLocationRelativeTo(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taMessages = new javax.swing.JTextArea();
        tfMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        taMessages.setEditable(false);
        taMessages.setColumns(20);
        taMessages.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        taMessages.setLineWrap(true);
        taMessages.setRows(5);
        taMessages.setWrapStyleWord(true);
        jScrollPane1.setViewportView(taMessages);

        tfMessage.setForeground(new java.awt.Color(204, 204, 204));
        tfMessage.setText("Üzenet...");
        tfMessage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMessageFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfMessageFocusLost(evt);
            }
        });
        tfMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfMessageKeyReleased(evt);
            }
        });

        btnSend.setText("Üzen");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfMessageFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfMessageFocusGained
        if(tfMessage.getText().equals("Üzenet...")){
            tfMessage.setForeground(Color.BLACK);
            tfMessage.setText("");
        }
    }//GEN-LAST:event_tfMessageFocusGained

    private void tfMessageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfMessageFocusLost
        if(tfMessage.getText().equals("")){
            tfMessage.setForeground(new Color(204,204,204));
            tfMessage.setText("Üzenet...");
        }
    }//GEN-LAST:event_tfMessageFocusLost

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        sendMessage();
    }//GEN-LAST:event_btnSendActionPerformed

    private void tfMessageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMessageKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            sendMessage();
        }
    }//GEN-LAST:event_tfMessageKeyReleased

    public void addMessage(String message, String name){
        taMessages.append(Amoba.now() + " | " + name + ": " + message + "\r\n");
    }
    
    private void sendMessage(){
        if(!tfMessage.getText().equals("")){
            stage.sendChatMessage(tfMessage.getText());
            tfMessage.setText("");
            tfMessage.requestFocus();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taMessages;
    private javax.swing.JTextField tfMessage;
    // End of variables declaration//GEN-END:variables
}
