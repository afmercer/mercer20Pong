package edu.up.cs301.mercer20.mercer20pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import java.util.Random;

/**
 * class that animates a ball bouncing off walls on three sides, and
 * a paddle on the fourth side. When the ball leaves the screen, a new
 * ball is created in a random position with a random speed and random
 * direction.
 *
 * @author Adam Mercer
 * @version March 2018
 */

public class myTestAnimator implements Animator {

    //instance variables
    private int width; //width of animation surface
    private int height; //height of animation surface
    private double xDir; //scaled amount of horizontal motion the ball has
    private double yDir; //scaled amount of vertical motion the ball has
    private double xCoord; //x coordinate of the center of the ball
    private double yCoord; //y coordinate of center of ball
    private double speed; //amount of pixels the ball moves per frame
    private boolean moveRight; //whether ball is moving right
    private boolean moveDown; //whether the ball is moving down
    private boolean ballInPlay; //whether there is a ball on the screen
    private int paddleSize = 550; //determines size of paddle

    /**
     * Constructor, creates a new ball when a new test begins
     */
    public myTestAnimator() {
        newBall();
    }

    /**
     * interval between frames: 0.03 seconds.
     *
     * @return the time interval between frames, in milliseconds.
     */
    @Override
    public int interval() {
        return 30;
    }

    /**
     * The background color: a dark blue.
     *
     * @return the color of the background
     */
    @Override
    public int backgroundColor() {
        return Color.rgb(19, 22, 66);
    }

    /**
     * Tells that animation never pauses.
     *
     * @return indication of whether to pause.
     */
    @Override
    public boolean doPause() {
        return false;
    }

    /**
     * Tells the the animation never quits.
     *
     * @return indication of whether to quit.
     */
    @Override
    public boolean doQuit() {
        return false;
    }

    /**
     * Creates a new ball at a random position with a random speed,
     * and a random direction
     */
    public void newBall() {
        Random rand = new Random(); //used to generate random values

        //create new ball at a random position on the screen
        xCoord = 100 + rand.nextInt(1800);
        yCoord = 100 + rand.nextInt(900);

        //send ball in a random direction at a random speed
        int direction = rand.nextInt(4);
        //ball goes up and right
        if(direction == 0) {
            //ball moves between 10-20 pixels in the
            // horizontal and vertical direction
            xDir = 10 + rand.nextInt(11);
            yDir = 10 + rand.nextInt(11);
            moveRight = true;
            moveDown = true;
        }
        //ball goes up and left
        else if(direction == 1) {
            xDir = -20 + rand.nextInt(11);
            yDir = 10 + rand.nextInt(11);
            moveRight = false;
            moveDown = true;
        }
        //ball goes down and right
        else if(direction == 2) {
            xDir = 10 + rand.nextInt(11);
            yDir = -20 + rand.nextInt(11);
            moveRight = true;
            moveDown = false;
        }
        //ball goes down and left
        else if(direction == 3) {
            xDir = -20 + rand.nextInt(11);
            yDir = -20 + rand.nextInt(11);
            moveRight = false;
            moveDown = false;
        }

        //amount of pixels ball moves in horizontal direction
        double xSpeed = Math.abs(xDir);
        //amount of pixels ball moves in vertical direction
        double ySpeed = Math.abs(yDir);
        //amount of total pixels the ball moves per frame using pythagorean theorem
        speed = Math.sqrt((xSpeed*xSpeed)+(ySpeed*ySpeed));

        ballInPlay = true;
    }

    /**
     * sets the paddle size depending on the mode. If "Beginner" mode,
     * size of paddle increases. If "Expert" mode, size of paddle decreases
     *
     * @param size determine if game is set to beginner or expert mode
     */
    public void setPaddleSize(String size) {
        if(size.equalsIgnoreCase("beginner")) {
            //amount of space between the paddle and the edges of the
            //screen on the top and bottom
            paddleSize = 500;
        }
        else if(size.equalsIgnoreCase("expert")) {
            paddleSize = 600;
        }
    }

