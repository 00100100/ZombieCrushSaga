package mini_game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Some basic event handling is provided by this class for this framework.
 * Specifically, mouse button presses on Sprites and the playing surface. Note
 * that this class makes sure to get a lock on game data using the Game class'
 * beginUsingData method, and releases the lock using the endUsingData. Game
 * developers using this framework must do the same when adding additional event
 * handlers.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class MiniGameEventRelayer implements MouseListener, MouseMotionListener, KeyListener
{
    // THE GAME FROM WHICH THE EVENTS WERE GENERATED
    private MiniGame game;

    /**
     * The constructor just sets up access to the game for which to respond to.
     *
     * @param initGame the game being played.
     */
    public MiniGameEventRelayer(MiniGame initGame)
    {
        game = initGame;
    }

    /**
     * This method will test to see if the mouse press was on one of the game
     * developer's registered buttons, and if it is, the appropriate event
     * handler will be executed. If not, the game program's custom response will
     * be executed in the appropriate data model class. Note that this response
     * makes sure to get a lock on the data before use.
     *
     * @param me the event object, it contains information about the user
     * interaction, like the x,y coordinates of the mouse button press.
     */
    @Override
    public void mousePressed(MouseEvent me)
    {
        try
        {
            // LOCK THE DATA
            game.beginUsingData();

            // GET THE COORDINATES
            int x = me.getX();
            int y = me.getY();

            // FIRST CHECK THE GUI BUTTONS
            boolean buttonClicked = game.processButtonPress(x, y);

            // IF IT WAS NOT A GUI BUTTON, THEN WE SHOULD
            // EXECUTE THE CUSTOM GAME RESPONSE
            if (!buttonClicked && game.getDataModel().inProgress())
            {
                MiniGameDataModel data = game.getDataModel();
                data.checkMousePressOnSprites(game, x, y);
            }
        } finally
        {
            // RELEASE THE DATA SO THAT THE TIMER THREAD MAY
            // APPROPRIATELY UPDATE AND RENDER THE GAME
            // WITHOUT INTERFERENCE
            game.endUsingData();
        }
    }

    // WE WILL NOT USE THESE METHODS
    @Override
    public void mouseEntered(MouseEvent me)
    {
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
    }

    @Override
    public void mouseReleased(MouseEvent me)
    {
        try
        {
            // LOCK THE DATA
            game.beginUsingData();
            
            // GET THE COORDINATES
            int x = me.getX();
            int y = me.getY();
            
            MiniGameDataModel data = game.getDataModel();
            data.checkMouseDragOnSprites(game, 0, 0, false);
            
        } finally
        {
            // RELEASE THE DATA SO THAT THE TIMER THREAD MAY
            // APPROPRIATELY UPDATE AND RENDER THE GAME
            // WITHOUT INTERFERENCE
            game.endUsingData();
        }
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
    }

    /**
     * This guy demonstrates how to get pixel information about about a
     * particular location of what's being drawing inside a panel.
     *
     * @param me Location of mouse click in canvas.0
     */
    @Override
    public void mouseMoved(MouseEvent me)
    {
        try
        {
            game.beginUsingData();
            game.getDataModel().setLastMouseX(me.getX());
            game.getDataModel().setLastMouseY(me.getY());
            game.getDataModel().updateDebugText(game);
        } finally
        {
            game.endUsingData();
        }
    }

    // WE'RE NOT USING THIS GUY AT THE MOMENT, THOUGH YOU MAY CHOOSE TO
    @Override
    public void mouseDragged(MouseEvent me)
    {
        
        try
        {
            // LOCK THE DATA
            game.beginUsingData();

            // GET THE COORDINATES
            int x = me.getX();
            int y = me.getY();

            // FIRST CHECK THE GUI BUTTONS
           // boolean buttonClicked = game.processButtonPress(x, y);

            // IF IT WAS NOT A GUI BUTTON, THEN WE SHOULD
            // EXECUTE THE CUSTOM GAME RESPONSE
            if ( game.getDataModel().inProgress()) //&& buttonClicked
            {
                MiniGameDataModel data = game.getDataModel();
                data.checkMouseDragOnSprites(game, x, y, true);
            }
        } finally
        {
            // RELEASE THE DATA SO THAT THE TIMER THREAD MAY
            // APPROPRIATELY UPDATE AND RENDER THE GAME
            // WITHOUT INTERFERENCE
            game.endUsingData();
        }
    }

    /**
     * This key handler simply activates our debug text display, which may then
     * be rendered by the canvas.
     *
     * @param ke the event object, it contains information about the user
     * interaction, like which key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent ke)
    {
        try
        {
            game.beginUsingData();

            // THE 'D' KEY TOGGLES DEBUG TEXT DISPLAY
            if (ke.getKeyCode() == KeyEvent.VK_D)
            {
                // TOGGLE IT OFF
                if (game.getDataModel().isDebugTextRenderingActive())
                {
                    game.getDataModel().deactivateDebugTextRendering();
                } // TOGGLE IT ON
                else
                {
                    game.getDataModel().activateDebugTextRendering();
                }
            } // THE 'P' KEY PAUSES THE GAME, WHICH MEANS 
            // ALL UPDATE LOGIC GETS SKIPPED
            else if (ke.getKeyCode() == KeyEvent.VK_P)
            {
                // TOGGLE THE OFF
                if (game.getDataModel().isPaused())
                {
                    game.getDataModel().unpause();
                } // TOGGLE IT ON
                else
                {
                    game.getDataModel().pause();
                }
            } else
            {
                game.getKeyHandler().keyPressed(ke);
            }
        } finally
        {
            game.endUsingData();
        }
    }

    // WE WILL NOT USE THESE METHODS
    @Override
    public void keyReleased(KeyEvent ke)
    {
    }

    @Override
    public void keyTyped(KeyEvent ke)
    {
    }
}