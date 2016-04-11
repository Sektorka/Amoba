package amoba.end.hu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import amoba.end.hu.interfaces.NewChatMessage;
import amoba.end.hu.interfaces.ResponseJSONMessage;

public class GameActivity extends Activity {
    protected StageView stageView;
    protected AsyncTask<Object, JSONObject, Object> thread;
    protected Socket sock;
    protected BufferedReader in = null;
    protected PrintWriter out = null;
    protected InetAddress host;
    protected int port;
    protected String pass;
    protected boolean ownGame;
    protected boolean partnerJoined;
    protected boolean ignoreClose;
    protected String name;
    protected ProgressDialog waitingForPlayer;
    protected ResponseJSONMessage receiveChatMessageCallback;

    protected ArrayList<ChatMessage> chatMessages;

    private static GameActivity inst = null;

    public static final String GS_HOST = "GS_HOST";
    public static final String GS_PORT = "GS_PORT";
    public static final String GS_PASS = "GS_PASS";

    public static GameActivity instance(){
        return inst;
    }

    private LinearLayout llDrawView;

    public PrintWriter getOut() {
        return out;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        inst = this;

        try{
            host = InetAddress.getByName(getIntent().getExtras().getString(GS_HOST));
            port = getIntent().getExtras().getInt(GS_PORT);
            pass = getIntent().getExtras().getString(GS_PASS);

            sock = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(),true);

            thread = new AsyncTask<Object, JSONObject, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    while(true){
                        try {
                            String line = in.readLine();

                            if(line == null){
                                return null;
                            }

                            System.out.println("SUBLINE=" + line);
                            publishProgress(new JSONObject(line));
                        }
                        catch (Exception e){
                            if(e.getMessage() == null || e.getMessage().equals("Socket closed")){
                                return null;
                            }
                            System.out.println("bgtask progress except: " + e.getMessage());
                        }
                    }
                }

