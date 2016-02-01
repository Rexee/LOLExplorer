package tsingularity.lolexplorer.Model;

import cupboard.annotation.CompositeIndex;
import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.LeagueDivision;
import tsingularity.lolexplorer.Model.Constants.LeagueTier;
import tsingularity.lolexplorer.Model.Constants.Leagues;
import tsingularity.lolexplorer.Model.DTO.League.LeagueDTO;
import tsingularity.lolexplorer.Model.DTO.League.LeagueEntry;

public class League {

    //    public Long   _id;
    @Index(indexNames = {@CompositeIndex(indexName = "indexLeague")})
    public long           summonerId;
    @Index(indexNames = {@CompositeIndex(indexName = "indexLeague", order = 2)})
    public int            queue;
    public LeagueTier     tier;
    public LeagueDivision division;
    public int            leaguePoints;

    public int losses;
    public int wins;

    public boolean isFreshBlood;
    public boolean isHotStreak;
    public boolean isInactive;
    public boolean isVeteran;

    public int    mini_losses;
    public int    mini_wins;
    public String mini_progress;
    public int    mini_target;

    public String playerOrTeamId;
    public String playerOrTeamName;
    public String name;
    public String participantId;

    public static League fromDTO(String summonerId, LeagueDTO leagueDTO) {

        League league = new League();

        league.summonerId = Long.parseLong(summonerId);
        league.queue = Leagues.getValue(leagueDTO.queue);
        league.tier = leagueDTO.tier;
        league.name = leagueDTO.name;
        league.participantId = leagueDTO.participantId;

        for (LeagueEntry leagueEntry : leagueDTO.entries) {
            league.division = leagueEntry.division;
            league.leaguePoints = leagueEntry.leaguePoints;
            league.losses = leagueEntry.losses;
            league.wins = leagueEntry.wins;
            league.isFreshBlood = leagueEntry.isFreshBlood;
            league.isHotStreak = leagueEntry.isHotStreak;
            league.isInactive = leagueEntry.isInactive;
            league.isVeteran = leagueEntry.isVeteran;

            if (leagueEntry.miniSeries != null) {
                league.mini_losses = leagueEntry.miniSeries.losses;
                league.mini_wins = leagueEntry.miniSeries.wins;
                league.mini_progress = leagueEntry.miniSeries.progress;
                league.mini_target = leagueEntry.miniSeries.target;
            }

            league.playerOrTeamId = leagueEntry.playerOrTeamId;
            league.playerOrTeamName = leagueEntry.playerOrTeamName;
        }

        return league;
    }
}
