package tsingularity.lolexplorer.Model;

import cupboard.annotation.CompositeIndex;
import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.DTO.Stats.ChampionStats;

public class StatsRanked {

    //    public Long _id;
    @Index(indexNames = {@CompositeIndex(indexName = "indexStatsRanked")})
    public long summonerId;
    @Index(indexNames = {@CompositeIndex(indexName = "indexStatsRanked", order = 2)})
    public int  championId;

    public int totalFirstBlood;

    public int totalDamageDealt;
    public int totalDamageTaken;
    public int totalHeal;

    public int totalGoldEarned;
    public int totalTurretsKilled;
    public int totalMinionKills;

    public int totalQuadraKills;
    public int totalPentaKills;

    public int totalSessionsPlayed;
    public int totalSessionsWon;

    public int totalKills;
    public int totalDeaths;
    public int totalAssists;

    public float avgKDA;


    public static StatsRanked fromDTO(long summonerId, long modifyDate, ChampionStats entry) {

        StatsRanked statsRanked = new StatsRanked();
        statsRanked.summonerId = summonerId;

        statsRanked.championId = entry.id;
        statsRanked.totalAssists = entry.stats.totalAssists;
        statsRanked.totalDamageDealt = entry.stats.totalDamageDealt;
        statsRanked.totalDamageTaken = entry.stats.totalDamageTaken;
        statsRanked.totalFirstBlood = entry.stats.totalFirstBlood;
        statsRanked.totalGoldEarned = entry.stats.totalGoldEarned;
        statsRanked.totalHeal = entry.stats.totalHeal;
        statsRanked.totalMinionKills = entry.stats.totalMinionKills;
        statsRanked.totalPentaKills = entry.stats.totalPentaKills;
        statsRanked.totalQuadraKills = entry.stats.totalQuadraKills;
        statsRanked.totalSessionsPlayed = entry.stats.totalSessionsPlayed;
        statsRanked.totalSessionsWon = entry.stats.totalSessionsWon;
        statsRanked.totalTurretsKilled = entry.stats.totalTurretsKilled;
        statsRanked.totalKills = entry.stats.totalChampionKills;
        statsRanked.totalDeaths = entry.stats.totalDeathsPerSession;

        if (statsRanked.totalAssists == 0) statsRanked.avgKDA = 0;
        else statsRanked.avgKDA = (float) (statsRanked.totalKills + statsRanked.totalAssists) / statsRanked.totalDeaths;

        return statsRanked;
    }
}
