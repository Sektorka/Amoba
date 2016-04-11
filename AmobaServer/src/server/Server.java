package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final boolean DEBUG = true;
    
    private ServerSocket listener;
    private ArrayList<GameServer> gameServers;
    private ArrayList<ClientWorker> clients;
    
    private static int lastSettedPort = 10000;
    private static final int ATTEMPT_CREATE_GAME_SERVER = 30;
    
    public Server(int port) throws IOException{
        gameServers = new ArrayList<GameServer>();
        clients = new ArrayList<ClientWorker>();
        
        listener = new ServerSocket(port);
        listener.setReuseAddress(true);
        System.out.println("Az amőba szerver a következő porton figyel: " + port);
    }
    
    public void start(){
        Socket client;
        while(true){
            try{
                client = listener.accept();
                System.out.println("Új kapcsolat: " + client.getInetAddress().getHostAddress() + ":" + client.getPort());
                ClientWorker cw = new ClientWorker(client,this);
                cw.start();
                clients.add(cw);
            }
            catch(Exception e){
                if(DEBUG){
                    e.printStackTrace();
                }
            }
        }
    }
    
    public ArrayList<GameServer> getGameServers(){
        return gameServers;
    }
    
    public GameServer createGameServer(String serverName, String joinPassword, int width, int height){
        int counter = 0;
        
        do{
            try{
                GameServer gs = new GameServer(this, (joinPassword == null ? "" : joinPassword),++lastSettedPort,(serverName == null ? "No name" : serverName), width, height);
                gs.start();
                gameServers.add(gs);
                return gs;
            }
            catch(Exception e){
                if(DEBUG){
                    e.printStackTrace();
                }
            }
            
            counter++;
        }
        while(counter < ATTEMPT_CREATE_GAME_SERVER);
        
        return null;
    }
    
    public static void main(String[] args) {
        int port = 10000;
        
        if(args.length != 1){
            printUsageAndExit();
        }
        else{
            try {
                port = Integer.parseInt(args[0]);
                
                if(port <= 0 || port > 65535){
                    printUsageAndExit();
                }
            } catch (Exception e) {
                printUsageAndExit();
            }
        }
        
        try {
            new Server(port).start();
        } catch (IOException ex) {
            if(DEBUG){
                ex.printStackTrace();
            }
        }
    }
    
    private static void printUsageAndExit(){
        System.err.println("Használat: AmobaServer <Port>");
        System.exit(-1);
    }
}
