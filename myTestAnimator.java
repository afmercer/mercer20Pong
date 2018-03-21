package edu.up.cs301.mercer20.mercer20pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by AdamMercer on 3/19/18.
 *
 */

public class myTestAnimator implements Animator {

    private double xDir;
    private double yDir;
    private double xCoord;
    private double yCoord;
    private double speed;
    private boolean moveRight;
    private boolean moveDown;
    private boolean ballInPlay;

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
        Random rand = new Random();

        xCoord = 100 + rand.nextInt(1800);
        yCoord = 100 + rand.nextInt(900);

        int direction = rand.nextInt(4);
        if(direction == 0) {
            xDir = 10 + rand.nextInt(11);
            yDir = 10 + rand.nextInt(11);
            moveRight = true;
            moveDown = true;
        }
        else if(direction == 1) {
            xDir = -20 + rand.nextInt(11);
            yDir = 10 + rand.nextInt(11);
            moveRight = false;
            moveDown = true;
        }
        else if(direction == 2) {
            xDir = 10 + rand.nextInt(11);
            yDir = -20 + rand.nextInt(11);
            moveRight = true;
            moveDown = false;
        }
        else if(direction == 3) {
            xDir = -20 + rand.nextInt(11);
            yDir = -20 + rand.nextInt(11);
            moveRight = false;
            moveDown = false;
        }

        double xSpeed = Math.abs(xDir);
        double ySpeed = Math.abs(yDir);
        speed = Math.sqrt((xSpeed*xSpeed)+(ySpeed*ySpeed));

        ballInPlay = true;
    }

    public void setPaddleSize() {

    }

    @Override
    public void tick(Canvas canvas) {
        //TODO citation
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //draw walls of playing area
        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(50);
        canvas.drawRect(15, 0, width-15, 15, whitePaint);
        canvas.drawRect(width-15, 0, width, height, whitePaint);
        canvas.drawRect(15, height-15, width-15, height, whitePaint);
        //draw paddle
        canvas.drawRect(0, 600, 15, height-600, whitePaint);

        Paint buttonPaint = new Paint();
        buttonPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawRect(width/2 - 300, 50, width/2 - 50, 200, buttonPaint);
        canvas.drawRect(width/2 + 50, 50, width/2 + 300, 200, buttonPaint);

        Paint buttonText = new Paint();
        buttonText.setColor(Color.BLACK);
        buttonText.setTextSize(45);
        canvas.drawText("Beginner", width/2 - 270, 140, buttonText);
        canvas.drawText("Expert", width/2 + 105, 140, buttonText);


        double angle = Math.atan(yDir/xDir);

        if(xCoord+50 >= width-15 && moveRight) {
            if(moveDown) {
                xDir = -(speed * Math.cos(angle));
                yDir = (speed * Math.sin(angle));
            }
            else {
                xDir = -(speed * Math.cos(angle));
                yDir = (speed * Math.sin(angle));
            }
            moveRight = false;
        }
        else if(xCoord-50 < 15 && xCoord-20 > 0 && yCoord > 600 && yCoord < height-600 && !moveRight) {
            if(moveDown) {
                xDir = (speed * Math.cos(angle));
                yDir = -(speed * Math.sin(angle));
            }
            else {
                xDir = (speed * Math.cos(angle));
                yDir = -(speed * Math.sin(angle));
            }
            moveRight = true;
        }
        else if(yCoord-50 <= 15 && !moveDown) {
            if(moveRight) {
                xDir = (speed * Math.cos(angle));
                yDir = -(speed * Math.sin(angle));
            }
            else {
                xDir = -(speed * Math.cos(angle));
                yDir = (speed * Math.sin(angle));
            }
            moveDown = true;
        }
        else if(yCoord+50 >= height-15 && moveDown) {
            if(moveRight) {
                xDir = (speed * Math.cos(angle));
                yDir = -(speed * Math.sin(angle));
            }
            else {
                xDir = -(speed * Math.cos(angle));
                yDir = (speed * Math.sin(angle));
            }
            moveDown = false;
        }
        else if(xCoord+50 < 0) {
            ballInPlay = false;
            canvas.drawText("Tap screen for new ball", 760, 696, whitePaint);
        }

        xCoord = xCoord+xDir;
        yCoord = yCoord+yDir;

        canvas.drawCircle((int)xCoord, (int)yCoord, 50, whitePaint);

    }

    @Override
    public void onTouch(MotionEvent event) {
        int xPos;
        int yPos;

        if(!ballInPlay) {
            newBall();
        }

        if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            xPos = (int)event.getX();
            yPos = (int)event.getY();

        }
    }
}
