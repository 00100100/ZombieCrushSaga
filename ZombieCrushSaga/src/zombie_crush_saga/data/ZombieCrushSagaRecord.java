package zombie_crush_saga.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents the complete playing history for the player
 * since originally starting the application. Note that it stores
 * stats separately for different levels.
 * 
 * @author Richard McKenna
 */
public class ZombieCrushSagaRecord
{
    // HERE ARE ALL THE RECORDS
    private HashMap<String, ZombieCrushSagaLevelRecord> levelRecords;

    /**
     * Default constructor, it simply creates the hash table for
     * storing all the records stored by level.
     */
    public ZombieCrushSagaRecord()
    {
        levelRecords = new HashMap<String, ZombieCrushSagaLevelRecord>();
    }

    // GET METHODS
        // - getGamesPlayed
        // - getWins
        // - getLosses
        // - getFastestTime
    
    /**
     * This method gets the games played for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The number of games played for the levelName level.
     */
    public int getGamesPlayed(String levelName) 
    {
        ZombieCrushSagaLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE GAMES PLAYED
        else
            return rec.gamesPlayed; 
    }
    
    /**
     * This method gets the games played for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The highscore for the levelName level.
     */
    public int getHighScore(String levelName) 
    {
        ZombieCrushSagaLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE high score
        else
            return rec.highScore;
    }
    
    /**
     * sets the records to null.
     */
    public void reset(){
        levelRecords = new HashMap<String, ZombieCrushSagaLevelRecord>();
    }

    // ADD METHODS
        // -addZombieCrushSagaLevelRecord
        // -addWin
        // -addLoss
    
    /**
     * Adds the record for a level
     * 
     * @param levelName
     * 
     * @param rec 
     */
    public void addZombieCrushSagaLevelRecord(String levelName, ZombieCrushSagaLevelRecord rec)
    {
        levelRecords.put(levelName, rec);
    }
    
    /**
     * This method adds a win to the current player's record according
     * to the level being played.
     * 
     * @param levelName The level being played that the player won.
     * 
     * @param winTime The time it took to win the game.
     */
    public void addWin(String levelName, int theScore)
    {
        // GET THE RECORD FOR levelName
        ZombieCrushSagaLevelRecord rec = levelRecords.get(levelName);
        
        // IF THE PLAYER HAS NEVER PLAYED A GAME ON levelName
        if (rec == null)
        {
            // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
            // THE FIRST TIME WE'VE PLAYED IT
            rec = new ZombieCrushSagaLevelRecord();
            rec.gamesPlayed = 1;
            rec.highScore = theScore;
            levelRecords.put(levelName, rec);
        }
        else
        {
            // WE'VE PLAYED THIS LEVEL BEFORE, SO SIMPLY
            // UPDATE THE STATS
            rec.gamesPlayed++;
            if (theScore > rec.highScore)
                rec.highScore = theScore;
        }
    }
    
    /**
     * This method constructs and fills in a byte array with all the
     * necessary data stored by this object. We do this because writing
     * a byte array all at once to a file is fast. Certainly much faster
     * than writing to a file across many write operations.
     * 
     * @return A byte array filled in with all the data stored in this
     * object, which means all the player records in all the levels.
     * 
     * @throws IOException Note that this method uses a stream that
     * writes to an internal byte array, not a file. So this exception
     * should never happen.
     */
    public byte[] toByteArray() throws IOException
    {
        Iterator<String> keysIt = levelRecords.keySet().iterator();
        int numLevels = levelRecords.keySet().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numLevels);
        while(keysIt.hasNext())
        {
            String key = keysIt.next();
            dos.writeUTF(key);
            ZombieCrushSagaLevelRecord rec = levelRecords.get(key);
            dos.writeInt(rec.gamesPlayed);
            dos.writeInt(rec.highScore);
        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}    