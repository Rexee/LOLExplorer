package tsingularity.lolexplorer.Model.Constants;

import java.util.ArrayList;
import java.util.List;

import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Util.SB;

public class MMR {

    public static final int UNRANKED_MMR = 1200;
    public List<LeagueToMMR> mLeagueToMMRTable;

    public MMR() {

        mLeagueToMMRTable = getMMRTable();
    }

    public static League getLeague(List<League> leagues) {

        League resLeague = null;

        if (leagues != null) {
            for (League league : leagues) {
                if (league == null) continue;
                if (league.queue != Leagues.RANKED_SOLO_5x5.getValue()) continue;

                resLeague = league;
                break;
            }
        }

        return resLeague;

    }

    public static String getLeagueText(League league, String sLP) {
        if (league == null) return LeagueTier.UNRANKED.getName();
        return SB.getLeagueRepresentation(league, sLP);
    }

    public int getMMR(League league) {

        if (league == null) return UNRANKED_MMR;

        for (int i = 0; i < mLeagueToMMRTable.size(); i++) {
            LeagueToMMR curLTM = mLeagueToMMRTable.get(i);
            if (curLTM.tier != league.tier || curLTM.division != league.division) continue;

            return curLTM.mmr + Math.round((float) curLTM.mmrWidth / 100 * league.leaguePoints);

        }

        return 0;
    }

    public String getLeagueFromMMR(int mmr) {

        if (mmr == 0) return LeagueTier.UNRANKED.getName();
        if (mmr < 800) return LeagueTier.WOOD.getName();

        for (int i = 0; i < mLeagueToMMRTable.size(); i++) {
            LeagueToMMR curLTM = mLeagueToMMRTable.get(i);
            if (curLTM.mmr <= mmr && ((curLTM.mmr + curLTM.mmrWidth) >= mmr) || curLTM.mmrWidth == 0) {
                int lp = mmr - curLTM.mmr;
                if (curLTM.mmrWidth != 0) lp = lp * 100 / curLTM.mmrWidth;
                return "" + curLTM.tier.getName() + " " + curLTM.division + " " + lp + " LP";
            }
        }

        return "";
    }

    public List<LeagueToMMR> getMMRTable() {
        List<LeagueToMMR> res = new ArrayList<>();

        res.add(new LeagueToMMR(LeagueTier.BRONZE, LeagueDivision.I, 1080, 70));
        res.add(new LeagueToMMR(LeagueTier.BRONZE, LeagueDivision.II, 1010, 70));
        res.add(new LeagueToMMR(LeagueTier.BRONZE, LeagueDivision.III, 940, 70));
        res.add(new LeagueToMMR(LeagueTier.BRONZE, LeagueDivision.IV, 870, 70));
        res.add(new LeagueToMMR(LeagueTier.BRONZE, LeagueDivision.V, 800, 70));

        res.add(new LeagueToMMR(LeagueTier.SILVER, LeagueDivision.I, 1430, 70));
        res.add(new LeagueToMMR(LeagueTier.SILVER, LeagueDivision.II, 1360, 70));
        res.add(new LeagueToMMR(LeagueTier.SILVER, LeagueDivision.III, 1290, 70));
        res.add(new LeagueToMMR(LeagueTier.SILVER, LeagueDivision.IV, 1220, 70));
        res.add(new LeagueToMMR(LeagueTier.SILVER, LeagueDivision.V, 1150, 70));

        res.add(new LeagueToMMR(LeagueTier.GOLD, LeagueDivision.I, 1780, 70));
        res.add(new LeagueToMMR(LeagueTier.GOLD, LeagueDivision.II, 1710, 70));
        res.add(new LeagueToMMR(LeagueTier.GOLD, LeagueDivision.III, 1640, 70));
        res.add(new LeagueToMMR(LeagueTier.GOLD, LeagueDivision.IV, 1570, 70));
        res.add(new LeagueToMMR(LeagueTier.GOLD, LeagueDivision.V, 1500, 70));

        res.add(new LeagueToMMR(LeagueTier.PLATINUM, LeagueDivision.I, 2130, 70));
        res.add(new LeagueToMMR(LeagueTier.PLATINUM, LeagueDivision.II, 2060, 70));
        res.add(new LeagueToMMR(LeagueTier.PLATINUM, LeagueDivision.III, 1990, 70));
        res.add(new LeagueToMMR(LeagueTier.PLATINUM, LeagueDivision.IV, 1920, 70));
        res.add(new LeagueToMMR(LeagueTier.PLATINUM, LeagueDivision.V, 1850, 70));

        res.add(new LeagueToMMR(LeagueTier.DIAMOND, LeagueDivision.I, 2480, 70));
        res.add(new LeagueToMMR(LeagueTier.DIAMOND, LeagueDivision.II, 2410, 70));
        res.add(new LeagueToMMR(LeagueTier.DIAMOND, LeagueDivision.III, 2340, 70));
        res.add(new LeagueToMMR(LeagueTier.DIAMOND, LeagueDivision.IV, 2270, 70));
        res.add(new LeagueToMMR(LeagueTier.DIAMOND, LeagueDivision.V, 2200, 70));

        res.add(new LeagueToMMR(LeagueTier.MASTER, LeagueDivision.I, 2550, 350));

        res.add(new LeagueToMMR(LeagueTier.CHALLENGER, LeagueDivision.I, 2900, 0));

        return res;
    }

    static class LeagueToMMR {
        public LeagueTier     tier;
        public LeagueDivision division;
        public int            mmr;
        public int            mmrWidth;

        public LeagueToMMR(LeagueTier tier, LeagueDivision division, int mmr, int mmrWidth) {
            this.tier = tier;
            this.division = division;
            this.mmr = mmr;
            this.mmrWidth = mmrWidth;
        }

    }

}
