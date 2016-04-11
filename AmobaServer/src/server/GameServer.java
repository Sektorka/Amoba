package server;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class GameServer extends Thread{
    private ServerSocket listener;
    private ClientGameWorker owner = null;
    private ClientGameWorker partner = null;
    private String password, joinPassword = "";
    private String serverName;
    private int width, height;
    private Server masterServer;
    private int stepCounter = 0;
    public static final String O = "O";
    public static final String X = "X";
    public static final String SESSION_DIR = "session";
    private String[][] squares;
    private Point[] winnerCoords;
    private CSVWriter csvw;
    private String csvFileName = "";
    
    public GameServer(Server masterServer, String joinPassword, int port, String serverName, int width, int height) throws IOException{
        listener = new ServerSocket(port,2);
        password = generatePassword();
        this.joinPassword = joinPassword;
        this.serverName = serverName;
        this.width = width;
        this.height = height;
        this.masterServer = masterServer;
        
        File f = new File(SESSION_DIR);
        if(!f.exists() || !f.isDirectory()){
            f.mkdir();
        }
        
        csvFileName = (new Date()).getTime() + "_" + width + "x" + height + ".csv";
        csvw = new CSVWriter(new FileWriter(SESSION_DIR + "/" + csvFileName));
        
        winnerCoords = new Point[5];
        squares = new String[width][height];
        for(int x = 0; x < squares.length; x++){
            for(int y = 0; y < squares[x].length; y++){
                squares[x][y] = "";
            }
        }
    }
    
    public String getJoinPassword(){
        return joinPassword;
    }
    
    public void saveStep(String[] arrStr){
        csvw.writeNext(arrStr);
        try{
            csvw.flush();
        }
        catch(IOException e){
        }
    }
    
    @Override
    public void run(){
        Socket client;
        while(true){
            try{
                client = listener.accept();
                System.out.println("Új kapcsolat: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
                
                if(client != null){
                    if(owner != null && partner != null){
                        client.close();
                    }
                    else{
                        ClientGameWorker cgw = new ClientGameWorker(client,this);
                        cgw.start();

                        if(owner != null && partner == null && joinPassword.equals("")){
                            if(Server.DEBUG){
                                System.out.println("Partner object setted!");
                            }
                            
                            partner = cgw;
                        }
                    }
                }
            }
            catch(Exception e){
                if(Server.DEBUG){
                    e.printStackTrace();
                }
                
                close();
                
                return;
            }
        }
    }
    
    public boolean isDistrainedSquare(int coordx, int coordy){
        return !squares[coordx][coordy].equals("");
    }
    
    public boolean distrainSquare(int coordx, int coordy){
        if(isDistrainedSquare(coordx,coordy)){
            return false;
        }
        else{
            squares[coordx][coordy] = getCurrentSignal();
            return true;
        }
    }
    
    public int getStageWidth(){
        return width;
    }
    
    public String getCurrentSignal(){
        return (stepCounter % 2 == 0 ? X : O);
    }
    
    public void incrementStepCounter(){
        stepCounter++;
    }
    
    public int getStageHeight(){
        return height;
    }
    
    public InetAddress getInetAddress(){
        return listener.getInetAddress();
    }
    
    public int getPort(){
        return listener.getLocalPort();
    }
    
    public boolean isListable(){
        if(Server.DEBUG){
            System.out.println(owner + " - " + partner);
        }
        
        return (owner != null && partner == null);
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setOwner(ClientGameWorker owner){
        this.owner = owner;
    }
    
    public void setPartner(ClientGameWorker partner){
        this.partner = partner;
    }
    
    public ClientGameWorker getOwner(){
        return owner;
    }
    
    public ClientGameWorker getPartner(){
        return partner;
    }
    
    private String generatePassword(){
        return new PasswordGenerator(true, true, true, false, 20).GeneratePassword();
    }
    
    public String getServerName(){
        return serverName;
    }
    
    public void close(){
        try {
            csvw.close();
            
            File tmpCsv = new File(SESSION_DIR + "/" + csvFileName);
            
            if(tmpCsv.exists() && tmpCsv.length() == 0){
                tmpCsv.delete();
            }
            
            listener.close();
        } catch (Exception e) {
            if(Server.DEBUG){
                e.printStackTrace();
            }
        }
        
        masterServer.getGameServers().remove(this);
    }
    
    public void checkWinner(Point coord){
        boolean hasWinner = false;
        ArrayList<Point> invCoords = this.involvedCoords(coord);
        
        for(Point p : invCoords){
            if(hasFiveHit(p,getCurrentSignal())){
                hasWinner = true;
                break;
            }
        }
        
        if(hasWinner){
            JsonArray jWinnerCoords = new JsonArray();
            for(int i = 0; i < winnerCoords.length; i++){
                JsonObject jCoord = new JsonObject();
                jCoord.addProperty("x", winnerCoords[i].x + "");
                jCoord.addProperty("y", winnerCoords[i].y + "");
                jWinnerCoords.add(jCoord);
            }
            
            //owner
            JsonObject msg = new JsonObject();
            msg.addProperty("winner", getCurrentSignal());
            msg.addProperty("you", owner.getSignal());
            msg.add("coords", jWinnerCoords);
            owner.send(msg.toString());

            //partner
            msg = new JsonObject();
            msg.addProperty("winner", getCurrentSignal());
            msg.addProperty("you", partner.getSignal());
            msg.add("coords", jWinnerCoords);
            partner.send(msg.toString());

            owner.close();
            partner.close();
            close();
            return;
        }
        
        //check draw
        boolean draw = true;
        xloop:
        for(int x = 0; x < squares.length; x++){
            for(int y = 0; y < squares[x].length; y++){
                if(squares[x][y].equals("")){
                    draw = false;
                    break xloop;
                }
            }
        }
        
        if(draw){
            JsonObject msg = new JsonObject();
            msg.addProperty("draw", "1");
            owner.send(msg.toString());
            partner.send(msg.toString());
            
            owner.close();
            partner.close();
            close();
        }
    }
    
    private boolean hasFiveHit(Point cp, String chr){
        //horizontal check
        
        int hits = 0; 

        if(cp.x - 2 >= 0 && cp.x + 2 < squares.length){
            for(int x = -2; x <= 2; x++){
                if(squares[cp.x + x][cp.y].equals(chr)){
                    hits++;
                }
            }
        }

        if(hits == 5){
            for(int x = -2; x <= 2; x++){
                winnerCoords[x+2] = new Point(cp.x + x, cp.y);
            }

            return true;
        }


        //vertical check
        hits = 0; 

        if(cp.y - 2 >= 0 && cp.y + 2 < squares[0].length){
            for(int y = -2; y <= 2; y++){
                if(squares[cp.x][cp.y + y].equals(chr)){
                    hits++;
                }
            }
        }

        if(hits == 5){
            for(int y = -2; y <= 2; y++){
                winnerCoords[y+2] = new Point(cp.x, cp.y + y);
            }

            return true;
        }


        //backslash check
        hits = 0; 

        if(cp.y - 2 >= 0 && cp.x - 2 >= 0 && 
           cp.y + 2 < squares[0].length && cp.x + 2 < squares.length){
            for(int x = -2; x <= 2; x++){
                if(squares[cp.x + x][cp.y + x].equals(chr)){
                    hits++;
                }
            }
        }

        if(hits == 5){
            for(int x = -2; x <= 2; x++){
                winnerCoords[x+2] = new Point(cp.x + x, cp.y + x);
            }

            return true;
        }


        //slash check
        hits = 0; 

        if(cp.y - 2 >= 0 && cp.x - 2 >= 0 && 
           cp.y + 2 < squares[0].length && cp.x + 2 < squares.length){
            for(int x = -2; x <= 2; x++){
                if(squares[cp.x + x][cp.y - x].equals(chr)){
                    hits++;
                }
            }
        }

        if(hits == 5){
            for(int x = -2; x <= 2; x++){
                winnerCoords[x+2] = new Point(cp.x + x, cp.y - x);
            }

            return true;
        }

        return false;
    }
    
    private ArrayList<Point> involvedCoords(Point coord){
        ArrayList<Point> coords = new ArrayList<Point>();
        
        for(int x = -2; x <= 2; x++){
            for(int y = -2; y <= 2; y++){
                if(coord.x + x >= 0 && coord.y + y >= 0 &&
                   coord.x + x < squares.length && coord.y + y < squares[0].length &&
                   (x != -1 || y != -2) && (x != 1 || y != -2) &&
                   (x != -2 || y != -1) && (x != 2 || y != -1) &&
                   (x != -2 || y !=  1) && (x != 2 || y !=  1) &&
                   (x != -1 || y !=  2) && (x != 1 || y !=  2))
                {
                    coords.add(new Point(coord.x + x, coord.y + y));
                }
            }
        }
        
        return coords;
    }
}
