package tsingularity.lolexplorer.Model.DTO.Game;

import java.util.List;

import tsingularity.lolexplorer.Model.Constants.QueueSubType;

public class Game {
    public int          championId;
    public long         createDate;
    public List<Player> fellowPlayers;
    public long         gameId;
    public String       gameMode;
    public String       gameType;
    public boolean      invalid;
    public int          ipEarned;
    public int          level;
    public int          mapId;
    public int          spell1;
    public int          spell2;
    public RawStats     stats;
    public QueueSubType subType;
    public int          teamId;
}
