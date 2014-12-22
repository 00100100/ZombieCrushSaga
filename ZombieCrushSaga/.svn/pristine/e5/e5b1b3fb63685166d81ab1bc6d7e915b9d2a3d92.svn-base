package mahjong_solitaire.data;

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
public class MahjongSolitaireRecord
{
    // HERE ARE ALL THE RECORDS
    private HashMap<String, MahjongLevelRecord> levelRecords;

    /**
     * Default constructor, it simply creates the hash table for
     * storing all the records stored by level.
     */
    public MahjongSolitaireRecord()
    {
        levelRecords = new HashMap();
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
        MahjongLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE GAMES PLAYED
        else
            return rec.gamesPlayed; 
    }

    /**
     * This method gets the wins for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The wins the player has earned for the levelName level.
     */    
    public int getWins(String levelName)
    {
        MahjongLevelRecord rec = levelRecords.get(levelName);
        
        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0        
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE WINS
        else
            return rec.wins; 
    }
    
    /**
     * This method gets the losses for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The losses the player has earned for the levelName level.
     */      
    public int getLosses(String levelName)
    {
        MahjongLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE LOSSES
        else
            return rec.losses; 
    }
    
    /**
     * This method gets the fastest time for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The fastest time the player has earned for the levelName level.
     */       
    public long getFastestTime(String levelName)
    {
        MahjongLevelRecord rec = levelRecords.get(levelName);
        
        // IF THE PLAYER HAS NEVER PLAYED THAT LEVEL, RETURN
        // THE MAX AS A  FLAG
        if (rec == null)
            return Long.MAX_VALUE;
        // OTHERWISE RETURN THE FASTEST TIME
        else
            return rec.fastestTime; 
    }

    // ADD METHODS
        // -addMahjongLevelRecord
        // -addWin
        // -addLoss
    
    /**
     * Adds the record for a level
     * 
     * @param levelName
     * 
     * @param rec 
     */
    public void addMahjongLevelRecord(String levelName, MahjongLevelRecord rec)
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
    public void addWin(String levelName, long winTime)
    {
        // GET THE RECORD FOR levelName
        MahjongLevelRecord rec = levelRecords.get(levelName);
        
        // IF THE PLAYER HAS NEVER PLAYED A GAME ON levelName
        if (rec == null)
        {
            // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
            // THE FIRST TIME WE'VE PLAYED IT
            rec = new MahjongLevelRecord();
            rec.gamesPlayed = 1;
            rec.wins = 1;
            rec.losses = 0;
            rec.fastestTime = winTime;
            levelRecords.put(levelName, rec);
        }
        else
        {
            // WE'VE PLAYED THIS LEVEL BEFORE, SO SIMPLY
            // UPDATE THE STATS
            rec.gamesPlayed++;
            rec.wins++;
            if (winTime < rec.fastestTime)
                rec.fastestTime = winTime;
        }
    }
    
    /**
     * This method adds a loss to the current player's record according
     * to the level being played.
     * 
     * @param levelName The level being played that the player lost.
     */
    public void addLoss(String levelName)
    {
        // GET THE RECORD FOR levelName
        MahjongLevelRecord rec = levelRecords.get(levelName);

        // IF THE PLAYER HAS NEVER PLAYED A GAME ON levelName
        if (rec == null)
        {
            // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
            // THE FIRST TIME WE'VE PLAYED IT
            rec = new MahjongLevelRecord();
            rec.gamesPlayed = 1;
            rec.wins = 0;
            rec.losses = 1;
            rec.fastestTime = Long.MAX_VALUE;
            levelRecords.put(levelName, rec);
        }
        else
        {
            // WE'VE PLAYED THIS LEVEL BEFORE, SO SIMPLY
            // UPDATE THE STATS
            rec.gamesPlayed++;
            rec.losses++;
        }
    }
    
    // ADDITIONAL SERVICE METHODS
        // -calculateWinPercentage
        // -toByteArray

    /**
     * This method calculates and returns the player's win
     * percentage for levelName.
     * 
     * @param levelName The level for which to retreive 
     * the win percentage.
     * 
     * @return The win percentage for levelName.
     */
    public double calculateWinPercentage(String levelName)
    {
        // GET THE RECORD FOR leveName
        MahjongLevelRecord rec = levelRecords.get(levelName);
        
        // IF NO GAMES HAVE BEEN PLAYED RETURN 0
        if (rec == null)
            return 0.0;
        // OTHERWISE CALCULATE AND RETURN THE WIN PERCENTAGE
        else
            return (double)rec.wins/(double)rec.gamesPlayed;
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
            MahjongLevelRecord rec = levelRecords.get(key);
            dos.writeInt(rec.gamesPlayed);
            dos.writeInt(rec.wins);
            dos.writeInt(rec.losses);
            dos.writeLong(rec.fastestTime);
        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}    