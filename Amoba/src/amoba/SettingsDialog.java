package amoba;

import com.google.gson.JsonObject;
import java.io.IOException;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;
import javax.xml.parsers.ParserConfigurationException;

public class SettingsDialog extends ResultDialog {

    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        setLocationRelativeTo(this);
        
        JFormattedTextField txt = ((JSpinner.NumberEditor) spinPort.getEditor()).getTextField();
        ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
        txt.setHorizontalAlignment(JTextField.LEFT);
        
        tfHost.setText(Settings.getInstance().getMasterServerHost());
        spinPort.setValue(Settings.getInstance().getMasterServerPort());
        tfClientID.setText(Settings.getInstance().getClientID());
        tfNickName.setText(Settings.getInstance().getNickName());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        tpServerConfig = new javax.swing.JPanel();
        lblHost = new javax.swing.JLabel();
        lblPort = new javax.swing.JLabel();
        tfHost = new javax.swing.JTextField();
        spinPort = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        lblClientID = new javax.swing.JLabel();
        tfClientID = new javax.swing.JTextField();
        lblNickName = new javax.swing.JLabel();
        tfNickName = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Beállítások");

        tpServerConfig.setBackground(new java.awt.Color(255, 255, 255));

        lblHost.setText("Kiszolgáló IP címe:");

        lblPort.setText("Port:");

        tfHost.setText("localhost");

        spinPort.setModel(new javax.swing.SpinnerNumberModel(10000, 1, 65535, 1));
        spinPort.setEditor(new javax.swing.JSpinner.NumberEditor(spinPort, ""));

        javax.swing.GroupLayout tpServerConfigLayout = new javax.swing.GroupLayout(tpServerConfig);
        tpServerConfig.setLayout(tpServerConfigLayout);
        tpServerConfigLayout.setHorizontalGroup(
            tpServerConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tpServerConfigLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tpServerConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHost)
                    .addComponent(lblPort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tpServerConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfHost, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(spinPort))
                .addContainerGap())
        );
        tpServerConfigLayout.setVerticalGroup(
            tpServerConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tpServerConfigLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tpServerConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHost)
                    .addComponent(tfHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tpServerConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPort)
                    .addComponent(spinPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Mester szerver", tpServerConfig);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblClientID.setText("Kliens azonosító:");

        tfClientID.setEditable(false);
        tfClientID.setBackground(new java.awt.Color(250, 250, 250));

        lblNickName.setText("Becenév:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblClientID)
                    .addComponent(lblNickName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNickName)
                    .addComponent(tfClientID, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNickName)
                    .addComponent(tfNickName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblClientID)
                    .addComponent(tfClientID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Kliens", jPanel1);

        btnCancel.setText("Mégse");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setText("Mentés");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try{
            retval = RESULT_OK;
            saveSettings();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void saveSettings() throws ParserConfigurationException, 
            IllegalArgumentException, IllegalAccessException, IOException 
    {
        Settings.getInstance().setMasterServerHost(tfHost.getText());
        Settings.getInstance().setMasterServerPort(Integer.parseInt(spinPort.getValue().toString()));
        Settings.getInstance().setNickName(tfNickName.getText());
        
        Settings.getInstance().saveSettings();
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblClientID;
    private javax.swing.JLabel lblHost;
    private javax.swing.JLabel lblNickName;
    private javax.swing.JLabel lblPort;
    private javax.swing.JSpinner spinPort;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField tfClientID;
    private javax.swing.JTextField tfHost;
    private javax.swing.JTextField tfNickName;
    private javax.swing.JPanel tpServerConfig;
    // End of variables declaration//GEN-END:variables
}
