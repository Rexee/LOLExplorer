package tsingularity.lolexplorer.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.ConstantsCommon;
import tsingularity.lolexplorer.Model.Constants.QueueType;
import tsingularity.lolexplorer.Model.DTO.Game.BannedChampion;
import tsingularity.lolexplorer.Model.DTO.Match.MatchDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchTeam;
import tsingularity.lolexplorer.Model.DTO.Match.Participant;
import tsingularity.lolexplorer.Model.DTO.Match.ParticipantIdentity;

public class Match {

    @Index
    public long matchId;

    public int       mapId;
    public long      matchCreation;
    public long      matchDuration;
    public QueueType queueType;
    public String    matchVersion;

    public boolean bottom_team_winner;
    public boolean bottom_team_firstBlood;
    public boolean bottom_team_firstBaron;
    public boolean bottom_team_firstDrake;

    public int  bottom_team_kills;
    public int  bottom_team_deaths;
    public int  bottom_team_assists;
    public long bottom_team_ban1;
    public long bottom_team_ban2;
    public long bottom_team_ban3;
    public int  bottom_team_barons;
    public int  bottom_team_drakes;
    public int  bottom_team_towers;

    public int  upper_team_kills;
    public int  upper_team_deaths;
    public int  upper_team_assists;
    public long upper_team_ban1;
    public long upper_team_ban2;
    public long upper_team_ban3;
    public int  upper_team_barons;
    public int  upper_team_drakes;
    public int  upper_team_towers;

    public static Match fromDTO(MatchDTO matchDTO) {

        Match match = new Match();

        match.matchId = matchDTO.matchId;
        match.mapId = matchDTO.mapId;
        match.matchCreation = matchDTO.matchCreation;
        match.matchDuration = matchDTO.matchDuration;
        match.queueType = matchDTO.queueType;
        match.matchVersion = matchDTO.matchVersion;

        for (Participant participant : matchDTO.participants) {
            if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) {
                match.bottom_team_kills += participant.stats.kills;
                match.bottom_team_deaths += participant.stats.deaths;
                match.bottom_team_assists += participant.stats.assists;
            } else {
                match.upper_team_kills += participant.stats.kills;
                match.upper_team_deaths += participant.stats.deaths;
                match.upper_team_assists += participant.stats.assists;
            }
        }

        for (MatchTeam team : matchDTO.teams) {
            if (team.teamId == ConstantsCommon.TEAM_BOTTOM) {
                match.bottom_team_winner = team.winner;
                match.bottom_team_firstBlood = team.firstBlood;
                match.bottom_team_firstBaron = team.firstBaron;
                match.bottom_team_firstDrake = team.firstDragon;
                match.bottom_team_barons = team.baronKills;
                match.bottom_team_drakes = team.dragonKills;
                match.bottom_team_towers = team.towerKills;

                if (team.bans != null) {
                    ListIterator banIterator = team.bans.listIterator();
                    while (banIterator.hasNext()) {
                        BannedChampion bannedChampion = (BannedChampion) banIterator.next();
                        switch (banIterator.nextIndex()) {
                            case 1:
                                match.bottom_team_ban1 = bannedChampion.championId;
                                break;
                            case 2:
                                match.bottom_team_ban2 = bannedChampion.championId;
                                break;
                            case 3:
                                match.bottom_team_ban3 = bannedChampion.championId;
                                break;
                        }
                    }
                }
            } else {
                match.upper_team_barons = team.baronKills;
                match.upper_team_drakes = team.dragonKills;
                match.upper_team_towers = team.towerKills;

                if (team.bans != null) {
                    ListIterator banIterator = team.bans.listIterator();
                    while (banIterator.hasNext()) {
                        BannedChampion bannedChampion = (BannedChampion) banIterator.next();
                        switch (banIterator.nextIndex()) {
                            case 1:
                                match.upper_team_ban1 = bannedChampion.championId;
                                break;
                            case 2:
                                match.upper_team_ban2 = bannedChampion.championId;
                                break;
                            case 3:
                                match.upper_team_ban3 = bannedChampion.championId;
                                break;
                        }
                    }
                }
            }
        }

        return match;
    }

    public static List<MatchParticipant> participantListFromDTO(MatchDTO matchDTO) {

        List<MatchParticipant> matchParticipantList = new ArrayList<MatchParticipant>(matchDTO.participants.size());

        for (Participant participant : matchDTO.participants) {

            MatchParticipant matchParticipant = new MatchParticipant();

            matchParticipant.matchId = matchDTO.matchId;
            matchParticipant.participantId = participant.participantId;
            matchParticipant.teamId = participant.teamId;
            matchParticipant.championId = participant.championId;
            matchParticipant.highestAchievedSeasonTier = participant.highestAchievedSeasonTier;
            matchParticipant.spell1Id = participant.spell1Id;
            matchParticipant.spell2Id = participant.spell2Id;

            matchParticipant.item0 = participant.stats.item0;
            matchParticipant.item1 = participant.stats.item1;
            matchParticipant.item2 = participant.stats.item2;
            matchParticipant.item3 = participant.stats.item3;
            matchParticipant.item4 = participant.stats.item4;
            matchParticipant.item5 = participant.stats.item5;
            matchParticipant.item6 = participant.stats.item6;

            matchParticipant.kills = participant.stats.kills;
            matchParticipant.deaths = participant.stats.deaths;
            matchParticipant.assists = participant.stats.assists;
            matchParticipant.champLevel = participant.stats.champLevel;
            matchParticipant.goldEarned = participant.stats.goldEarned;
            matchParticipant.totalDamageDealtToChampions = participant.stats.totalDamageDealtToChampions;
            matchParticipant.wardsPlaced = participant.stats.wardsPlaced;
            matchParticipant.totalCreepsKilled = participant.stats.minionsKilled + participant.stats.neutralMinionsKilled;
            matchParticipant.pentaKills = participant.stats.pentaKills;
            matchParticipant.quadraKills = participant.stats.quadraKills;
            matchParticipant.unrealKills = participant.stats.unrealKills;

            for (ParticipantIdentity participantIdentity : matchDTO.participantIdentities) {
                if (participantIdentity.player == null) continue;
                if (participantIdentity.participantId == participant.participantId) {
                    matchParticipant.summonerId = participantIdentity.player.summonerId;
                    matchParticipant.summonerName = participantIdentity.player.summonerName;
                }
            }

            matchParticipantList.add(matchParticipant);

        }

        return matchParticipantList;
    }

}
