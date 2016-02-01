package tsingularity.lolexplorer.Model;

import cupboard.annotation.CompositeIndex;
import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.GameType;
import tsingularity.lolexplorer.Model.DTO.Stats.PlayerStatsSummary;

public class StatsSummary {

    //    public Long _id;
    @Index(indexNames = {@CompositeIndex(indexName = "indexStatsSummary")})
    public long summonerId;
    @Index(indexNames = {@CompositeIndex(indexName = "indexStatsSummary", order = 2)})
    public int  playerStatSummaryTypeId;
    public long modifyDate;
    public int  losses;
    public int  wins;
    public int  maxNumDeaths;
    public int  totalDeathsPerSession;
    public int  botGamesPlayed;
    public int  killingSpree;
    public int  maxChampionsKilled;
    public int  maxLargestCriticalStrike;
    public int  maxLargestKillingSpree;
    public int  maxTimePlayed;
    public int  maxTimeSpentLiving;
    public int  mostChampionKillsPerSession;
    public int  mostSpellsCast;
    public int  normalGamesPlayed;
    public int  rankedPremadeGamesPlayed;
    public int  rankedSoloGamesPlayed;
    public int  totalAssists;
    public int  totalChampionKills;
    public int  totalDamageDealt;
    public int  totalDamageTaken;
    public int  totalDoubleKills;
    public int  totalFirstBlood;
    public int  totalGoldEarned;
    public int  totalHeal;
    public int  totalMagicDamageDealt;
    public int  totalMinionKills;
    public int  totalNeutralMinionsKilled;
    public int  totalPentaKills;
    public int  totalPhysicalDamageDealt;
    public int  totalQuadraKills;
    public int  totalSessionsLost;
    public int  totalSessionsPlayed;
    public int  totalSessionsWon;
    public int  totalTripleKills;
    public int  totalTurretsKilled;
    public int  totalUnrealKills;

    public static StatsSummary fromDTO(long summonerId, PlayerStatsSummary entry) {

        StatsSummary statsRanked = new StatsSummary();
        statsRanked.summonerId = summonerId;
        statsRanked.modifyDate = entry.modifyDate;
        statsRanked.playerStatSummaryTypeId = GameType.getValue(entry.playerStatSummaryType);
        statsRanked.losses = entry.losses;
        statsRanked.wins = entry.wins;

        statsRanked.maxNumDeaths = entry.aggregatedStats.maxNumDeaths;
        statsRanked.totalDeathsPerSession = entry.aggregatedStats.totalDeathsPerSession;
        statsRanked.botGamesPlayed = entry.aggregatedStats.botGamesPlayed;
        statsRanked.killingSpree = entry.aggregatedStats.killingSpree;
        statsRanked.maxChampionsKilled = entry.aggregatedStats.maxChampionsKilled;
        statsRanked.maxLargestCriticalStrike = entry.aggregatedStats.maxLargestCriticalStrike;
        statsRanked.maxLargestKillingSpree = entry.aggregatedStats.maxLargestKillingSpree;
        statsRanked.maxTimePlayed = entry.aggregatedStats.maxTimePlayed;
        statsRanked.maxTimeSpentLiving = entry.aggregatedStats.maxTimeSpentLiving;
        statsRanked.mostChampionKillsPerSession = entry.aggregatedStats.mostChampionKillsPerSession;
        statsRanked.mostSpellsCast = entry.aggregatedStats.mostSpellsCast;
        statsRanked.normalGamesPlayed = entry.aggregatedStats.normalGamesPlayed;
        statsRanked.rankedPremadeGamesPlayed = entry.aggregatedStats.rankedPremadeGamesPlayed;
        statsRanked.rankedSoloGamesPlayed = entry.aggregatedStats.rankedSoloGamesPlayed;
        statsRanked.totalAssists = entry.aggregatedStats.totalAssists;
        statsRanked.totalChampionKills = entry.aggregatedStats.totalChampionKills;
        statsRanked.totalDamageDealt = entry.aggregatedStats.totalDamageDealt;
        statsRanked.totalDamageTaken = entry.aggregatedStats.totalDamageTaken;
        statsRanked.totalDoubleKills = entry.aggregatedStats.totalDoubleKills;
        statsRanked.totalFirstBlood = entry.aggregatedStats.totalFirstBlood;
        statsRanked.totalGoldEarned = entry.aggregatedStats.totalGoldEarned;
        statsRanked.totalHeal = entry.aggregatedStats.totalHeal;
        statsRanked.totalMagicDamageDealt = entry.aggregatedStats.totalMagicDamageDealt;
        statsRanked.totalMinionKills = entry.aggregatedStats.totalMinionKills;
        statsRanked.totalNeutralMinionsKilled = entry.aggregatedStats.totalNeutralMinionsKilled;
        statsRanked.totalPentaKills = entry.aggregatedStats.totalPentaKills;
        statsRanked.totalPhysicalDamageDealt = entry.aggregatedStats.totalPhysicalDamageDealt;
        statsRanked.totalQuadraKills = entry.aggregatedStats.totalQuadraKills;
        statsRanked.totalSessionsLost = entry.aggregatedStats.totalSessionsLost;
        statsRanked.totalSessionsPlayed = entry.aggregatedStats.totalSessionsPlayed;
        statsRanked.totalSessionsWon = entry.aggregatedStats.totalSessionsWon;
        statsRanked.totalTripleKills = entry.aggregatedStats.totalTripleKills;
        statsRanked.totalTurretsKilled = entry.aggregatedStats.totalTurretsKilled;
        statsRanked.totalUnrealKills = entry.aggregatedStats.totalUnrealKills;

        return statsRanked;
    }
}
