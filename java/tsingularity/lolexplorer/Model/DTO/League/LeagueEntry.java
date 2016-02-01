package tsingularity.lolexplorer.Model.DTO.League;

import tsingularity.lolexplorer.Model.Constants.LeagueDivision;

public class LeagueEntry {
    public LeagueDivision division;
    public boolean        isFreshBlood;
    public boolean        isHotStreak;
    public boolean        isInactive;
    public boolean        isVeteran;
    public int            leaguePoints;
    public int            losses;
    public MiniSeries     miniSeries;
    public String         playerOrTeamId;
    public String         playerOrTeamName;
    public int            wins;
}
