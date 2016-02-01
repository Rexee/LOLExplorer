package tsingularity.lolexplorer.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.ConstantsCommon;
import tsingularity.lolexplorer.Model.Constants.QueueType;
import tsingularity.lolexplorer.Model.DTO.Game.BannedChampion;
import tsingularity.lolexplorer.Model.DTO.Game.CurrentGameDTO;
import tsingularity.lolexplorer.Model.DTO.Game.Participant;

public class CurrentGame {

    @Index
    public long      matchId;
    public long      gameStart;
    public long      gameLength;
    public QueueType queueType;
    public long      mapId;

    public long upper_team_ban1;
    public long upper_team_ban2;
    public long upper_team_ban3;

    public long bottom_team_ban1;
    public long bottom_team_ban2;
    public long bottom_team_ban3;

    public static CurrentGame fromDTO(CurrentGameDTO currentGameDTO) {

        CurrentGame currentGame = new CurrentGame();

        currentGame.matchId = currentGameDTO.gameId;
        currentGame.mapId = currentGameDTO.mapId;
        if (currentGameDTO.gameStartTime == 0) currentGame.gameStart = System.currentTimeMillis();
        else currentGame.gameStart = currentGameDTO.gameStartTime;

        currentGame.gameLength = currentGameDTO.gameLength;
        currentGame.queueType = QueueType.getQueueTypeByConfigId(currentGameDTO.gameQueueConfigId);

        if (currentGameDTO.bannedChampions != null) {
            int bot_k = 0;
            int top_k = 0;
            Iterator<BannedChampion> banIterator = currentGameDTO.bannedChampions.iterator();
            while (banIterator.hasNext()) {
                BannedChampion bannedChampion = banIterator.next();
                if (bannedChampion.teamId == ConstantsCommon.TEAM_BOTTOM) {
                    bot_k++;
                    switch (bot_k) {
                        case 1:
                            currentGame.bottom_team_ban1 = bannedChampion.championId;
                            break;
                        case 2:
                            currentGame.bottom_team_ban2 = bannedChampion.championId;
                            break;
                        case 3:
                            currentGame.bottom_team_ban3 = bannedChampion.championId;
                            break;
                    }
                } else {
                    top_k++;
                    switch (top_k) {
                        case 1:
                            currentGame.upper_team_ban1 = bannedChampion.championId;
                            break;
                        case 2:
                            currentGame.upper_team_ban2 = bannedChampion.championId;
                            break;
                        case 3:
                            currentGame.upper_team_ban3 = bannedChampion.championId;
                            break;
                    }

                }

            }
        }

        return currentGame;

    }

    public static List<CurrentGameParticipant> participantListFromDTO(CurrentGameDTO currentGameDTO) {

        List<CurrentGameParticipant> matchParticipantList = new ArrayList<CurrentGameParticipant>(currentGameDTO.participants.size());

        for (Participant participant : currentGameDTO.participants) {

            CurrentGameParticipant matchParticipant = new CurrentGameParticipant();

            matchParticipant.matchId = currentGameDTO.gameId;
            matchParticipant.teamId = participant.teamId;
            matchParticipant.championId = participant.championId;
            matchParticipant.summonerId = participant.summonerId;
            matchParticipant.summonerName = participant.summonerName;
            matchParticipant.spell1Id = participant.spell1Id;
            matchParticipant.spell2Id = participant.spell2Id;
            matchParticipant.isBot = participant.bot;

//            for (Mastery mastery : participant.masteries) {
//
//            }
            matchParticipantList.add(matchParticipant);

        }

        return matchParticipantList;
    }
}
