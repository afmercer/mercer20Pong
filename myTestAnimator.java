package edu.up.cs301.mercer20.mercer20pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import java.util.Random;

/**
 * class that animates a ball and 2 paddles, 1 controlled by a human, the other
 * controlled by the computer. When the ball leaves the screen, the score is updated, and
 * a new ball is generated from the center with a random speed and random direction.
 *
 * @author Adam Mercer
 * @version March 2018
 */

public class myTestAnimator implements Animator {

    //instance variables
    private int width; //width of animation surface
    private int height; //height of animation surface
    private int xDir; //amount of pixels ball moves in horizontal direction per frame
    private int yDir; //amount of pixels ball moves in vertical direction per frame
    private int xCoord; //x coordinate of the center of the ball
    private int yCoord; //y coordinate of center of ball
    private boolean moveRight; //whether ball is moving right
    private boolean moveDown; //whether the ball is moving down
    private boolean ballInPlay; //whether there is a ball on the screen
    private int paddleSize;
    private int paddleCenter; //determines y coordinate of the center of paddle
    private int compPaddleCenter;
    private int compPaddleSpeed; //determines max number of pixels computer paddle moves per frame
    private int xStartTouch; //determines x coordinate where user started to touch the screen
    private int yStartTouch; //determines x coordinate where user started to touch the screen
    private int playerScore;
    private int compScore;
    private boolean updateScore; //whether or not score has been updated since ball exited screen
    private boolean gameOver;

