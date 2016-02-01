package tsingularity.lolexplorer.Model.DTO.Match;

import java.util.List;

import tsingularity.lolexplorer.Model.Constants.QueueType;

public class MatchSummaryDTO {

    public int                       mapId;
    public long                      matchId;
    public long                      matchCreation;
    public long                      matchDuration;
    public String                    matchMode;        //CLASSIC, ODIN, ARAM, TUTORIAL, ONEFORALL, ASCENSION, FIRSTBLOOD, KINGPORO
    public String                    matchType;        //CUSTOM_GAME, MATCHED_GAME, TUTORIAL_GAME
    public String                    matchVersion;
    public List<ParticipantIdentity> participantIdentities;
    public List<Participant>         participants;
    public QueueType                 queueType;
    public String                    region;
    public String                    season;           //PRESEASON3, SEASON3, PRESEASON2014, SEASON2014, PRESEASON2015, SEASON2015
}
