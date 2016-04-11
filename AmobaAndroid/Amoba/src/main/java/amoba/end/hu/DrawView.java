package amoba.end.hu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

public class DrawView extends View {
    private static final String TAG = "DrawView";
    protected Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);

        setFocusable(true);
        setFocusableInTouchMode(true);

        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.DKGRAY);
        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.parseColor("#5C5C5C"), Color.parseColor("#313131"), Shader.TileMode.MIRROR));

        canvas.drawPaint(paint);
    }


}
