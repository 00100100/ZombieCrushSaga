package mini_game;

import java.util.TimerTask;

/**
 * This simple class serves as the task executed each frame for updating, and
 * then rendering the game.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class MiniGameTimerTask extends TimerTask
{
    // WE'LL UPDATE THIS game OBJECT EACH FRAME
    protected MiniGame game;

    /**
     * Constructor for initializing the task, it simply stores away the game to
     * use in its updates.
     *
     * @param initGame the game to be updated and rendered each frame.
     */
    public MiniGameTimerTask(MiniGame initGame)
    {
        // STORE FOR LATER
        game = initGame;
    }

    /**
     * Called 30 times per second, or whatever the current frame rate is, this
     * method updates the game and renders it, making sure to get a lock on the
     * data before doing so and releasing the data when done.
     */
    @Override
    public void run()
    {
        try
        {
            // LOCK THE DATA
            game.beginUsingData();

            // UPDATE THE GAME
            game.update();

            // RENDER THE GAME
            game.getCanvas().repaint();
        } 
        finally
        {
            // RELEASE IT, SINCE THE OTHER THREAD
            // MIGHT WANT TO UPDATE STUFF IN RESPONSE
            // TO A MOUSE CLICK
            game.endUsingData();
        }
    }
}