    /**
     * Constructor, creates a new game when a new test begins
     */
    public myTestAnimator() {
        newGame();
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
     * Tells that the animation never quits.
     *
     * @return indication of whether to quit.
     */
    @Override
    public boolean doQuit() {
        return false;
    }

    /**
     * initializes variables to represent the start of a new game,
     */
    public void newGame() {
        playerScore = 0;
        compScore = 0;
        paddleSize = 330;
        paddleCenter = 696;
        gameOver = false;
        newBall();
    }

    /**
     * Creates a new ball at a random position with a random speed,
     * and a random direction
     */
    public void newBall() {
        Random rand = new Random(); //used to generate random values

        //create new ball in the center of the screen
        xCoord = 1024;
        yCoord = 696;

        //send ball in a random direction at a random speed
        int direction = rand.nextInt(4);
        //ball goes up and right
        if(direction == 0) {
            //ball moves between 50-60 pixels in the
            //horizontal and vertical direction
            xDir = 50 + rand.nextInt(11);
            yDir = 50 + rand.nextInt(11);
            moveRight = true;
            moveDown = true;
        }
        //ball goes up and left
        else if(direction == 1) {
            xDir = -60 + rand.nextInt(11);
            yDir = 50 + rand.nextInt(11);
            moveRight = false;
            moveDown = true;
        }
        //ball goes down and right
        else if(direction == 2) {
            xDir = 50 + rand.nextInt(11);
            yDir = -60 + rand.nextInt(11);
            moveRight = true;
            moveDown = false;
        }
        //ball goes down and left
        else if(direction == 3) {
            xDir = -60 + rand.nextInt(11);
            yDir = -60 + rand.nextInt(11);
            moveRight = false;
            moveDown = false;
        }

        //computer paddle max speed initialized to 85%
        // of initial vertical speed of ball
        compPaddleSpeed = (int)(0.85*Math.abs(yDir));

        compPaddleCenter = yCoord;
        ballInPlay = true;
        updateScore = false;
    }

    /**
     * sets the paddle size depending on the mode. If "Beginner" mode,
     * size of paddle increases. If "Expert" mode, size of paddle decreases
     *
     * @param size determine if game is set to beginner or expert mode
     */
    public void setPaddleSize(String size) {
        if(size.equalsIgnoreCase("beginner")) {
            paddleSize = 330; //paddle is 330 pixels wide
        }
        else if(size.equalsIgnoreCase("expert")) {
            paddleSize = 210;
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
        canvas.drawRect(15, height-15, width-15, height, whitePaint);

        //check if the ball collided with a wall or paddle
        collision(canvas, whitePaint);

        //update coordinates of ball
        xCoord = xCoord+xDir;
        yCoord = yCoord+yDir;

        //draw computer paddle
        drawComputerPaddle(canvas, yCoord, yDir, whitePaint);

        //draw ball in correct position
        canvas.drawCircle(xCoord, yCoord, 45, whitePaint);

        //draw human paddle
        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        Paint redPaint = new Paint();
        redPaint.setColor(Color.RED);
        canvas.drawRect(0, paddleCenter-paddleSize/2, 15, paddleCenter+paddleSize/2, greenPaint);
        canvas.drawRect(0, paddleCenter-paddleSize/5, 15, paddleCenter+paddleSize/5, redPaint);

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

        //draw score on animation surface
        Paint scoreText = new Paint();
        scoreText.setColor(Color.argb(175, 255, 255, 255));
        scoreText.setTextSize(80);
        canvas.drawText(playerScore+"  :  "+compScore, width/2 - 100, height - 140, scoreText);
    }

    /**
     * determine which wall the ball collides with, and
     * change the direction of the ball accordingly
     *
     * @param canvas the graphics object on which to draw
     * @param ballPaint paint to draw the ball with
     *
     */
    public void collision(Canvas canvas, Paint ballPaint) {
        //ball collides with computer paddle
        if(xCoord+45 > width - 15 && xCoord - 40 < width && yCoord > compPaddleCenter-105
                && yCoord < compPaddleCenter+105 && moveRight) {
            xDir = -xDir;
            moveRight = false;
        }
        //ball collides with player paddle
        else if(xCoord-45 < 15 && xCoord + 40 > 0 && yCoord > paddleCenter-paddleSize/2
                && yCoord < paddleCenter+paddleSize/2 && !moveRight) {
            //make sure ball does not move slower than 33 pixels in horizontal direction or
            //35 pixels in vertical direction
            if(Math.abs(xDir) < 33 || Math.abs(yDir) < 35) {
                xDir = -xDir;
            }
            //check if ball hits green part of paddle and increase speed of ball
            else if(yCoord >= paddleCenter+paddleSize/5 || yCoord <= paddleCenter-paddleSize/5) {
                xDir = -(int)(1.05*xDir);
                yDir = (int)(1.12*yDir);
            }
            //check if ball hits red part of paddle and decrease speed of ball
            else if(yCoord < paddleCenter+paddleSize/5 && yCoord > paddleCenter-paddleSize/5) {
                xDir = -(int)(0.95*xDir);
                yDir = (int)(0.9*yDir);
            }
            moveRight = true;
        }
        //ball collides with top wall
        else if(yCoord-45 <= 15 && !moveDown) {
            yDir = -yDir;
            moveDown = true;
        }
        //ball collides with bottom wall
        else if(yCoord+45 >= height-15 && moveDown) {
            yDir = -yDir;
            moveDown = false;
        }
        //ball leaves the playing area and updates score by calling ballExitsScreen method
        else if(xCoord+45 < 0) {
            compScore = ballExitsScreen(canvas, compScore, ballPaint);
        }
        else if(xCoord-45 > width) {
            playerScore = ballExitsScreen(canvas, playerScore, ballPaint);
        }
    }

    /**
     * update the state of the game when the ball exits the screen
     *
     * @param canvas the graphics object on which to draw
     * @param changeScore the score to update
     * @param textPaint the paint to text
     * @return the updated score
     */
    public int ballExitsScreen(Canvas canvas, int changeScore, Paint textPaint) {
        ballInPlay = false;
        //check if score has been updated already
        if(!updateScore) {
            changeScore++;
            updateScore = true;
        }
        //check if the game is over
        if(changeScore == 10) {
            canvas.drawText("Game Over, tap screen to start a new game", 600, 696, textPaint);
            gameOver = true;
        }
        else{
            canvas.drawText("Tap screen for new ball", 760, 696, textPaint);
        }
        return changeScore;
    }

    /**
     * update the position of the computer paddle and draw it on the screen
     *
     * @param canvas the graphics object on which to draw
     * @param yCoordBall the y coordinate of the ball
     * @param ySpeed the vertical speed of the ball
     * @param paddlePaint the paint to draw the paddle with
     */
    public void drawComputerPaddle(Canvas canvas, int yCoordBall, int ySpeed, Paint paddlePaint) {

        if(!ballInPlay) { //paddle doesn't move if ball is not in play
            canvas.drawRect(width - 15, compPaddleCenter + 105, width,
                    compPaddleCenter - 105, paddlePaint);
        }
        //check if the ball is further away than the max speed allows the paddle to move
        else if(yCoordBall + ySpeed > compPaddleCenter + compPaddleSpeed) {
            //move paddle according to its max speed
            compPaddleCenter = compPaddleCenter + compPaddleSpeed;
            canvas.drawRect(width - 15, compPaddleCenter + 105, width,
                    compPaddleCenter - 105, paddlePaint);
        }
        //check if the ball is further away than the max speed allows the paddle to move
        else if(yCoordBall + ySpeed < compPaddleCenter - compPaddleSpeed) {
            //move paddle according to its max speed
            compPaddleCenter = compPaddleCenter - compPaddleSpeed;
            canvas.drawRect(width - 15, compPaddleCenter + 105, width,
                    compPaddleCenter - 105, paddlePaint);
        }
        else { //move paddle at the same speed the ball is moving
            compPaddleCenter = yCoordBall + ySpeed;
            canvas.drawRect(width-15, compPaddleCenter + 105, width,
                    compPaddleCenter - 105, paddlePaint);
        }
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

        //actions to be made when the user initially presses down on the screen
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
            xStartTouch = xPos;
            yStartTouch = yPos;

            if(gameOver) {
                newGame();
            }
            //Change game mode to beginner if user touches area
            // defined as "Beginner" button
            else if(xPos > width/2-300 && xPos < width/2-50 && yPos > 50
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
            //user is controlling the paddle
            else {
                paddleCenter = yPos;
            }
        }
        //action to be taken when user drags finger across screen
        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            yPos = (int)event.getY();
            if(xStartTouch > width/2-300 && xStartTouch < width/2-50 &&
                    yStartTouch > 50 && yStartTouch < 200) {
                //user started the touch inside "Beginner" button, do nothing
            }
            else if(xStartTouch > width/2+50 && xStartTouch < width/2+300 &&
                    yStartTouch > 50 && yStartTouch < 200) {
                //user started the touch inside "Expert" button, do nothing
            }
            //prevent user from moving paddle off the screen
            else if(yPos - paddleSize/2 <= 0) {
                paddleCenter = paddleSize/2;
            }
            //prevent user from moving paddle off the screen
            else if(yPos + paddleSize/2 >= height) {
                paddleCenter = height - paddleSize/2;
            }
            //user is controlling the paddle
            else {
                paddleCenter = yPos;
            }
        }
    }
}
