package tsingularity.lolexplorer.Model.DTO.Team;

import java.util.List;

public class Team {
    public long                      createDate;
    public String                    fullId;
    public long                      lastGameDate;
    public long                      lastJoinDate;
    public long                      lastJoinedRankedTeamQueueDate;
    public List<MatchHistorySummary> matchHistory;
    public long                      modifyDate;
    public String                    name;
    public Roster                    roster;
    public long                      secondLastJoinDate;
    public String                    status;
    public String                    tag;
    public List<TeamStatDetail>      teamStatDetails;
    public long                      thirdLastJoinDate;
}
