package edu.up.cs301.mercer20.mercer20pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by AdamMercer on 3/19/18.
 */

public class myTestAnimator implements Animator {

    private double xDir;
    private double yDir;
    private double xCoord;
    private double yCoord;
    private boolean moveRight;
    private boolean moveDown;

    public myTestAnimator() {
        newBall();
    }

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

    public void newBall() {
        xCoord = 1024;
        yCoord = 696;

        Random rand = new Random();
        int direction = rand.nextInt(4);
        if(direction == 0) {
            xDir = 15 + rand.nextInt(6);
            yDir = 15 + rand.nextInt(6);
            moveRight = true;
            moveDown = true;
        }
        else if(direction == 1) {
            xDir = -20 + rand.nextInt(6);
            yDir = 15 + rand.nextInt(6);
            moveRight = false;
            moveDown = true;
        }
        else if(direction == 2) {
            xDir = 15 + rand.nextInt(6);
            yDir = -20 + rand.nextInt(6);
            moveRight = true;
            moveDown = false;
        }
        else if(direction == 3) {
            xDir = -20 + rand.nextInt(6);
            yDir = -20 + rand.nextInt(6);
            moveRight = false;
            moveDown = false;
        }

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


        double angle = Math.atan(yDir/xDir);

        if(xCoord+50 >= width-15 && moveRight) {
            if(moveDown) {
                xDir = -(20 * Math.cos(angle));
                yDir = (20 * Math.sin(angle));
            }
            else {
                xDir = -(20 * Math.cos(angle));
                yDir = (20 * Math.sin(angle));
            }
            moveRight = false;
        }
        else if(xCoord-50 < 15 && xCoord-20 > 0 && yCoord > 600 && yCoord < height-600 && !moveRight) {
            if(moveDown) {
                xDir = (20 * Math.cos(angle));
                yDir = -(20 * Math.sin(angle));
            }
            else {
                xDir = (20 * Math.cos(angle));
                yDir = -(20 * Math.sin(angle));
            }
            moveRight = true;
        }
        else if(yCoord-50 <= 15 && !moveDown) {
            if(moveRight) {
                xDir = (20 * Math.cos(angle));
                yDir = -(20 * Math.sin(angle));
            }
            else {
                xDir = -(20 * Math.cos(angle));
                yDir = (20 * Math.sin(angle));
            }
            moveDown = true;
        }
        else if(yCoord+50 >= height-15 && moveDown) {
            if(moveRight) {
                xDir = (20 * Math.cos(angle));
                yDir = -(20 * Math.sin(angle));
            }
            else {
                xDir = -(20 * Math.cos(angle));
                yDir = (20 * Math.sin(angle));
            }
            moveDown = false;
        }
        else if(xCoord+50 < 0) {
            newBall();
        }

        xCoord = xCoord+xDir;
        yCoord = yCoord+yDir;

        canvas.drawCircle((int)xCoord, (int)yCoord, 50, whitePaint);

    }

    @Override
    public void onTouch(MotionEvent event) {

    }
}
