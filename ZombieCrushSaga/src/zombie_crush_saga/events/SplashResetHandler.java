package zombie_crush_saga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;

/**
 * This class manages when the user clicks the splash's reset button
 * 
 * @author Richard McKenna
 */
public class SplashResetHandler implements ActionListener
{
    private ZombieCrushSagaMiniGame miniGame;
    
    public SplashResetHandler(ZombieCrushSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }
    
    /**
     * This method is called when the user clicks the splash's reset button.
     * 
     * @param ae the event object for the button press
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // WE ONLY LET THIS HAPPEN IF THE SPLASH/SAGA SCREEN IS VISIBLE
        if (miniGame.isCurrentScreenState(SPLASH_SCREEN_STATE)){
            
            //popup a yes no box; msg, title, 0 = yes/no option
            int choice = JOptionPane.showConfirmDialog (null, "Are you sure you want to erase your data?"
                    ,"Zombie Crush Saga", 0);
            
            //yes
            if(choice == 0){
                miniGame.getPlayerRecord().reset();
                miniGame.getFileManager().saveRecord();
            }
        }
    }
}