package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.Servers.Server;

public class SearchHistory {

    @Index
    public long summonerId;

    public String name;
    public long   searchDate;
    public Server server;
    public int    profileIconId;
    public long   summonerLevel;

    public static SearchHistory fromSummoner(Summoner summoner, long searchDate) {
        SearchHistory res = new SearchHistory();

        res.summonerId = summoner.summonerId;
        res.name = summoner.name;
        res.server = summoner.server;
        res.searchDate = searchDate;
        res.profileIconId = summoner.profileIconId;
        res.summonerLevel = summoner.summonerLevel;

        return res;
    }
}
