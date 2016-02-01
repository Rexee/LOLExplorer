package tsingularity.lolexplorer.Model.DTO.Game;

import java.util.List;

public class Participant {
    public boolean       bot;
    public int           championId;
    public List<Mastery> masteries;
    public long          profileIconId;
    public List<Rune>    runes;
    public int           spell1Id;
    public int           spell2Id;
    public long          summonerId;
    public String        summonerName;
    public long          teamId;
}
