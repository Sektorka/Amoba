package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientGameWorker extends Thread{
    private Socket sock;
    private GameServer gserver;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String ID = "", nickName = "";
    
    public ClientGameWorker(Socket sock, GameServer gserver) throws IOException{
        this.sock = sock;
        this.gserver = gserver;
        
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintWriter(sock.getOutputStream(),true);
    }
    
    public void send(String msg){
        if(out != null){
            try{
                if(Server.DEBUG){
                    System.out.println("Sent: " + msg);
                }
                
                out.println(msg);
            }
            catch(Exception e){
                if(Server.DEBUG){
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void run() {
        try{
            if(gserver.getOwner() != null && gserver.getPartner() != null && gserver.getJoinPassword().equals("")){
                JsonObject msg = new JsonObject();
                msg.addProperty("accept","1");
                msg.addProperty("signal", GameServer.X);
                msg.addProperty("name", gserver.getServerName());
                msg.addProperty("width",gserver.getStageWidth() + "");
                msg.addProperty("height",gserver.getStageWidth() + "");
                send(msg.toString());
                
                msg = new JsonObject();
                msg.addProperty("partnerJoined","1");
                
                gserver.getOwner().send(msg.toString());
            }
            else if(gserver.getOwner() != null && gserver.getPartner() == null){
                final JsonObject msg = new JsonObject();
                msg.addProperty("serverName", gserver.getServerName());
                msg.addProperty("requireJoinPassword","1");
                send(msg.toString());
            }
            
            while(true){
                String line = in.readLine();
                
                if(line == null) return;
                
                if(Server.DEBUG){
                    System.out.println("ClientGameWorkerline: " + line);
                }
                
                Gson gson = new Gson();
                Map<String,String> json = new HashMap<String,String>();
                json = (Map<String,String>)gson.fromJson(line, json.getClass());
                
                if(json.containsKey("clientID") && json.get("clientID").length() == 30){
                    ID = json.get("clientID");
                    
                    if(json.containsKey("nickName") && !json.get("nickName").isEmpty()){
                        nickName = json.get("nickName");
                    }
                    
                    final JsonObject msg = new JsonObject();
                    msg.addProperty("masterserver", "1");
                    out.println(msg.toString());
                    
                    continue;
                }
                else if(json.containsKey("nickName") && !json.get("nickName").isEmpty()){
                    nickName = json.get("nickName");
                }
                
                //set server owner
                if(json.containsKey("password")){
                    
                    if(gserver.getOwner() == null){
                        
                        if(json.get("password").equals(gserver.getPassword())){
                            gserver.setOwner(this);
                            
                            final JsonObject msg = new JsonObject();
                            msg.addProperty("accept","1");
                            msg.addProperty("owner","1");
                            msg.addProperty("signal", GameServer.O);
                            msg.addProperty("name", gserver.getServerName());
                            msg.addProperty("width",gserver.getStageWidth() + "");
                            msg.addProperty("height",gserver.getStageHeight() + "");
                            send(msg.toString());
                        }
                        else{
                            final JsonObject msg = new JsonObject();
                            msg.addProperty("accept","0");
                            msg.addProperty("owner", "0");
                            msg.addProperty("reason", "Hibás jelszó!");

                            send(msg.toString());
                        }
                    }
                    else{
                        final JsonObject msg = new JsonObject();
                        msg.addProperty("accept","0");
                        msg.addProperty("owner", "0");
                        msg.addProperty("reason", "E játéknak megvan a tulajdonosa!");
                        
                        send(msg.toString());
                    }
                }
                
                else if(json.containsKey("joinPassword") && !gserver.getJoinPassword().equals("")){
                    
                    if(gserver.getPartner() == null){
                        
                        if(json.get("joinPassword").equals(gserver.getJoinPassword())){
                            gserver.setPartner(this);
                            
                            JsonObject msg = new JsonObject();
                            msg.addProperty("accept","1");
                            msg.addProperty("signal", GameServer.X);
                            msg.addProperty("name", gserver.getServerName());
                            msg.addProperty("width",gserver.getStageWidth() + "");
                            msg.addProperty("height",gserver.getStageWidth() + "");
                            send(msg.toString());
                            
                            msg = new JsonObject();
                            msg.addProperty("partnerJoined","1");

                            gserver.getOwner().send(msg.toString());
                        }
                        else{
                            final JsonObject msg = new JsonObject();
                            msg.addProperty("accept","0");
                            msg.addProperty("partner", "0");
                            msg.addProperty("reason", "Hibás jelszó!");
                            send(msg.toString());
                        }
                    }
                    else{
                        final JsonObject msg = new JsonObject();
                        msg.addProperty("accept","0");
                        msg.addProperty("partner", "0");
                        msg.addProperty("reason", "Partner játékos már csatlakozott ehhez a játékhoz!");
                        
                        send(msg.toString());
                    }
                }
                
                else if(json.containsKey("closeServer")){
                    final JsonObject msg = new JsonObject();
                    msg.addProperty("serverClosed", "1");
                    
                    try{
                        gserver.getOwner().send(msg.toString());
                    }
                    catch(Exception e){
                        if(Server.DEBUG){
                            e.printStackTrace();
                        }
                    }
                    
                    try {
                        if(gserver.getPartner() != null){
                            gserver.getPartner().send(msg.toString());
                        }
                    } catch (Exception e) {
                        if(Server.DEBUG){
                            e.printStackTrace();
                        }
                    }
                    
                    gserver.close();
                }
                
                //game thinks
                else if(gserver.getOwner() != null && gserver.getPartner() != null){
                    if(json.containsKey("message")){
                        JsonObject msg;
                        
                        msg = new JsonObject();
                        msg.addProperty("message", json.get("message"));
                        msg.addProperty("name", nickName);
                        gserver.getOwner().send(msg.toString());
                        
                        msg = new JsonObject();
                        msg.addProperty("message", json.get("message"));
                        msg.addProperty("name", nickName);
                        gserver.getPartner().send(msg.toString());
                        
                    }
                    else if(gserver.getCurrentSignal().equals(getSignal())){
                        if(json.containsKey("step") && json.containsKey("coordx") && json.containsKey("coordy")){
                            int cx = Integer.parseInt(json.get("coordx"));
                            int cy = Integer.parseInt(json.get("coordy"));
                            JsonObject msg;
                            
                            if(!gserver.isDistrainedSquare(cx, cy)){
                                gserver.distrainSquare(cx, cy);
                                
                                msg = new JsonObject();
                                msg.addProperty("step", gserver.getCurrentSignal());
                                msg.addProperty("coordx", cx + "");
                                msg.addProperty("coordy", cy + "");
                                
                                String[] step = {
                                    gserver.getCurrentSignal(),
                                    cx + "",
                                    cy + ""
                                };
                                
                                gserver.saveStep(step);

                                gserver.getOwner().send(msg.toString());
                                gserver.getPartner().send(msg.toString());

                                gserver.checkWinner(new Point(cx, cy));
                                
                                gserver.incrementStepCounter();
                                
                                if(gserver.getCurrentSignal().equals(gserver.getOwner().getSignal())){
                                    msg = new JsonObject();
                                    msg.addProperty("yourturn", "1");
                                    gserver.getOwner().send(msg.toString());
                                    
                                    msg = new JsonObject();
                                    msg.addProperty("yourturn", "0");
                                    gserver.getPartner().send(msg.toString());
                                }
                                else if(gserver.getCurrentSignal().equals(gserver.getPartner().getSignal())){
                                    msg = new JsonObject();
                                    msg.addProperty("yourturn", "0");
                                    gserver.getOwner().send(msg.toString());
                                    
                                    msg = new JsonObject();
                                    msg.addProperty("yourturn", "1");
                                    gserver.getPartner().send(msg.toString());
                                }
                            }
                        }
                    }
                }
                //else
            }
        }
        catch(Exception e){
            if(Server.DEBUG){
                e.printStackTrace();
            }
        }
    }
    
    public String getSignal(){
        return (this.equals(gserver.getOwner()) ? GameServer.O : GameServer.X);
    }
    
    public void close(){
        try {
            sock.close();
        } catch (Exception e) {
            if(Server.DEBUG){
                e.printStackTrace();
            }
        }
    }
}
