package amoba.end.hu;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amoba.end.hu.exceptions.NotConnectedToMasterServerException;
import amoba.end.hu.interfaces.NewChatMessage;
import amoba.end.hu.interfaces.NotStartedMatches;
import amoba.end.hu.interfaces.NotStartedMatch;
import amoba.end.hu.interfaces.PlayedGame;
import amoba.end.hu.interfaces.PlayedGames;
import amoba.end.hu.interfaces.ResponseJSONMessage;

public class Manager extends AsyncTask<Object, JSONObject, Object>{
    private Socket sock;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private boolean connectedToMasterServer = false;

    private ResponseJSONMessage connectedToServerCallback, notStartedMatchesCallback,
            playedGamesCallback, gameServerCreatedCallback;

    private static Manager inst = null;

    public static Manager instance(){
        if(inst == null){
            inst = new Manager();
        }

        return inst;
    }

    private Manager(){

    }

    @Override
    protected void onProgressUpdate(JSONObject[] updateDataJsons) {
        for(JSONObject json : updateDataJsons){
            try{
                if(json.has("masterserver")){
                    if(json.getString("masterserver").equals("1")){
                        connectedToMasterServer = true;
                    }

                    if(connectedToServerCallback != null){
                        connectedToServerCallback.onMessage(json);
                    }
                }

                //response of game servers list
                else if(json.has("gameServers")){
                    if(notStartedMatchesCallback != null){
                        notStartedMatchesCallback.onMessage(json);

                        if(notStartedMatchesCallback instanceof NotStartedMatches){
                            List<NotStartedMatch> list = new ArrayList<NotStartedMatch>();
                            JSONArray jsonArray = json.getJSONArray("gameServers");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                if(jsonObj.has("name") && jsonObj.has("port") && jsonObj.has("password")){
                                    list.add(
                                            new NotStartedMatch(
                                                    jsonObj.getString("name"),
                                                    jsonObj.getInt("port"),
                                                    jsonObj.getString("password").equals("1")
                                            )
                                    );
                                }
                            }

                            ((NotStartedMatches) notStartedMatchesCallback).onGotNotStartedMatchesList(list);
                        }
                    }
                }//end of game server

                //response of played games list
                else if(json.has("playedGames")){
                    if(playedGamesCallback != null){
                        playedGamesCallback.onMessage(json);

                        if(playedGamesCallback instanceof PlayedGames){
                            List<PlayedGame> list = new ArrayList<PlayedGame>();
                            JSONArray jsonArray = json.getJSONArray("playedGames");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                if(jsonObj.has("header") && jsonObj.has("body")){
                                    JSONObject joHeader = jsonObj.getJSONObject("header");
                                    JSONArray joBody = jsonObj.getJSONArray("body");

                                    PlayedGame.Header header = new PlayedGame.Header(
                                            new Date(joHeader.getLong("timestamp")),
                                            joHeader.getInt("width"),
                                            joHeader.getInt("height")
                                    );

                                    List<PlayedGame.Step> body = new ArrayList<PlayedGame.Step>();

                                    for(int j = 0; j < joBody.length(); j++){
                                        body.add(
                                                new PlayedGame.Step(
                                                        joBody.getJSONObject(i).getString("signal").charAt(0),
                                                        joBody.getJSONObject(i).getInt("x"),
                                                        joBody.getJSONObject(i).getInt("y")
                                                )
                                        );
                                    }

                                    list.add(
                                            new PlayedGame(header, body)
                                    );
                                }
                            }

                            ((PlayedGames) playedGamesCallback).onGotNotStartedMatchesList(list);
                        }
                    }
                }
                //new server created response
                else if(json.has("newServer")){
                    if(gameServerCreatedCallback != null){
                        gameServerCreatedCallback.onMessage(json);
                    }
                }
            }
            catch (Exception e){
                System.out.println("Update progress except: " + e.getMessage());
            }
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        while(true){
            try {
                String line = in.readLine();
                System.out.println("LINE=" + line);
                publishProgress(new JSONObject(line));
            }
            catch (Exception e){
                System.out.println("bgtask progress except: " + e.getMessage());
            }
        }
    }

    public void connectToMasterServer(){
        connectToMasterServer(null);
    }

    public void connectToMasterServer(ResponseJSONMessage callback){
        if(connectedToMasterServer){
            return;
        }

        connectedToServerCallback = callback;

        try{
            sock = new Socket(Settings.instance().getMasterServerHost(), Settings.instance().getMasterServerPort());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(),true);

            final JSONObject msg = new JSONObject();
            msg.put("clientID", Settings.instance().getClientId());
            msg.put("nickName", Settings.instance().getNickName());
            out.println(msg.toString());

            execute();
        }
        catch(Exception e){
            try{
                JSONObject msg = new JSONObject();
                msg.put("masterserver","0");
                msg.put("reason",e.getMessage());
                connectedToServerCallback.onMessage(msg);
            }
            catch (Exception ex){}
        }
    }

    public void getNotStartedMatches(ResponseJSONMessage callback) throws NotConnectedToMasterServerException{
        if(!connectedToMasterServer){
            throw new NotConnectedToMasterServerException();
        }

        notStartedMatchesCallback = callback;

        try{
            final JSONObject msg = new JSONObject();
            msg.put("listGameServers","1");
            out.println(msg.toString());
        }
        catch(JSONException e){}
    }

    public void getPlayedGames(ResponseJSONMessage callback) throws NotConnectedToMasterServerException{
        if(!connectedToMasterServer){
            throw new NotConnectedToMasterServerException();
        }

        playedGamesCallback = callback;

        try{
            final JSONObject msg = new JSONObject();
            msg.put("listPlayedGames","1");
            out.println(msg.toString());
        }
        catch(JSONException e){}
    }

    public void startNewServer(String serverName, String joinPassword, int width, int height, ResponseJSONMessage callback) throws NotConnectedToMasterServerException{
        if(!connectedToMasterServer){
            throw new NotConnectedToMasterServerException();
        }

        gameServerCreatedCallback = callback;

        try {
            JSONObject msg = new JSONObject();

            msg.put("newServer", serverName);
            msg.put("joinPassword", joinPassword);
            msg.put("width", width + "");
            msg.put("height", height + "");

            out.println(msg.toString());
        } catch (JSONException e) {}
    }

    public boolean isConnectedToMasterServer() {
        return connectedToMasterServer;
    }
}
