package tsingularity.lolexplorer.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tsingularity.lolexplorer.API.StaticData;
import tsingularity.lolexplorer.Model.Constants.MapEnum;
import tsingularity.lolexplorer.Model.Constants.QueueSubType;
import tsingularity.lolexplorer.Util.Util;

public class MatchHistoryDisplay {

    public long    matchId;
    public String  matchCreation;
    public String  matchDuration;
    public String  queueType;
    public String  champion_url;
    public String  champion_name;
    public String  summonerName;
    public int     teamId;
    public String  spell1_url;
    public String  spell2_url;
    public String  highestAchievedSeasonTier;
    public String  item0_url;
    public String  item1_url;
    public String  item2_url;
    public String  item3_url;
    public String  item4_url;
    public String  item5_url;
    public String  item6_url;
    public boolean winner;
    public String  KDA;
    public String  KDA_Num;
    public long    champLevel;
    public String  goldEarned;
    public long    totalDamageDealtToChampions;
    public long    wardsPlaced;
    public long    totalCreepsKilled;
    public long    pentaKills;
    public long    quadraKills;
    public long    unrealKills;

    public static void getMatchHistory(StaticData mStaticData, List<MatchHistoryDisplay> matchHistoryDisplayList, List<MatchHistory> matchHistoryList, String k) {

        SimpleDateFormat sdf_match_data = new SimpleDateFormat("d MMM yyyy HH:mm");

        int size = matchHistoryList.size();

        for (int i = 0; i < size; i++) {

            MatchHistory matchHistory = matchHistoryList.get(i);

            MatchHistoryDisplay matchHistoryDisplayItem = getMatchById(matchHistory.matchId, matchHistoryDisplayList);

            if (matchHistoryDisplayItem == null) {
                matchHistoryDisplayItem = new MatchHistoryDisplay();
                matchHistoryDisplayList.add(matchHistoryDisplayItem);
            }

            matchHistoryDisplayItem.matchId = matchHistory.matchId;
            Date date = new Date(matchHistory.matchCreation);
            matchHistoryDisplayItem.matchCreation = sdf_match_data.format(date);
            matchHistoryDisplayItem.matchDuration = Util.secondsToTime(matchHistory.matchDuration);

            matchHistoryDisplayItem.KDA = Long.toString(matchHistory.kills) + "/" + Long.toString(matchHistory.deaths) + "/" + Long.toString(matchHistory.assists);

            if (matchHistory.deaths == 0) matchHistoryDisplayItem.KDA_Num = "*";
            else {
                float kda = ((float) matchHistory.kills + (float) matchHistory.assists) / (float) matchHistory.deaths;
                matchHistoryDisplayItem.KDA_Num = String.format("%.2f", kda);
            }

            float gold = (float) matchHistory.goldEarned / 1000;
            matchHistoryDisplayItem.goldEarned = String.format("%.1f", gold) + k;

            QueueSubType qst = matchHistory.queueType;
            String queueType = qst.getName();
            if (qst == QueueSubType.NONE) {
                queueType = queueType + " (" + MapEnum.getMap(matchHistory.mapId) + ")";
            }

            matchHistoryDisplayItem.queueType = queueType;

            matchHistoryDisplayItem.champion_url = mStaticData.getChampionIconURL(matchHistory.championId);
            matchHistoryDisplayItem.champion_name = "";
            matchHistoryDisplayItem.spell1_url = "";
            matchHistoryDisplayItem.spell2_url = "";
            matchHistoryDisplayItem.item0_url = mStaticData.getItemIconURL(matchHistory.item0);
            matchHistoryDisplayItem.item1_url = mStaticData.getItemIconURL(matchHistory.item1);
            matchHistoryDisplayItem.item2_url = mStaticData.getItemIconURL(matchHistory.item2);
            matchHistoryDisplayItem.item3_url = mStaticData.getItemIconURL(matchHistory.item3);
            matchHistoryDisplayItem.item4_url = mStaticData.getItemIconURL(matchHistory.item4);
            matchHistoryDisplayItem.item5_url = mStaticData.getItemIconURL(matchHistory.item5);
            matchHistoryDisplayItem.item6_url = mStaticData.getItemIconURL(matchHistory.item6);

            matchHistoryDisplayItem.teamId = matchHistory.teamId;
            matchHistoryDisplayItem.winner = matchHistory.winner;
            matchHistoryDisplayItem.champLevel = matchHistory.champLevel;
            matchHistoryDisplayItem.totalDamageDealtToChampions = matchHistory.totalDamageDealtToChampions;
            matchHistoryDisplayItem.wardsPlaced = matchHistory.wardsPlaced;
            matchHistoryDisplayItem.totalCreepsKilled = matchHistory.totalCreepsKilled;
            matchHistoryDisplayItem.pentaKills = matchHistory.pentaKills;
            matchHistoryDisplayItem.quadraKills = matchHistory.quadraKills;
            matchHistoryDisplayItem.unrealKills = matchHistory.unrealKills;

        }

    }

    private static MatchHistoryDisplay getMatchById(long matchId, List<MatchHistoryDisplay> matchHistoryList) {
        for (MatchHistoryDisplay locMatch : matchHistoryList) {
            if (locMatch == null) continue;

            if (locMatch.matchId == matchId) {
                return locMatch;
            }
        }
        return null;
    }
}
