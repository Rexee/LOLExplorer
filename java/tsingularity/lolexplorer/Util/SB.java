package tsingularity.lolexplorer.Util;

import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Model.Match;
import tsingularity.lolexplorer.Model.MatchHistory;
import tsingularity.lolexplorer.Model.MatchParticipant;
import tsingularity.lolexplorer.Model.StatsRanked;

public class SB {

    public static String upTeamKDA(Match mMatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Score: ");
        sb.append(mMatch.upper_team_kills);
        sb.append("/");
        sb.append(mMatch.upper_team_deaths);
        sb.append("/");
        sb.append(mMatch.upper_team_assists);

        return sb.toString();
    }

    public static String bottomTeamKDA(Match mMatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Score: ");
        sb.append(mMatch.bottom_team_kills);
        sb.append("/");
        sb.append(mMatch.bottom_team_deaths);
        sb.append("/");
        sb.append(mMatch.bottom_team_assists);

        return sb.toString();
    }

    public static String KDA(MatchParticipant participant) {
        return KDA(participant.kills, participant.deaths, participant.assists);
    }

    public static String KDA(MatchHistory participant) {
        return KDA(participant.kills, participant.deaths, participant.assists);
    }

    public static String KDA(long kills, long deaths, long assists) {

        float kda = ((float) kills + assists) / deaths;

        StringBuilder sb = new StringBuilder();
        sb.append("KDA: ");
        sb.append(String.format("%.2f", kda));
        sb.append(" (");
        sb.append(kills);
        sb.append("/");
        sb.append(deaths);
        sb.append("/");
        sb.append(assists);
        sb.append(")");

        return sb.toString();
    }

    public static String getLeagueRepresentation(League league, String sLP) {
        StringBuilder res = new StringBuilder();

        res.append(league.tier.getName());
        res.append(" ");
        res.append(league.division);
        res.append(". ");
        res.append(league.leaguePoints);
        res.append(" ");
        res.append(sLP);

        return res.toString();
    }

    public static String rankedStats(StatsRanked statsRanked) {
        StringBuilder res = new StringBuilder();

        float winratio = ((float) statsRanked.totalSessionsWon / statsRanked.totalSessionsPlayed * 100);

        res.append("G:");
        res.append(statsRanked.totalSessionsPlayed);
        res.append(" (");
        res.append(String.format("%.0f", winratio));
        res.append("%)");

        return res.toString();
    }

}
