package amoba;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.StringMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Menu extends javax.swing.JFrame implements Runnable{
    private Socket sock;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Thread thread = null;
    private GameServerList gslForm = null;
    private PlayedGamesList pglForm = null;
    private Stage stage = null;
    private ReplayStage rstage = null;
    private Timer timer;
    
    public Menu() {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(this);
        
        timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToMasterServer(false);
            }
        });
        
        //connect to master server
        connectToMasterServer();
    }

    public PrintWriter getWriter(){
        return out;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnNewGame = new javax.swing.JButton();
        btnJoinGame = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnAbout = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        btnPlayedGames = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Amőba");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("Amőba");

        btnNewGame.setText("Új hálózati játszma");
        btnNewGame.setEnabled(false);
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        btnJoinGame.setText("Jelenlegi játszmák");
        btnJoinGame.setEnabled(false);
        btnJoinGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinGameActionPerformed(evt);
            }
        });

        btnExit.setText("Kilépés");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnAbout.setText("Névjegy");
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });

        btnSettings.setText("Beállítások");
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });

        btnPlayedGames.setText("Lejátszott játszmák");
        btnPlayedGames.setEnabled(false);
        btnPlayedGames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayedGamesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNewGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnJoinGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAbout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPlayedGames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNewGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnJoinGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPlayedGames)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSettings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        NewGameServer ngs = new NewGameServer(this,true);
        if(ngs.Show() == NewGameServer.RESULT_OK){
            final JsonObject msg = new JsonObject();
            msg.addProperty("newServer", ngs.getServerName());
            msg.addProperty("joinPassword", ngs.getJoinPassword());
            msg.addProperty("width", ngs.getStageWidth() + "");
            msg.addProperty("height", ngs.getStageHeight() + "");
            
            out.println(msg.toString());
        }
        
        ngs.dispose();
        ngs = null;
    }//GEN-LAST:event_btnNewGameActionPerformed

    private void btnJoinGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinGameActionPerformed
        try{
            gslForm = new GameServerList(this,true);
            
            updateServerList();
            
            if(gslForm.Show() == ResultDialog.RESULT_OK){
                stage = new Stage(
                        this,false,
                        gslForm.getHost(),
                        gslForm.getPort()
                );
            }
            
            gslForm.dispose();
            gslForm = null;
        }
        catch(Exception e){
            if(Amoba.DEBUG){
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnJoinGameActionPerformed

    public void updateServerList(){
        final JsonObject msg = new JsonObject();
        msg.addProperty("listGameServers", "1");
        out.println(msg.toString());
    }
    
    public void updatePlayedGameList(){
        final JsonObject msg = new JsonObject();
        msg.addProperty("listPlayedGames", "1");
        out.println(msg.toString());
    }
    
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(stage != null){
            stage.close();
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        new About(this, true).setVisible(true);
    }//GEN-LAST:event_btnAboutActionPerformed

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        SettingsDialog sd = new SettingsDialog(this, true);
        if(sd.Show() == SettingsDialog.RESULT_OK){
            connectToMasterServer();
            
            final JsonObject msg = new JsonObject();
            msg.addProperty("nickName", Settings.getInstance().getNickName());
            out.println(msg.toString());
            if(stage != null){
                stage.getWriter().println(msg.toString());
            }
        }
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void btnPlayedGamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayedGamesActionPerformed
        pglForm = new PlayedGamesList(this, true);
        
        updatePlayedGameList();
        
        if(pglForm.Show() == ResultDialog.RESULT_OK){
            StringMap map = pglForm.getSelected();
            if(map.containsKey("header") && map.containsKey("body")){
                StringMap header = (StringMap)map.get("header");
                if(header.containsKey("timestamp") && header.containsKey("width") && header.containsKey("height")){
                    rstage = new ReplayStage(this,false,
                            PlayedGamesList.getDateTime(Long.parseLong(header.get("timestamp").toString())),
                            Integer.parseInt(header.get("width").toString()),
                            Integer.parseInt(header.get("height").toString())
                    );
                    
                    rstage.setSteps((ArrayList<StringMap>) map.get("body"));
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Hiba lépett fel az adatok feldolgozása közben!\nIndok: Hibás adatok érkeztek a szerver felől!","Játék visszanéző menedzser",JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        pglForm.dispose();
        pglForm = null;
    }//GEN-LAST:event_btnPlayedGamesActionPerformed
    
    public void enableComponents(boolean enable){
        btnNewGame.setEnabled(enable);
        btnJoinGame.setEnabled(enable);
        btnPlayedGames.setEnabled(enable);
        
        btnNewGame.requestFocus();
        
        if(stage != null){
            btnSettings.setEnabled(enable);
        }
    }
    
    public void setNullStage(){
        stage = null;
    }
    
    private void connectToMasterServer(){
        connectToMasterServer(true);
    }
    
    private void connectToMasterServer(boolean showError){
        if(Amoba.DEBUG){
            System.out.println("Kapcsolódás a mester szerverhez...");
        }
        
        try{
            if(Amoba.DEBUG){
                System.out.println(Settings.getInstance().getMasterServerHost() + ":" + Settings.getInstance().getMasterServerPort());
            }
            sock = new Socket(Settings.getInstance().getMasterServerHost(),Settings.getInstance().getMasterServerPort());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(),true);
            
            final JsonObject msg = new JsonObject();
            msg.addProperty("clientID", Settings.getInstance().getClientID());
            msg.addProperty("nickName", Settings.getInstance().getNickName());
            out.println(msg.toString());
            
            thread = new Thread(this);
            thread.start();
            timer.stop();
        }
        catch(Exception e){
            if(showError){
                JOptionPane.showMessageDialog(this, "Sikertelen kapcsolódás a mester szerverhez!\n" + e.getMessage(),"Amőba",JOptionPane.ERROR_MESSAGE);
            }
            
            if(Amoba.DEBUG){
                e.printStackTrace();
            }
            
            enableComponents(false);
            
            timer.stop();
            timer.start();
            //System.exit(-1);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnJoinGame;
    private javax.swing.JButton btnNewGame;
    private javax.swing.JButton btnPlayedGames;
    private javax.swing.JButton btnSettings;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while(true){
            try {
                String line = in.readLine();
                
                if(Amoba.DEBUG){
                    System.out.println(line);
                }
                
                Gson gson = new Gson();
                Map<String,Object> json = new HashMap<String,Object>();
                json = (Map<String,Object>)gson.fromJson(line, json.getClass());
                
                if(json.containsKey("masterserver") && json.get("masterserver").equals("1")){
                    enableComponents(true);
                }

                else if(json.containsKey("newServer"))
                {
                    if(json.get("newServer").toString().equals("1") && 
                       json.containsKey("port") && json.containsKey("password")){
                        try{
                            if(Amoba.DEBUG){
                                System.out.println("Connecting to: " + Settings.getInstance().getMasterServerHost() + ":" + Integer.parseInt(json.get("port").toString()));
                            }
                            
                            stage = new Stage(
                                this, false, 
                                Settings.getInstance().getMasterServerHost(),
                                Integer.parseInt(json.get("port").toString())
                            );
                            
                            stage.setOwnGame(json.get("password").toString());
                        }
                        catch(Exception e){
                            JOptionPane.showMessageDialog(this, "Sikertelen kapcsolódás a játék szerverhez!\n" + e.getMessage(),"Amőba",JOptionPane.ERROR_MESSAGE);
                            if(Amoba.DEBUG){
                                e.printStackTrace();
                            }
                            //System.exit(-1);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Nem sikerült létrehozni a játékszervert!" + (json.containsKey("reason") ? "\n" + json.get("reason").toString() : ""),"Amőba",JOptionPane.ERROR_MESSAGE);
                    }  
                }
                else if(json.containsKey("gameServers") && gslForm != null){
                    gslForm.removeRows();
                    
                    for(StringMap map : (ArrayList<StringMap>)json.get("gameServers")){
                        if(map.containsKey("name") && /*map.containsKey("host") && */map.containsKey("port")){
                            if(stage == null || 
                               (stage != null && 
                                   !stage.getSocket().getInetAddress().getHostAddress().equals(map.get("host").toString()) &&
                                    stage.getSocket().getPort() != Integer.parseInt(map.get("port").toString())
                               )
                            )
                            {
                                gslForm.addRow(map.get("name").toString(), 
                                        Settings.getInstance().getMasterServerHost(), 
                                        map.get("port").toString(),
                                        (map.get("password").equals("1") ? "igen" : "nem")
                                );
                            }
                        }
                    }
                }
                else if(json.containsKey("playedGames") && pglForm != null){
                    pglForm.updateList((ArrayList<StringMap>)json.get("playedGames"));
                }
            } catch (Exception e) {
                enableComponents(false);
                if(Amoba.DEBUG){
                    e.printStackTrace();
                }
            }
            
        }
    }
}
