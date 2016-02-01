package tsingularity.lolexplorer.Model;

import cupboard.annotation.CompositeIndex;
import cupboard.annotation.Index;

public class MatchParticipant {

    @Index(indexNames = {@CompositeIndex(indexName = "indexParticipant")})
    public long matchId;
    @Index(indexNames = {@CompositeIndex(indexName = "indexParticipant", order = 2)})
    public long summonerId;

    public int participantId;
    public int teamId;

    public int    championId;
    public String summonerName;

    public int    spell1Id;
    public int    spell2Id;
    public String highestAchievedSeasonTier;

    public int item0;
    public int item1;
    public int item2;
    public int item3;
    public int item4;
    public int item5;
    public int item6;

    public long kills;
    public long deaths;
    public long assists;

    public long champLevel;
    public long goldEarned;
    public long totalDamageDealtToChampions;

    public long wardsPlaced;

    public long totalCreepsKilled;

    public long pentaKills;
    public long quadraKills;
    public long unrealKills;
}
