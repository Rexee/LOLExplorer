package tsingularity.lolexplorer.Model.DTO.Game;

import java.util.List;

public class CurrentGameDTO {
    public List<BannedChampion> bannedChampions;
    public long                 gameId;
    public long                 gameLength;
    public String               gameMode;
    public int                  gameQueueConfigId;
    public long                 gameStartTime;
    public String               gameType;
    public long                 mapId;
    public Observer             observers;
    public List<Participant>    participants;
    public String               platformId;
}
