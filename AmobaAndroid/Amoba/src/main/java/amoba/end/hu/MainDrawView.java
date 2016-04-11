package amoba.end.hu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class MainDrawView extends DrawView {
    private static final String TAG = "MainDrawView";
    private List<MenuItem> menu;

    public MainDrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();

        //set title
        String title = "Amőba";
        float titleLength = 0;
        float titleX = 0, titleY = 0;
        paint = new Paint();
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        paint.setTextSize(150);
        paint.setTypeface(tf);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        //paint.setStrokeWidth(2);
        //paint.setStyle(Paint.Style.STROKE);
        paint.setShadowLayer(5.0f, 7.0f, 7.0f, Color.BLACK);

        titleLength = paint.measureText(title);
        titleX = canvas.getWidth() / 2 - titleLength / 2;
        titleY = canvas.getHeight() / 3;

        canvas.drawText(title, titleX, titleY, paint);

        //set menu
        menu = new ArrayList<MenuItem>();
        int menuItemX, menuItemY;

        menu.add(new MenuItem(StartActivity.instance(),StartActivity.instance().getString(R.string.miNewGame), MenuItem.MenuID.MI_NEW_NETWORK_GAME));
        menu.add(new MenuItem(StartActivity.instance(),StartActivity.instance().getString(R.string.miCurrentGames), MenuItem.MenuID.MI_NOT_STARTED_MATCHES));
        //menu.add(new MenuItem(StartActivity.instance(),StartActivity.instance().getString(R.string.miPlayedGames), MenuItem.MenuID.MI_PLAYED_GAMES));
        menu.add(new MenuItem(StartActivity.instance(),StartActivity.instance().getString(R.string.miSettings), MenuItem.MenuID.MI_SETTINGS));
        menu.add(new MenuItem(StartActivity.instance(),StartActivity.instance().getString(R.string.miAbout), MenuItem.MenuID.MI_ABOUT));
        menu.add(new MenuItem(StartActivity.instance(),StartActivity.instance().getString(R.string.miExit), MenuItem.MenuID.MI_EXIT));

        int count = 1;
        final int margin = 30;
        int miHeight = 65;
        titleY += 100;

        for(MenuItem menuItem : menu){
            menuItem.setHeight(miHeight);
            menuItem.setX(canvas.getWidth() / 2 - menuItem.getWidth() / 2);
            menuItem.setY((int) titleY + ((miHeight + margin) * count)  );
            menuItem.draw(canvas);
            //canvas.dr

            count++;
        }

        //super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(menu != null){
            for(MenuItem mi : menu){
                if(mi.pointContains(event.getX(),event.getY())){
                    mi.onClick();
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point();
        point.set((int)event.getX(), (int)event.getY());
        points.add(point);
        invalidate();
        return true;
    }*/
}