    /**
     * Draws the screen on each clock tick
     *
     * @param canvas the graphics object on which to draw
     */
    @Override
    public void tick(Canvas canvas) {
        /**
         External Citation
            Date:      20 March 2018
            Problem:   Did not know how to find the width and height of the
                        animation surface
            Resource:  https://stackoverflow.com/questions/3996850/
                       how-do-you-get-the-width-and-height-of-a-surface-view
            Solution: I used an example from this post
         */
        width = canvas.getWidth();
        height = canvas.getHeight();

        //draw walls of playing area
        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(50);
        canvas.drawRect(15, 0, width-15, 15, whitePaint);
        canvas.drawRect(width-15, 0, width, height, whitePaint);
        canvas.drawRect(15, height-15, width-15, height, whitePaint);
        //draw paddle
        canvas.drawRect(0, paddleSize, 15, height-paddleSize, whitePaint);

        /**
         External Citation
         Date:      20 March 2018
         Problem:   Did not know how to implement trig functions
         Resource:  https://stackoverflow.com/questions/30434088/
                    how-to-use-sine-cosine-log-function-in-android-studio
         Solution: I used an example from this post
         */

        //angle of the direction the ball is moving in
        double angle = Math.atan(yDir/xDir);

        //determine the direction the ball is moving in when it collides
        //with a certain wall, and determine the new direction of the
        //the ball using trig functions

        //ball collides with right wall
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
        //ball collides with paddle
        else if(xCoord-50 < 15 && xCoord-20 > 0 && yCoord > paddleSize
                && yCoord < height-paddleSize && !moveRight) {
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
        //ball collides with top wall
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
        //ball collides with bottom wall
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
        //ball leaves the playing area
        else if(xCoord+50 < 0) {
            ballInPlay = false;
            canvas.drawText("Tap screen for new ball", 760, 696, whitePaint);
        }

        //update coordinates of ball
        xCoord = xCoord+xDir;
        yCoord = yCoord+yDir;

        //draw ball in correct position
        canvas.drawCircle((int)xCoord, (int)yCoord, 50, whitePaint);

        //draw buttons on animation surface
        Paint buttonPaint = new Paint();
        buttonPaint.setColor(Color.argb(175, 150, 150, 150));
        canvas.drawRect(width/2 - 300, 50, width/2 - 50, 200, buttonPaint);
        canvas.drawRect(width/2 + 50, 50, width/2 + 300, 200, buttonPaint);

        //draw button text on animation surface
        Paint buttonText = new Paint();
        buttonText.setColor(Color.argb(175, 0, 0, 0));
        buttonText.setTextSize(45);
        canvas.drawText("Beginner", width/2 - 270, 140, buttonText);
        canvas.drawText("Expert", width/2 + 105, 140, buttonText);
    }

    /**
     * Determines what action to take when the screen is touched
     *
     * @param event a MotionEvent describing the touch
     */
    @Override
    public void onTouch(MotionEvent event) {
        int xPos; //x coordinate of event
        int yPos; //y coordinate of event

        /**
         External Citation
         Date:      20 March 2018
         Problem:   Did not know how to determine what type of action the
                    user performed
         Resource:  https://developer.android.com/reference/
                    android/view/MotionEvent.html
         Solution:  Used action descriptions from this reference
         */

        //Only update the game when the user initially touches the screen
        if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
            /**
             External Citation
             Date:      20 March 2018
             Problem:   Did not know how to get the coordinates of where the
                        user touched the screen
             Resource:  https://stackoverflow.com/questions/3476779/
                        how-to-get-the-touch-position-in-android
             Solution:  I used an example from this post
             */
            xPos = (int)event.getX();
            yPos = (int)event.getY();
            //Change game mode to beginner if user touches area
            // defined as "Beginner" button
            if(xPos > width/2-300 && xPos < width/2-50 && yPos > 50
                    && yPos < 200) {
                setPaddleSize("beginner");
            }
            //Change game mode to expert if user touches area
            // defined as "Expert" button
            else if(xPos > width/2+50 && xPos < width/2+300 && yPos > 50
                    && yPos < 200) {
                setPaddleSize("expert");
            }
            //If no ball is in play, user taps screen to create a new ball
            else if(!ballInPlay) {
                newBall();
            }
        }
    }
}
