package amoba;

import static amoba.ResultDialog.RESULT_OK;
import com.google.gson.internal.StringMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.DefaultListModel;
import javax.swing.Timer;

public class PlayedGamesList extends ResultDialog {
    private Menu menu;
    
    private String serverName, host;
    private int port;
    private Timer timer;
    
    private ArrayList<StringMap> list;
    private StringMap selected;
    
    public PlayedGamesList(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        menu = (Menu)parent;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this);

        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.updatePlayedGameList();
            }
        });

        timer.start();
    }
    
    public void updateList(ArrayList<StringMap> list){
        this.list = list;
        
        //dlmGames.clear();
        DefaultListModel<String> dlmGames = new DefaultListModel<String>();
        
        for(StringMap map : this.list){
            if(map.containsKey("header")){
                StringMap header = (StringMap)map.get("header");
                if(header.containsKey("timestamp") && header.containsKey("width") && header.containsKey("height")){
                    String row = getDateTime(Long.parseLong(header.get("timestamp").toString())) + " - " + Integer.parseInt(header.get("width").toString()) + "x" + Integer.parseInt(header.get("height").toString()) + " mező";
                    dlmGames.addElement(row.toString());
                }
            }
        }
        
        lstPlayedGames.setModel(dlmGames);
    }
    
    public static String getDateTime(long timestamp){
        Date d = new Date(timestamp);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(d);
        
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        
        return calendar.get(Calendar.YEAR) + "." + 
            (month < 10 ? "0" + month : month) + "." + 
            (day < 10 ? "0" + day : day) + ". " + 
            (hour < 10 ? "0" + hour : hour) + ":" +
            (minute < 10 ? "0" + minute : minute) + ":" + 
            (second < 10 ? "0" + second : second);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnView = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstPlayedGames = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lejátszott meccsek listája");
        setResizable(false);

        btnView.setText("Megnéz");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnCancel.setText("Mégse");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lstPlayedGames.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstPlayedGames.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstPlayedGamesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstPlayedGames);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnView)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        viewGame();
    }//GEN-LAST:event_btnViewActionPerformed

    private void lstPlayedGamesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstPlayedGamesMouseClicked
        if(evt.getClickCount() >= 2){
            viewGame();
        }
    }//GEN-LAST:event_lstPlayedGamesMouseClicked

    @Override
    public void dispose() {
        timer.stop();
        super.dispose();
    }

    private void viewGame(){
        if(!lstPlayedGames.isSelectionEmpty()){
            selected = list.get(lstPlayedGames.getSelectedIndex());
            
            retval = RESULT_OK;
            timer.stop();
            setVisible(false);
        }
    }
    
    public StringMap getSelected(){
        return selected;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnView;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lstPlayedGames;
    // End of variables declaration//GEN-END:variables

}
