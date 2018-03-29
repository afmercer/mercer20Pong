package edu.up.cs301.mercer20.mercer20pong;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;

/**
 * PongMainActivity
 *
 * This is the activity for the Pong game. It attaches a PongAnimator to
 * an AnimationSurface.
 *
 * When a ball leaves the area of play, the user must tap the screen for a new
 * ball to appear. Two buttons located at the top of the screen change the size
 * of the paddle. "Beginner" button makes paddle larger, "Expert" button makes
 * paddle smaller.
 *
 * The game is played between a human and a computer. The game ends when one
 * player reaches 10 points. A point is scored when the ball leaves the
 * opponent's side of the screen. The human paddle has a red area and green area.
 * If the ball is hit in the green area, the ball will bounce back with a slightly
 * greater speed, and a slightly steeper angle. If the ball hits the red area, the
 * ball will bounce back with slightly less speed, and at a slightly shallower angle.
 * The user should try to hit the ball with the green part of the paddle in order
 * to make the computer miss.
 *
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @author Adam Mercer
 * @version March 2018
 *
 */
public class MainActivity extends Activity {

    /**
     * creates an AnimationSurface containing a Test Animator.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         External Citation
            Date:      20 March 2018
            Problem:   Did not know how the lock the orientation of the display
            Resource:  https://stackoverflow.com/questions/2941561/
                        how-do-i-lock-the-layout-of-my-android-
                        program-to-one-orientation
             Solution: I used an example from this post
         */
        super.setRequestedOrientation
                (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.pong_main);

        // Connect the animation surface with the animator
        AnimationSurface mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        mySurface.setAnimator(new myTestAnimator());
    }
}
