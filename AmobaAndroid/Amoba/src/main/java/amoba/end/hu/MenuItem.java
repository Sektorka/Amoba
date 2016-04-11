package amoba.end.hu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.Toast;

import amoba.end.hu.exceptions.NotConnectedToMasterServerException;

public class MenuItem extends Actor {
    protected String text;
    protected int width, height;
    protected MenuID id;

    public enum MenuID{
        MI_NEW_NETWORK_GAME,
        MI_NOT_STARTED_MATCHES,
        MI_PLAYED_GAMES,
        MI_SETTINGS,
        MI_ABOUT,
        MI_EXIT
    }

    public MenuItem(Activity parent, String text, MenuID id){
        super(parent);

        this.id = id;
        this.text = text;
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.getTextBounds(this.text, 0, this.text.length(), rect);
        paint.setShadowLayer(5.0f, 7.0f, 7.0f, Color.BLACK);
        width = rect.width();
        height = rect.height();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, rect.left, rect.top + height - 15, paint);
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setX(int x){
        super.setX(x);
        rect.right = x + width;
    }

    @Override
    public void setY(int y){
        super.setY(y);
        rect.bottom = y + height;
    }

    @Override
    public void onClick() {
        switch (id){
            case MI_NEW_NETWORK_GAME:
                if(!Manager.instance().isConnectedToMasterServer()){
                    Toast.makeText(parent, R.string.unableToConnectMasterServer, Toast.LENGTH_LONG).show();
                    return;
                }

                parent.startActivityForResult(new Intent(parent, NewGame.class), NewGame.NEW_SERVER_REQUEST);
                //parent.startActivity(new Intent(parent, GameActivity.class));
                break;
            case MI_NOT_STARTED_MATCHES:
                if(!Manager.instance().isConnectedToMasterServer()){
                    Toast.makeText(parent, R.string.unableToConnectMasterServer, Toast.LENGTH_LONG).show();
                    return;
                }

                parent.startActivityForResult(new Intent(parent, NotStartedMatchesActivity.class), NotStartedMatchesActivity.LIST_NOT_STARTED_GAMES);
                break;
            case MI_PLAYED_GAMES:
                parent.startActivity(new Intent(parent, PlayedGamesActivity.class));
                break;
            case MI_SETTINGS:
                parent.startActivity(new Intent(parent, SettingsActivity.class));
                break;
            case MI_ABOUT:
                parent.startActivity(new Intent(parent, AboutActivity.class));
                break;
            case MI_EXIT:
                StartActivity.instance().exit();
                break;
            default:
                Toast.makeText(StartActivity.instance(), text + " not binded!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
