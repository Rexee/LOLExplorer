package tsingularity.lolexplorer.Model.DTO.League;

import java.util.List;

import tsingularity.lolexplorer.Model.Constants.LeagueTier;

public class LeagueDTO {
    public List<LeagueEntry> entries;
    public String            name;
    public String            participantId;
    public String            queue;
    public LeagueTier        tier;
}