                @Override
                protected void onProgressUpdate(JSONObject... values) {
                    GameActivity.this.onServerMessage(values[0]);
                }
            };
            thread.execute();

            JSONObject msg = new JSONObject();
            msg.put("clientID", Settings.instance().getClientId());
            msg.put("nickName", Settings.instance().getNickName());
            out.println(msg.toString());

            if(pass != null){
                msg = new JSONObject();
                msg.put("password", pass);
                out.println(msg.toString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        llDrawView = (LinearLayout) findViewById(R.id.llDrawView);
        chatMessages = new ArrayList<ChatMessage>();
    }

    protected void onServerMessage(JSONObject json){
        try{
        if(json.has("accept")){
            if(json.getString("accept").equals("1")){
                name = json.getString("name");
                int width = Integer.parseInt(json.getString("width"));
                int height = Integer.parseInt(json.getString("height"));
                Cell.Signal signal = (json.getString("signal").equals("O") ? Cell.Signal.O : Cell.Signal.X);

                if(json.has("owner") && json.getString("owner").equals("1")){
                    ownGame = true;

                    waitingForPlayer = new ProgressDialog(this);
                    waitingForPlayer.setTitle(name);
                    waitingForPlayer.setMessage(getString(R.string.waitingForPartner));
                    waitingForPlayer.setCancelable(false);
                    waitingForPlayer.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                close();
                                sock.close();
                            }
                            catch (Exception e){}
                            finally {
                                finish();
                            }
                        }
                    });
                    waitingForPlayer.show();
                }

                stageView = new StageView(this, width, height, signal, name);
                llDrawView.addView(stageView);
            }
        }
        else if(json.has("step") && json.has("coordx") && json.has("coordy")){
            int cx = Integer.parseInt(json.getString("coordx"));
            int cy = Integer.parseInt(json.getString("coordy"));
            Cell.Signal signal = (json.getString("step").equals("O") ? Cell.Signal.O : Cell.Signal.X);
            stageView.doStep(cx, cy, signal);

            /*squares[cx][cy].setText(json.get("step").toString());
            squares[lastPoint.x][lastPoint.y].setForeground(Color.BLACK);
            squares[cx][cy].setForeground(Color.RED);
            squares[cx][cy].getModel().setSelected(true);
            lastPoint.x = cx;
            lastPoint.y = cy;*/
        }

        else if(json.has("winner") && json.has("you") && json.has("coords")){
            JSONArray coords = json.getJSONArray("coords");

            for(int i = 0; i < coords.length(); i++){
                if(coords.getJSONObject(i).has("x") && coords.getJSONObject(i).has("y")){
                    int wx = Integer.parseInt(coords.getJSONObject(i).getString("x"));
                    int wy = Integer.parseInt(coords.getJSONObject(i).getString("y"));
                    //squares[wx][wy].setForeground(Color.RED);
                }
            }

            if(json.get("winner").equals(json.get("you"))){
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.app_name);
                alertDialogBuilder.setMessage(R.string.youWin);
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);

                alertDialogBuilder.setPositiveButton(R.string.btnClose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try{
                            sock.close();
                        }
                        catch (Exception e){

                        }
                        finally {
                            finish();
                        }
                    }
                });

                alertDialogBuilder.create().show();
            }
            else{
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.app_name);
                alertDialogBuilder.setMessage(R.string.youLose);
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);

                alertDialogBuilder.setPositiveButton(R.string.btnClose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try{
                            sock.close();
                        }
                        catch (Exception e){

                        }
                        finally {
                            finish();
                        }
                    }
                });

                alertDialogBuilder.create().show();
            }
        }

        else if(json.has("draw")){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.app_name);
            alertDialogBuilder.setMessage(R.string.stateDraw);
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);

            alertDialogBuilder.setPositiveButton(R.string.btnClose, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create().show();
            return;
        }

        else if(json.has("yourturn")){
            //setTurn(json.get("yourturn").equals("1"));
        }

        else if(!ignoreClose && partnerJoined && json.has("serverClosed") && json.getString("serverClosed").equals("1")){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.app_name);
            alertDialogBuilder.setMessage(R.string.partnerLeavedTheMatch);
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

            alertDialogBuilder.setPositiveButton(R.string.btnClose, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try{
                        sock.close();
                    }
                    catch(Exception e){}
                    finally {
                        finish();
                    }
                }
            });

            alertDialogBuilder.create().show();


        }

        //we got new chat message
        else if(json.has("message") && json.has("name")){
            ChatMessage chatMsg = new ChatMessage(json.getString("message"), json.getString("name"));
            addChatMessage(chatMsg);

            if(receiveChatMessageCallback != null){
                receiveChatMessageCallback.onMessage(json);

                if(receiveChatMessageCallback instanceof NewChatMessage){

                    ((NewChatMessage) receiveChatMessageCallback).onGotChatMessage(chatMsg);
                }
            }
        }

        else if(json.has("requireJoinPassword") && json.has("serverName")){
            View password_dialog = LayoutInflater.from(this).inflate(R.layout.password_dialog, null);
            name = json.getString("serverName");
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(name + " :: " + getString(R.string.joinPassword));
            alertDialogBuilder.setView(password_dialog);
            alertDialogBuilder.setPositiveButton(R.string.btnEnter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText tbPassword = (EditText) ((AlertDialog) dialog).findViewById(R.id.tbPassword);

                    try {
                        JSONObject msg = new JSONObject();
                        msg.put("joinPassword", tbPassword.getText());
                        out.println(msg.toString());
                    } catch (Exception e) {
                    }
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        sock.close();
                    } catch (Exception e) {
                    } finally {
                        finish();
                    }
                }
            });

            alertDialogBuilder.create().show();
        }

        else if(json.has("accept") && json.get("accept").equals("0") &&
                json.has("partner") && json.get("partner").equals("0") &&
                json.has("reason"))
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.app_name);
            alertDialogBuilder.setMessage(R.string.invalidPasswordTyped);
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

            alertDialogBuilder.create().show();

            sock.close();
            finish();
            return;
        }

        else if(json.has("partnerJoined") && json.get("partnerJoined").equals("1")){
            partnerJoined = true;
            waitingForPlayer.dismiss();
        }


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(name)
                    .setMessage(R.string.areYouSureYouWantToAbortCurrentGame)
                    .setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                close();
                                sock.close();
                            }
                            catch (Exception e){}
                            finally {
                                finish();
                            }
                        }

                    })
                    .setNegativeButton(R.string.btnNo, null)
                    .create()
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void close(){
        try{
            JSONObject msg = new JSONObject();
            msg.put("closeServer", "1");
            out.println(msg.toString());
        }
        catch(Exception e){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        startActivity(new Intent(this, ChatActivity.class));
        return false;
    }

    public void sendChatMessage(String message) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("message", message);
            out.println(msg.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setReceiveChatMessageCallback(ResponseJSONMessage callback){
        receiveChatMessageCallback = callback;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void addChatMessage(ChatMessage chatMessage){
        chatMessages.add(chatMessage);
    }
}
