package tsingularity.lolexplorer.Model;

import java.util.ArrayList;
import java.util.List;

import cupboard.annotation.CompositeIndex;
import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.QueueSubType;
import tsingularity.lolexplorer.Model.DTO.Game.Game;
import tsingularity.lolexplorer.Model.DTO.Game.Player;
import tsingularity.lolexplorer.Model.DTO.Match.MatchSummaryDTO;
import tsingularity.lolexplorer.Model.DTO.Match.Participant;
import tsingularity.lolexplorer.Model.DTO.Match.ParticipantIdentity;

public class MatchHistory {

    @Index(indexNames = {@CompositeIndex(indexName = "indexMatch")})
    public long matchId;
    @Index(indexNames = {@CompositeIndex(indexName = "indexMatch", order = 2)})
    public long summonerId;

    public long         matchCreation;
    public long         matchDuration;
    public QueueSubType queueType;
    public String       matchVersion;

    public int championId;
//    public String summonerName;

    public int mapId;
    public int teamId;

    public int ipEarned;
    public int spell1Id;
    public int spell2Id;

    public int item0;
    public int item1;
    public int item2;
    public int item3;
    public int item4;
    public int item5;
    public int item6;

    public boolean winner;

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

    public static MatchHistory fromDTO(MatchSummaryDTO matchSummaryDTO) {

        MatchHistory matchHistory = new MatchHistory();

        matchHistory.matchId = matchSummaryDTO.matchId;
        matchHistory.mapId = matchSummaryDTO.mapId;
        matchHistory.matchCreation = matchSummaryDTO.matchCreation;
        matchHistory.matchDuration = matchSummaryDTO.matchDuration;
//        matchHistory.description = matchSummaryDTO.description;
        matchHistory.matchVersion = matchSummaryDTO.matchVersion;

        for (Participant participant : matchSummaryDTO.participants) {
            matchHistory.championId = participant.championId;
            matchHistory.spell1Id = participant.spell1Id;
            matchHistory.spell2Id = participant.spell2Id;
            matchHistory.teamId = participant.teamId;

            matchHistory.item0 = participant.stats.item0;
            matchHistory.item1 = participant.stats.item1;
            matchHistory.item2 = participant.stats.item2;
            matchHistory.item3 = participant.stats.item3;
            matchHistory.item4 = participant.stats.item4;
            matchHistory.item5 = participant.stats.item5;
            matchHistory.item6 = participant.stats.item6;

            matchHistory.winner = participant.stats.winner;
            matchHistory.kills = participant.stats.kills;
            matchHistory.deaths = participant.stats.deaths;
            matchHistory.assists = participant.stats.assists;
            matchHistory.champLevel = participant.stats.champLevel;
            matchHistory.goldEarned = participant.stats.goldEarned;
            matchHistory.totalDamageDealtToChampions = participant.stats.totalDamageDealtToChampions;

            matchHistory.wardsPlaced = participant.stats.wardsPlaced;

            matchHistory.totalCreepsKilled = participant.stats.minionsKilled + participant.stats.neutralMinionsKilled;

            matchHistory.pentaKills = participant.stats.pentaKills;
            matchHistory.quadraKills = participant.stats.quadraKills;
            matchHistory.unrealKills = participant.stats.unrealKills;
        }

        for (ParticipantIdentity participantIdentity : matchSummaryDTO.participantIdentities) {
            matchHistory.summonerId = participantIdentity.player.summonerId;
//            matchHistory.summonerName = participantIdentity.player.summonerName;
        }

        return matchHistory;
    }

    public static MatchHistory fromDTO(Game game, long summonerId) {

        MatchHistory matchHistory = new MatchHistory();

        matchHistory.summonerId = summonerId;
        matchHistory.matchId = game.gameId;
        matchHistory.mapId = game.mapId;
        matchHistory.matchCreation = game.createDate;
        matchHistory.queueType = game.subType;
        matchHistory.spell1Id = game.spell1;
        matchHistory.spell2Id = game.spell2;
        matchHistory.ipEarned = game.ipEarned;
        matchHistory.championId = game.championId;
        matchHistory.teamId = game.teamId;

        matchHistory.matchDuration = game.stats.timePlayed;

        matchHistory.item0 = game.stats.item0;
        matchHistory.item1 = game.stats.item1;
        matchHistory.item2 = game.stats.item2;
        matchHistory.item3 = game.stats.item3;
        matchHistory.item4 = game.stats.item4;
        matchHistory.item5 = game.stats.item5;
        matchHistory.item6 = game.stats.item6;

        matchHistory.winner = game.stats.win;
        matchHistory.kills = game.stats.championsKilled;
        matchHistory.deaths = game.stats.numDeaths;
        matchHistory.assists = game.stats.assists;
        matchHistory.champLevel = game.stats.level;
        matchHistory.goldEarned = game.stats.goldEarned;
        matchHistory.totalDamageDealtToChampions = game.stats.totalDamageDealtToChampions;
        matchHistory.wardsPlaced = game.stats.wardPlaced;
        matchHistory.totalCreepsKilled = game.stats.minionsKilled + game.stats.neutralMinionsKilled;
        matchHistory.pentaKills = game.stats.pentaKills;
        matchHistory.quadraKills = game.stats.quadraKills;
        matchHistory.unrealKills = game.stats.unrealKills;

        return matchHistory;
    }

    public static List<MatchParticipant> participantListFromDTO(Game entry, long summonerId) {

        List<MatchParticipant> matchParticipantList;
        if (entry.fellowPlayers == null) matchParticipantList = new ArrayList<MatchParticipant>(1);
        else matchParticipantList = new ArrayList<MatchParticipant>(entry.fellowPlayers.size() + 1);

        MatchParticipant matchParticipant = new MatchParticipant();
        matchParticipant.championId = entry.championId;
        matchParticipant.summonerId = summonerId;
        matchParticipant.teamId = entry.teamId;
        matchParticipant.matchId = entry.gameId;
        matchParticipant.spell1Id = entry.spell1;
        matchParticipant.spell2Id = entry.spell2;
        matchParticipant.item0 = entry.stats.item0;
        matchParticipant.item1 = entry.stats.item1;
        matchParticipant.item2 = entry.stats.item2;
        matchParticipant.item3 = entry.stats.item3;
        matchParticipant.item4 = entry.stats.item4;
        matchParticipant.item5 = entry.stats.item5;
        matchParticipant.item6 = entry.stats.item6;

        matchParticipantList.add(matchParticipant);

        if (entry.fellowPlayers == null) return matchParticipantList;

        for (Player player : entry.fellowPlayers) {

            matchParticipant = new MatchParticipant();
            matchParticipant.championId = player.championId;
            matchParticipant.summonerId = player.summonerId;
            matchParticipant.teamId = player.teamId;
            matchParticipant.matchId = entry.gameId;

            matchParticipantList.add(matchParticipant);
        }

        return matchParticipantList;
    }
}
