/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package audio_manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author McKillaGorilla
 */
public class AudioManager
{
    public enum AudioManagerFileType
    {
        WAV,
        MP3,
        MIDI
    }
    
    private HashMap<String, Sequencer>      midiAudio;
    private HashMap<String, Clip>           wavAudio;
   // private HashMap<String, MediaPlayer>    mp3Audio;

    public AudioManager() 
    {
        midiAudio = new HashMap();
        wavAudio = new HashMap();
   //     mp3Audio = new HashMap();
    }
    
    public void loadAudio(String audioName, String audioFileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InvalidMidiDataException, MidiUnavailableException
    {
        if (audioFileName.endsWith(".wav"))
        {
            File soundFile = new File(audioFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
            
            // load the sound into memory (a Clip)
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(sound);
            wavAudio.put(audioName, clip);
        }
        else if (audioFileName.endsWith(".mid"))
        {
            Sequence sequence = MidiSystem.getSequence(new File(audioFileName));
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            midiAudio.put(audioName, sequencer);
        }
    }
    
    public void play(String audioName, boolean loop)
    {
        Sequencer sequencer = midiAudio.get(audioName);
        if (sequencer != null)
        {
            sequencer.setTickPosition(0);
            sequencer.start();
        }
        else
        {
            Clip clip = wavAudio.get(audioName);
            clip.setFramePosition(0);
            clip.start();
        }   
    }
    
    public boolean isPlaying(String audioName)
    {
        Sequencer sequencer = midiAudio.get(audioName);
        if (sequencer != null)
        {
            return sequencer.isRunning();
        }
        else
        {
            Clip clip = wavAudio.get(audioName);  
            return clip.isRunning();
        }
    }
    
    public void stop(String audioName)
    {
        Sequencer sequencer = midiAudio.get(audioName);
        if (sequencer != null)
        {
            sequencer.stop();
        }
        else
        {
            Clip clip = wavAudio.get(audioName);
            clip.stop();
        }
    }
}