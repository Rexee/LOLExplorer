package tsingularity.lolexplorer.Model.DTO.Match;

import java.util.List;

import tsingularity.lolexplorer.Model.Constants.QueueType;


public class MatchDTO {

    public int                       mapId;
    public long                      matchCreation;
    public long                      matchDuration;
    public long                      matchId;
    public String                    matchMode;
    public String                    matchType;
    public String                    matchVersion;
    public List<ParticipantIdentity> participantIdentities;
    public List<Participant>         participants;
    public String                    platformId;
    public QueueType                 queueType;
    public String                    region;
    public String                    season;
    public List<MatchTeam>           teams;
    public Timeline                  timeline;
}
