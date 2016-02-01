package tsingularity.lolexplorer.Model;

import cupboard.annotation.CompositeIndex;
import cupboard.annotation.Index;

public class CurrentGameParticipant {

    @Index(indexNames = {@CompositeIndex(indexName = "indexCGParticipant")})
    public long matchId;
    @Index(indexNames = {@CompositeIndex(indexName = "indexCGParticipant", order = 2)})
    public long summonerId;

    public String  summonerName;
    public long    teamId;
    public int     championId;
    public int     spell1Id;
    public int     spell2Id;
    public boolean isBot;
    public String  masteries;

}
