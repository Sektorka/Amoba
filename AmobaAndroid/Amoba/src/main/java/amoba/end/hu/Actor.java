package amoba.end.hu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Actor {
    protected Rect rect;
    protected Paint paint;
    protected Activity parent;

    public Actor(Activity parent){
        rect = new Rect();
        paint = new Paint();
        this.parent = parent;
    }

    public Paint getPaint(){
        return paint;
    }

    public Rect getRect(){
        return rect;
    }

    public void draw(Canvas canvas){
        canvas.drawPaint(paint);
    }

    public boolean pointContains(Point point){
        return pointContains(point.x, point.y);
    }

    public boolean pointContains(float x, float y){
        if(rect == null){
            return false;
        }

        return rect.contains((int)x, (int)y);
    }

    public int getHeight(){
        return rect.bottom - rect.top;
    }

    public int getWidth(){
        return rect.right - rect.left;
    }

    public void setX(int x){
        rect.left = x;
    }

    public void setY(int y){
        rect.top = y;
    }

    public void onClick(){}
}
