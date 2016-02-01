package tsingularity.lolexplorer.Model.DTO.Match;

import java.util.List;

import tsingularity.lolexplorer.Model.DTO.Game.BannedChampion;

public class MatchTeam {
    public List<BannedChampion> bans;
    public int                  baronKills;
    public long                 dominionVictoryScore;
    public int                  dragonKills;
    public boolean              firstBaron;
    public boolean              firstBlood;
    public boolean              firstDragon;
    public boolean              firstInhibitor;
    public boolean              firstTower;
    public int                  inhibitorKills;
    public int                  teamId;
    public int                  towerKills;
    public int                  vilemawKills;
    public boolean              winner;

}
