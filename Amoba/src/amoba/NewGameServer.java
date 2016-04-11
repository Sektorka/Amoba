package amoba;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class NewGameServer extends ResultDialog {

    private String serverName, joinPassword = "";
    private int width, height;
    
    public NewGameServer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        lblWidthValue.setText(slWidth.getValue() + "");
        lblHeightValue.setText(slHeight.getValue() + "");
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this);
        
        getRootPane().registerKeyboardAction(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createServer();
                }
            }, 
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), 
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    public String getServerName() {
        return serverName;
    }
    
    public String getJoinPassword(){
        return joinPassword;
    }

    public int getStageWidth() {
        return width;
    }

    public int getStageHeight() {
        return height;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblServerName = new javax.swing.JLabel();
        tfServerName = new javax.swing.JTextField();
        lblWidth = new javax.swing.JLabel();
        slWidth = new javax.swing.JSlider();
        lblWidthValue = new javax.swing.JLabel();
        lblHeight = new javax.swing.JLabel();
        slHeight = new javax.swing.JSlider();
        lblHeightValue = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblServerName1 = new javax.swing.JLabel();
        pfJoinPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Amőba :: Új játék indítása");

        lblServerName.setText("Szerver neve:");

        lblWidth.setText("Szélesség:");

        slWidth.setMinimum(10);
        slWidth.setPaintLabels(true);
        slWidth.setPaintTicks(true);
        slWidth.setValue(25);
        slWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slWidthStateChanged(evt);
            }
        });

        lblWidthValue.setText("0");
        lblWidthValue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblHeight.setText("Magasság:");

        slHeight.setMinimum(10);
        slHeight.setPaintLabels(true);
        slHeight.setPaintTicks(true);
        slHeight.setValue(25);
        slHeight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slHeightStateChanged(evt);
            }
        });

        lblHeightValue.setText("0");
        lblHeightValue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Mégse");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblServerName1.setText("Jelszó:");

        pfJoinPassword.setToolTipText("Csatlakozási jelszó a partner részére. Megadása nem kötelező.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblServerName)
                            .addComponent(lblWidth)
                            .addComponent(lblHeight)
                            .addComponent(lblServerName1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pfJoinPassword)
                            .addComponent(tfServerName)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(slHeight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                    .addComponent(slWidth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblWidthValue, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblHeightValue, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblServerName)
                    .addComponent(tfServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblWidthValue)
                        .addComponent(slWidth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblWidth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(slHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHeightValue)
                    .addComponent(lblHeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblServerName1)
                    .addComponent(pfJoinPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void slWidthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slWidthStateChanged
        lblWidthValue.setText(slWidth.getValue() + "");
    }//GEN-LAST:event_slWidthStateChanged

    private void slHeightStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slHeightStateChanged
        lblHeightValue.setText(slHeight.getValue() + "");
    }//GEN-LAST:event_slHeightStateChanged

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        createServer();
    }//GEN-LAST:event_btnOKActionPerformed

    private void createServer(){
        serverName = tfServerName.getText();
        width = slWidth.getValue();
        height = slHeight.getValue();
        joinPassword = new String(pfJoinPassword.getPassword());
        
        retval = RESULT_OK;
        setVisible(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblHeightValue;
    private javax.swing.JLabel lblServerName;
    private javax.swing.JLabel lblServerName1;
    private javax.swing.JLabel lblWidth;
    private javax.swing.JLabel lblWidthValue;
    private javax.swing.JPasswordField pfJoinPassword;
    private javax.swing.JSlider slHeight;
    private javax.swing.JSlider slWidth;
    private javax.swing.JTextField tfServerName;
    // End of variables declaration//GEN-END:variables

}
