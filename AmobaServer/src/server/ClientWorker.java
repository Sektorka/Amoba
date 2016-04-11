package server;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientWorker extends Thread{
    private Socket sock;
    private Server server;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String ID = "", nickName = "";
    
    public ClientWorker(Socket sock, Server server) throws IOException{
        this.sock = sock;
        this.server = server;
        
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintWriter(sock.getOutputStream(),true);
    }
    
    @Override
    public void run(){
        try{
            while(true){
                String line = in.readLine();
                
                if(Server.DEBUG){
                    System.out.println("ClientWorkerline: " + line);
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
                
                if(ID.isEmpty() || ID.length() != 30){
                    final JsonObject msg = new JsonObject();
                    msg.addProperty("require", "ClientID");
                    
                    out.println(msg.toString());
                    continue;
                }
                
                if(json.containsKey("newServer") && json.containsKey("joinPassword") && 
                        json.containsKey("width") && json.containsKey("height")){
                    try{
                        int width = Integer.parseInt(json.get("width"));
                        int height = Integer.parseInt(json.get("height"));
                        
                        if(width < 10 || width > 100 || height < 10 || height > 100){
                            final JsonObject msg = new JsonObject();
                            msg.addProperty("newServer", "0");
                            msg.addProperty("reason", "A szélesség és a magasság legalább 10 és legfeljebb 100 egység lehet!");

                            out.println(msg.toString());
                        }
                        else{
                        
                            GameServer gs = server.createGameServer(json.get("newServer"),
                                    json.get("joinPassword"), width, height);
                            if(gs != null){
                                final JsonObject msg = new JsonObject();
                                msg.addProperty("newServer", "1");
                                //msg.addProperty("host", InetAddress.getLocalHost().getHostAddress());
                                msg.addProperty("port", gs.getPort() + "");
                                msg.addProperty("password", gs.getPassword());

                                out.println(msg.toString());
                            }
                            else{
                                final JsonObject msg = new JsonObject();
                                msg.addProperty("newServer", "0");
                                msg.addProperty("reason", "Szerver létrehozása sikertelen! Nincs szabad port a szerveren!");

                                out.println(msg.toString());
                            }
                        }
                    }
                    catch(Exception e){
                        final JsonObject msg = new JsonObject();
                        msg.addProperty("newServer", "0");
                        msg.addProperty("reason", e.getMessage());

                        out.println(msg.toString());
                        
                        if(Server.DEBUG){
                            e.printStackTrace();
                        }
                    }
                    
                }
                else if(json.containsKey("listGameServers")){
                    JsonArray gservers = new JsonArray();
                    synchronized ( server.getGameServers() ) {
                        for(GameServer gs : server.getGameServers()){
                            if(gs.isListable()){
                                JsonObject tmpObj = new JsonObject();
                                
                                tmpObj.addProperty("name", gs.getServerName());
                                //tmpObj.addProperty("host", InetAddress.getLocalHost().getHostAddress());
                                tmpObj.addProperty("port", gs.getPort() + "");
                                tmpObj.addProperty("password", (gs.getJoinPassword().equals("") ? "0" : "1"));
                                
                                gservers.add(tmpObj);
                            }
                        }
                    }
                    
                    final JsonObject msg = new JsonObject();
                    msg.add("gameServers", gservers);

                    out.println(msg.toString());
                }
                else if(json.containsKey("listPlayedGames")){
                    final int COL_SIGNAL = 0;
                    final int COL_X = 1;
                    final int COL_Y = 2;
                    
                    CSVReader csvr;
                    JsonArray gPlayedGames = new JsonArray();
                    FileFilter csvFilter = new FileFilter() {

                        @Override
                        public boolean accept(File pathname) {
                            return (pathname.getName().substring(pathname.getName().lastIndexOf(".")).equals(".csv"));
                        }
                    };
                    
                    File sessionDir = new File(GameServer.SESSION_DIR);
                    if(sessionDir.exists() && sessionDir.isDirectory()){
                        for(File f : sessionDir.listFiles(csvFilter)){
                            if(f.length() == 0) continue;
                            
                            String timestamp = f.getName().substring(0,f.getName().indexOf("_"));
                            
                            String wh = f.getName().substring(f.getName().indexOf("_")+1);
                            wh = wh.substring(0,wh.indexOf("."));
                            
                            String width = wh.substring(0,wh.lastIndexOf("x"));
                            String height = wh.substring(wh.lastIndexOf("x")+1);
                            
                            JsonObject header = new JsonObject();
                            header.addProperty("timestamp",timestamp);
                            header.addProperty("width",width);
                            header.addProperty("height",height);
                            
                            JsonArray body = new JsonArray();
                            csvr = new CSVReader(new FileReader(f));
                            
                            for(String[] cols : csvr.readAll()){
                                JsonObject row = new JsonObject();
                                for(int i = 0; i < cols.length; i++){
                                    switch(i){
                                        case COL_SIGNAL:
                                            row.addProperty("signal", cols[i]);
                                            break;
                                        case COL_X:
                                            row.addProperty("x", cols[i]);
                                            break;
                                        case COL_Y:
                                            row.addProperty("y", cols[i]);
                                            break;
                                    }
                                }
                                body.add(row);
                            }
                            
                            JsonObject playedGame = new JsonObject();
                            playedGame.add("header", header);
                            playedGame.add("body", body);
                                    
                            gPlayedGames.add(playedGame);
                        }
                    }
                    
                    JsonObject msg = new JsonObject();
                    msg.add("playedGames", gPlayedGames);
                    
                    System.out.println(msg.toString());
                    out.println(msg.toString());
                }
            }
            
            
        }
        catch(Exception e){
            if(Server.DEBUG){
                e.printStackTrace();
            }
        }
    }
    
}
