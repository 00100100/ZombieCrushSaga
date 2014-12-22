/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package audio_manager;

import javax.swing.JOptionPane;

/**
 *
 * @author McKillaGorilla
 */
public class Player
{
    public static void main(String[] args)
    {
        AudioManager audio = new AudioManager();
        try
        {
            audio.loadAudio("NA", "NA.mid");
            audio.play("NA", true);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getStackTrace());
        }        
    }
}
