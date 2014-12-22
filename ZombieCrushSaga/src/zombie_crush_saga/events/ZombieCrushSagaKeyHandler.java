package zombie_crush_saga.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static zombie_crush_saga.ZombieCrushSagaConstants.GAMEPLAY_SCREEN_STATE;
import zombie_crush_saga.data.ZombieCrushSagaDataModel;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;

/**
 * This event handler lets us provide additional custom responses
 * to key presses while ZombieCrushSaga is running.
 *
 * @author Richard McKenna
 */
public class ZombieCrushSagaKeyHandler extends KeyAdapter
{
    // THE ZombieCrushSaga GAME ON WHICH WE'LL RESPOND
    private ZombieCrushSagaMiniGame game;
    
    /**
     * This constructor simply inits the object by
     * keeping the game for later.
     *
     * @param initGame The ZombieCrushSaga game that contains
     * the back button.
     */
    public ZombieCrushSagaKeyHandler(ZombieCrushSagaMiniGame initGame)
    {
        game = initGame;
    }
    
    /**
     * This method provides a custom game response to when the user
     * presses a keyboard key.
     *
     * @param ke Event object containing information about the event,
     * like which key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent ke)
    {
        if (game.isCurrentScreenState(GAMEPLAY_SCREEN_STATE)){
            
            ZombieCrushSagaDataModel data = (ZombieCrushSagaDataModel)game.getDataModel();
            
            //if stuff is moving, don't do it.
            if(!data.hasMovingZombies()){
                
                if (ke.getKeyCode() == KeyEvent.VK_2)
                {
                    //set the board in a specific way
                    data.getBoardZombies().setBoardCheatTwo();
                    data.updateForce();
                }
                
                //press 4 to test four cheat.
                else if (ke.getKeyCode() == KeyEvent.VK_4)
                {
                    //set the board in a specific way
                    data.getBoardZombies().setBoardCheatFour();
                    data.updateForce();
                }
                
                //press 5 to test five in a row cheat
                else if (ke.getKeyCode() == KeyEvent.VK_5)
                {
                    //set the board in a specific way
                    data.getBoardZombies().setBoardCheatFive();
                    data.updateForce();
                }
                
                //press L to test L in a row cheat
                else if (ke.getKeyCode() == KeyEvent.VK_L)
                {
                    //set the board in a specific way
                    data.getBoardZombies().setBoardCheatL();
                    data.updateForce();
                }
                
                //press T to test T in a row cheat
                else if (ke.getKeyCode() == KeyEvent.VK_T)
                {
                    //set the board in a specific way
                    data.getBoardZombies().setBoardCheatT();
                    data.updateForce();
                }
                
                //press Q to set to loss
                else if (ke.getKeyCode() == KeyEvent.VK_Q)
                {
                    //sets moves to 1
                    data.setMoves(1);
                }
                
                //press W to set to get a column of five bombs to meld.
                else  if (ke.getKeyCode() == KeyEvent.VK_W)
                {
                    //set to win
                    data.getBoardZombies().setBoardCheatTwoFiveBombs();
                }
            }
            
        }
        
        //        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE C
        //        // KEY DOWN IT WILL CONTINUALLY CHEAT
        //        if (ke.getKeyCode() == KeyEvent.VK_C)
        //        {
        //            ZombieCrushSagaDataModel data = (ZombieCrushSagaDataModel)game.getDataModel();
        //
        //            // FIND A MOVE IF THERE IS ONE
        //            ZombieCrushSagaMove move = data.findMove();
        //            if (move != null)
        //                data.processMove(move);
        //        }
        //        // UNDO THE LAST MOVE, AGAIN, U CAN BE HELD DOWN
        //        else if (ke.getKeyCode() == KeyEvent.VK_U)
        //        {
        //            // UNDO IS ONLY RELEVANT TO THE GAME SCREEN
        //            if (game.isCurrentScreenState(GAMEPLAY_SCREEN_STATE))
        //            {
        //                ZombieCrushSagaDataModel data = (ZombieCrushSagaDataModel)game.getDataModel();
        //                data.undoLastMove();
        //            }
        //        }
        //
        
    }
}