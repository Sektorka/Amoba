package amoba.end.hu;

import amoba.end.hu.exceptions.NotConnectedToMasterServerException;
import amoba.end.hu.interfaces.NotStartedMatch;
import amoba.end.hu.interfaces.ResponseJSONMessage;
import amoba.end.hu.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class StartActivity extends Activity implements ResponseJSONMessage{
    private MainDrawView mainDrawView;

    private Socket sock;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private AsyncTask thread = null;

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final boolean TOGGLE_ON_CLICK = true;

    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    private SystemUiHider mSystemUiHider;

    private static StartActivity inst = null;

    public static StartActivity instance(){
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        inst = this;

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
            // Cached values.
            int mControlsHeight;
            int mShortAnimTime;

            @Override
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
            public void onVisibilityChange(boolean visible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

                    if (mControlsHeight == 0) {
                        mControlsHeight = controlsView.getHeight();
                    }
                    if (mShortAnimTime == 0) {
                        mShortAnimTime = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                    }
                    controlsView.animate()
                            .translationY(visible ? 0 : mControlsHeight)
                            .setDuration(mShortAnimTime);
                } else {

                    controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                if (visible && AUTO_HIDE) {
                    // Schedule a hide().
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
            }
        });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);

        //DbManager.getInstance().reCreateDb();

        mainDrawView = new MainDrawView(this);
        setContentView(mainDrawView);
        mainDrawView.requestFocus();

        Manager.instance();

        try{
            boolean needSave = false;
            if(Settings.instance().getMasterServerHost() == null){
                Settings.instance().setMasterServerHost(InetAddress.getByName("193.164.131.22"));
                needSave = true;
            }
            if(Settings.instance().getMasterServerPort() == 0){
                Settings.instance().setMasterServerPort(9100);
                needSave = true;
            }
            if(Settings.instance().getClientId() == null || Settings.instance().getClientId().length() != 30){
                Settings.instance().setClientId(new PasswordGenerator(true, true, true, false, 30).GeneratePassword());
                needSave = true;
            }
            if(Settings.instance().getNickName() == null || Settings.instance().getNickName() == ""){
                Settings.instance().setNickName(getString(R.string.player));
                needSave = true;
            }

            if(needSave){
                Settings.instance().save();
            }
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(this, "connecting to master server!", Toast.LENGTH_LONG).show();
        Manager.instance().connectToMasterServer(this);
    }


    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent mot) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == NewGame.NEW_SERVER_REQUEST){
                try{
                    Manager.instance().startNewServer(
                            data.getExtras().getString(NewGame.SERVER_NAME),
                            data.getExtras().getString(NewGame.SERVER_PASSWORD),
                            data.getExtras().getInt(NewGame.STAGE_WIDTH),
                            data.getExtras().getInt(NewGame.STAGE_HEIGHT),
                            this
                    );
                }
                catch(NotConnectedToMasterServerException e){
                    Toast.makeText(this, R.string.unableToConnectMasterServer, Toast.LENGTH_LONG).show();
                }
            }
            else if(requestCode == NotStartedMatchesActivity.LIST_NOT_STARTED_GAMES){
                NotStartedMatch nsm = (NotStartedMatch) data.getExtras().getSerializable(NotStartedMatchesActivity.NOT_STARTED_MATCH_OBJECT);

                Intent gameIntent = new Intent(this, GameActivity.class);

                gameIntent.putExtra(GameActivity.GS_HOST, Settings.instance().getMasterServerHost().getHostAddress());
                gameIntent.putExtra(GameActivity.GS_PORT, nsm.port);
                //gameIntent.putExtra(GameActivity.GS_PASS, nsm.password);

                startActivity(gameIntent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMessage(JSONObject json) {
        try {
            if(json.has("masterserver")){
                if(json.getString("masterserver").equals("1")){
                    //Toast.makeText(this, "Successfully connected to master server!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, getString(R.string.unableToConnectMasterServer) + (json.has("reason") ? "\n" + json.getString("reason") : ""), Toast.LENGTH_LONG).show();
                }
            }
            else if(json.has("newServer")){
                if(json.getString("newServer").equals("1")){
                    Intent gameIntent = new Intent(this, GameActivity.class);

                    gameIntent.putExtra(GameActivity.GS_HOST, Settings.instance().getMasterServerHost().getHostAddress());
                    gameIntent.putExtra(GameActivity.GS_PORT, Integer.parseInt(json.getString("port")));
                    gameIntent.putExtra(GameActivity.GS_PASS, json.getString("password"));

                    startActivity(gameIntent);
                }
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialogExit);
        builder.setMessage(R.string.dialogExitGameConfirm);
        builder.setIcon(android.R.drawable.ic_menu_help);

        builder.setPositiveButton(R.string.dialogOptionYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton(R.string.dialogOptionNo, null);
        builder.create().show();
    }
}
