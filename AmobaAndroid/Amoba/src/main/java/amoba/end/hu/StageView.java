package amoba.end.hu;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.ViewGroup;

import org.json.JSONObject;

public class StageView extends DrawView {
    protected int width, height;
    protected Cell[][] cells;
    protected Cell.Signal signal;
    protected String name;
    protected GameActivity gameActivity;

    private static final int MAX_CLICK_DURATION = 1000;
    private static final int MAX_CLICK_DISTANCE = 15;

    private long pressStartTime;
    private float pressedX;
    private float pressedY;

    public StageView(Context context, int width, int height, Cell.Signal signal, String name) {
        super(context);

        this.width = width;
        this.height = height;
        this.signal = signal;
        this.name = name;
        this.cells = new Cell[width][height];

        if(context instanceof GameActivity){
            this.gameActivity = (GameActivity) context;
        }

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                cells[i][j] = new Cell(GameActivity.instance(), i, j);
            }
        }

        this.setLayoutParams(new ViewGroup.LayoutParams(width * Cell.WIDTH + width * Cell.MARGIN_V + Cell.MARGIN_V, height * Cell.HEIGHT + height * Cell.MARGIN_H + Cell.MARGIN_H));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                cells[i][j].draw(canvas);
            }
        }
    }

    public void doStep(int coordx, int coordy, Cell.Signal signal){
        cells[coordx][coordy].onClick(signal);
        invalidate();
    }

    private void doClick(int x, int y){
        search:{
            for(int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    if(cells[i][j].getRect().contains(x, y)){

                        try{
                            JSONObject msg = new JSONObject();
                            msg.put("step", "1");
                            msg.put("coordx", i + "");
                            msg.put("coordy", j + "");

                            gameActivity.getOut().println(msg.toString());
                        }
                        catch (Exception e){

                        }
                        break search;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = System.currentTimeMillis();
                pressedX = e.getX();
                pressedY = e.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                long pressDuration = System.currentTimeMillis() - pressStartTime;

                if (pressDuration < MAX_CLICK_DURATION && distance(pressedX, pressedY, e.getX(), e.getY()) < MAX_CLICK_DISTANCE) {
                    doClick((int)e.getX(), (int)e.getY());
                }
            }
        }

        return super.onTouchEvent(e);
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx);
    }

    private static float pxToDp(float px) {
        return px / GameActivity.instance().getResources().getDisplayMetrics().density;
    }
}
