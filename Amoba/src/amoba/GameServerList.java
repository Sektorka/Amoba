package amoba;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class GameServerList extends ResultDialog {
    private Menu menu;
    private DefaultTableModel dtmServers;
    
    private String serverName, host;
    private int port;
    private Timer timer;
    
    private static final int COL_SERVERNAME = 0;
    private static final int COL_HOST = 1;
    private static final int COL_PORT = 2;
    private static final int COL_PASS = 3;
    
    public GameServerList(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        menu = (Menu)parent;
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this);
        
        String[] columnNames = {"Szervernév","Kiszolgáló","Port","Jelszó"};
        dtmServers = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.updateServerList();
            }
        });
        
        timer.start();
        
        tableServers.setModel(dtmServers);
    }

    public void addRow(String name, String host, String port, String password){
        String[] row = {name, host, port, password};
        dtmServers.addRow(row);
    }
    
    public void removeRows(){
        for(int i = dtmServers.getRowCount() - 1; i >= 0 ; i--){
            dtmServers.removeRow(i);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableServers = new javax.swing.JTable();
        btnJoin = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Szabad meccsek listája");
        setPreferredSize(new java.awt.Dimension(400, 331));
        setResizable(false);

        tableServers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableServers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableServers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableServersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableServers);

        btnJoin.setText("Kapcsolódik");
        btnJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinActionPerformed(evt);
            }
        });

        btnCancel.setText("Mégse");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnJoin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJoin)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinActionPerformed
        joinGame();
    }//GEN-LAST:event_btnJoinActionPerformed

    private void tableServersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableServersMouseClicked
        if(evt.getClickCount() >= 2){
            joinGame();
        }
    }//GEN-LAST:event_tableServersMouseClicked

    @Override
    public void dispose() {
        timer.stop();
        super.dispose();
    }

    private void joinGame(){
        System.out.println("JOINING...");
        if(tableServers.getSelectedRowCount() == 1){
            serverName = dtmServers.getValueAt(tableServers.getSelectedRow(), COL_SERVERNAME).toString();
            host = dtmServers.getValueAt(tableServers.getSelectedRow(), COL_HOST).toString();
            port = Integer.parseInt(dtmServers.getValueAt(tableServers.getSelectedRow(), COL_PORT).toString());

            retval = RESULT_OK;
            timer.stop();
            setVisible(false);
        }
    }

    public String getServerName() {
        return serverName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnJoin;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableServers;
    // End of variables declaration//GEN-END:variables

}
