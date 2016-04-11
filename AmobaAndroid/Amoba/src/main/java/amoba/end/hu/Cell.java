package amoba.end.hu;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cell extends Actor {
    protected int coordx, coordy;
    protected boolean pushed = false;
    protected Signal signal;
    public static final int WIDTH = 100, HEIGHT = 100, MARGIN_V = 15, MARGIN_H = 15;

    public enum Signal{
        X, O;
    }

    public Cell(Activity parent, int coordx, int coordy) {
        super(parent);

        this.coordx = coordx;
        this.coordy = coordy;

        int left = this.coordx * WIDTH + (this.coordx + 1) * MARGIN_H;
        int top = this.coordy * HEIGHT + (this.coordy + 1) * MARGIN_V;
        int right = this.coordx * WIDTH + WIDTH + (this.coordx + 1) * MARGIN_H;
        int bottom = this.coordy * HEIGHT + HEIGHT + (this.coordy + 1) * MARGIN_V;

        rect.set(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);

        if(pushed){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            paint.setTextSize(100);

            switch (signal){
                case X:
                    canvas.drawText("X",rect.left + 20, rect.top + 80, paint);
                    break;
                case O:
                    canvas.drawText("O",rect.left + 20, rect.top + 80, paint);
                    break;
            }

            //Toast.makeText(this.parent,"Clicked: " + coordx + "x" + coordy, Toast.LENGTH_SHORT).show();
        }

    }

    public int getCoordX() {
        return coordx;
    }

    public int getCoordY() {
        return coordy;
    }

    public void onClick(Signal signal) {
        pushed = true;
        this.signal = signal;
    }
}
