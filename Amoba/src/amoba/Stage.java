package amoba;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.StringMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Stage extends BaseStage implements Runnable, ActionListener{

    private boolean ownGame = false;
    private boolean partnerJoined = false;
    private boolean ignoreClose = false;
    
    private final Socket gsock;
    private final BufferedReader gin;
    private final PrintWriter gout;
    
    private Thread gThread;
    private Chat chat;
    
    private JLabel lblYourSignal, lblSignal, lblNextSignal, lblNext, lblWFP;
    
    public Stage(Frame parent, boolean modal, String host, int port) throws IOException {
        super(parent, modal);
        
        gsock = new Socket(host,port);
        gin = new BufferedReader(new InputStreamReader(gsock.getInputStream()));
        gout = new PrintWriter(gsock.getOutputStream(),true);
        
        lblWFP = new JLabel("<html>Várakozás a<br>partnerre...</html>");
        lblWFP.setFont(defaultFont);
        lblWFP.setForeground(Color.RED);
        lblWFP.setVisible(false);
        lblWFP.setBounds(MARGIN, MARGIN * 4 + TEXT_HEIGHT, 
                          LEFT_TEXT_WIDTH, TEXT_HEIGHT * 3);
        
        final JsonObject msg = new JsonObject();
        msg.addProperty("clientID", Settings.getInstance().getClientID());
        msg.addProperty("nickName", Settings.getInstance().getNickName());
        gout.println(msg.toString());
        
        gThread = new Thread(this);
        gThread.start();
    
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Stage.this.close();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateChatLocation();
            }
        });
    }

    @Override
    protected void initSquares(int countX, int countY, String name) {
        super.initSquares(countX, countY, name);
        
        for(int x = 0; x < squares.length; x++){
            for(int y = 0; y < squares[x].length; y++){
                squares[x][y].addActionListener(this);
            }
        }           
    }
    
    protected void initStage(int countX, int countY, String signal, String name) {
        super.initStage(countX, countY, name);
        
        lblYourSignal = new JLabel("Jeled: ");
        lblYourSignal.setFont(defaultFont);
        lblYourSignal.setBounds(MARGIN, MARGIN, LEFT_TEXT_WIDTH, TEXT_HEIGHT);
        
        lblSignal = new JLabel(signal);
        lblSignal.setFont(defaultFont);
        lblSignal.setBounds(MARGIN + LEFT_TEXT_WIDTH, MARGIN, 
                            LEFT_TEXT_WIDTH, TEXT_HEIGHT);
        
        lblNext = new JLabel("Következik: ");
        lblNext.setFont(defaultFont);
        lblNext.setBounds(MARGIN, MARGIN * 2 + TEXT_HEIGHT, 
                          LEFT_TEXT_WIDTH, TEXT_HEIGHT);
        
        lblNextSignal = new JLabel();
        lblNextSignal.setFont(defaultFont);
        lblNextSignal.setBounds(MARGIN + LEFT_TEXT_WIDTH, MARGIN * 2 + TEXT_HEIGHT, 
                          LEFT_TEXT_WIDTH, TEXT_HEIGHT);
        
        statePanel.add(lblYourSignal);
        statePanel.add(lblSignal);
        statePanel.add(lblNext);
        statePanel.add(lblNextSignal);
        statePanel.add(lblWFP);
        
        setTurn(!ownGame);
    }
    
    public void sendChatMessage(String message){
        final JsonObject msg = new JsonObject();
        msg.addProperty("message",message);
        gout.println(msg.toString());
    }
    
    public Socket getSocket(){
        return gsock;
    }
    
    public PrintWriter getWriter(){
        return gout;
    }
    
    public void setOwnGame(String password) throws IOException{
        final JsonObject msg = new JsonObject();
        msg.addProperty("password",password);
        gout.println(msg.toString());
    }
    
    public boolean isOwnGame(){
        return ownGame;
    }
    
    @Override
    public void dispose() {
        this.close();
        super.dispose();
    }
    
    public void close(){
        if(!ignoreClose){
            if(Amoba.DEBUG){
                System.out.println("Stage closing...");
            }
            
            final JsonObject msg = new JsonObject();
            msg.addProperty("closeServer", "1");
            gout.println(msg.toString());
            
            if(chat != null){
                chat.dispose();
                chat = null;
            }
            
            ignoreClose = true;
            
            menu.setNullStage();
        }
    }
    
    private void updateChatLocation() {
        if(chat != null){
            chat.setLocation(getLocation().x + getWidth() + 10,getLocation().y);
        }
    }
    
    public void setTurn(boolean yourturn){
        lblNextSignal.setText((yourturn && ownGame || !yourturn && !ownGame) ? O : X);
    }

    @Override
    public void run() {
        while(true){
            try {
                String line = gin.readLine();
                if(Amoba.DEBUG){
                    System.out.println("Stage line: " + line);
                }
                
                Gson ggson = new Gson();
                Map<String,Object> json = new HashMap<String,Object>();
                json = (Map<String,Object>)ggson.fromJson(line, json.getClass());
                
                if(json == null) continue;
                
                if(json.containsKey("accept") && json.get("accept").toString().equals("1") && 
                    json.containsKey("width") && json.containsKey("height"))
                {
                    if(initializedStage) continue;
                    
                    if(json.containsKey("owner") && json.get("owner").toString().equals("1")){
                        this.ownGame = true;
                        
                        lblWFP.setVisible(true);
                    }
                    
                    initStage(
                       Integer.parseInt(json.get("width").toString()), 
                       Integer.parseInt(json.get("height").toString()),
                       json.get("signal").toString(),
                       json.get("name").toString()
                    );
                    
                    chat = new Chat(this, false);
                    chat.setTitle("Amőba :: " + json.get("name").toString() + " :: Chat");
                    chat.setVisible(true);
                    
                    Dimension ss = getToolkit().getScreenSize();
                    
                    setLocation(ss.width / 2 - (this.getWidth()+chat.getWidth()) / 2,ss.height / 2 - this.getHeight() / 2);
                    updateChatLocation();
                    toFront();
                 }
                
                else if(json.containsKey("step") && json.containsKey("coordx") && json.containsKey("coordy")){
                    int cx = Integer.parseInt(json.get("coordx").toString());
                    int cy = Integer.parseInt(json.get("coordy").toString());
                    squares[cx][cy].setText(json.get("step").toString());
                    squares[lastPoint.x][lastPoint.y].setForeground(Color.BLACK);
                    squares[cx][cy].setForeground(Color.RED);
                    squares[cx][cy].getModel().setSelected(true);
                    lastPoint.x = cx;
                    lastPoint.y = cy;
                }
                
                else if(json.containsKey("winner") && json.containsKey("you") && json.containsKey("coords")){
                    for(StringMap map : (ArrayList<StringMap>)json.get("coords")){
                        if(map.containsKey("x") && map.containsKey("y")){
                            int wx = Integer.parseInt(map.get("x").toString());
                            int wy = Integer.parseInt(map.get("y").toString());
                            squares[wx][wy].setForeground(Color.RED);
                        }
                    }
                    
                    if(json.get("winner").equals(json.get("you"))){
                        JOptionPane.showMessageDialog(this, "Gratulálok nyertél! :)","Amőba",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Már megint vesztettél! :(","Amőba",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                
                else if(json.containsKey("draw")){
                    JOptionPane.showMessageDialog(this, "Elfogytak a mezők! Döntetlen :|","Amőba",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                else if(json.containsKey("yourturn")){
                    setTurn(json.get("yourturn").equals("1"));
                }
                
                else if(!ignoreClose && partnerJoined && json.containsKey("serverClosed") && json.get("serverClosed").equals("1")){
                    JOptionPane.showMessageDialog(this, "A másik játékos kilépett a játékból. :/", "Amőba", JOptionPane.INFORMATION_MESSAGE);
                    close();
                    dispose();
                }
                
                else if(json.containsKey("message") && json.containsKey("name")){
                    chat.addMessage(json.get("message").toString(), json.get("name").toString());
                }
                
                else if(json.containsKey("requireJoinPassword") && json.containsKey("serverName")){
                    PasswordDialog pd = new PasswordDialog(menu, true);
                    pd.setTitle(json.get("serverName").toString() + " :: csatlakozási jelszó");
                    if(pd.Show() == PasswordDialog.RESULT_OK){
                        final JsonObject msg = new JsonObject();
                        msg.addProperty("joinPassword", pd.getPassword());
                        gout.println(msg.toString());
                        pd.dispose();
                        pd = null;
                    }
                    else{
                        pd.dispose();
                        pd = null;
                        gsock.close();
                        dispose();
                        return;
                    }
                }
                
                else if(json.containsKey("accept") && json.get("accept").equals("0") &&
                        json.containsKey("partner") && json.get("partner").equals("0") &&
                        json.containsKey("reason"))
                {
                    JOptionPane.showMessageDialog(this, "Hibás jelszót adtál meg!","Amőba",JOptionPane.WARNING_MESSAGE);
                    gsock.close();
                    dispose();
                    return;
                }
                
                else if(json.containsKey("partnerJoined") && json.get("partnerJoined").equals("1")){
                    partnerJoined = true;
                    lblWFP.setVisible(false);
                }
            } catch (Exception e) {
                if(Amoba.DEBUG){
                    e.printStackTrace();
                }
            }
            
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof Square){
            Square btn = (Square)e.getSource();
            if(btn.getText().equals("")){
                btn.getModel().setSelected(false);
                
                final JsonObject msg = new JsonObject();
                msg.addProperty("step", "1");
                msg.addProperty("coordx", ((int)btn.getCoord().x) + "");
                msg.addProperty("coordy", ((int)btn.getCoord().y) + "");
                
                gout.println(msg.toString());
            }
            else{
                btn.getModel().setSelected(true);
            }
        }
    }
}
