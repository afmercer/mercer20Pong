package edu.up.cs301.mercer20.mercer20pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by AdamMercer on 3/19/18.
 */

public class myTestAnimator implements Animator {

    private int xDir = 10;
    private int yDir = 15;
    private int xCoord = 100;
    private int yCoord = 500;

    @Override
    public int interval() {
        return 30;
    }

    @Override
    public int backgroundColor() {
        return Color.rgb(19, 22, 66);
    }

    @Override
    public boolean doPause() {
        return false;
    }

    @Override
    public boolean doQuit() {
        return false;
    }

    @Override
    public void tick(Canvas canvas) {
        //TODO citation
        int width = canvas.getWidth();
        int height = canvas.getHeight();


        //draw walls of playing area
        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        canvas.drawRect(15, 0, width-15, 15, whitePaint);
        canvas.drawRect(width-15, 0, width, height, whitePaint);
        canvas.drawRect(15, height-15, width-15, height, whitePaint);
        //draw paddle
        canvas.drawRect(0, 600, 15, height-600, whitePaint);



        int num = xCoord%200;
        double angle = Math.atan(yDir/xDir);
        if(num < 5) {
            xDir = (int)(10*Math.cos(angle));
            yDir = -(int)(10*Math.sin(angle));
        }

        xCoord = xCoord+xDir;
        yCoord = yCoord+yDir;



        canvas.drawCircle(xCoord, yCoord, 50, whitePaint);

    }

    @Override
    public void onTouch(MotionEvent event) {

    }
}
