package tsingularity.lolexplorer.Model.DTO.Match;

import java.util.List;

import tsingularity.lolexplorer.Model.DTO.Match.Players.Mastery;
import tsingularity.lolexplorer.Model.DTO.Match.Players.Rune;

public class Participant {

    public int                 championId;
    public List<Mastery>       masteries;
    public int                 participantId;
    public List<Rune>          runes;
    public int                 spell1Id;
    public int                 spell2Id;
    public ParticipantStats    stats;
    public int                 teamId;
    public ParticipantTimeline timeline;
    public String              highestAchievedSeasonTier;

}
