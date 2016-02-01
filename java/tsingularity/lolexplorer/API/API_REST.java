package tsingularity.lolexplorer.API;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import tsingularity.lolexplorer.Model.Constants.Servers.Server;
import tsingularity.lolexplorer.Model.DTO.Champion.Champion;
import tsingularity.lolexplorer.Model.DTO.Champion.ChampionsList;
import tsingularity.lolexplorer.Model.DTO.Game.CurrentGameDTO;
import tsingularity.lolexplorer.Model.DTO.Game.RecentGames;
import tsingularity.lolexplorer.Model.DTO.League.LeagueDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchesDTO;
import tsingularity.lolexplorer.Model.DTO.Server.Shards;
import tsingularity.lolexplorer.Model.DTO.StaticData.Champion.ChampionList;
import tsingularity.lolexplorer.Model.DTO.StaticData.Item.ItemList;
import tsingularity.lolexplorer.Model.DTO.StaticData.Realm.RealmDTO;
import tsingularity.lolexplorer.Model.DTO.StaticData.SummonerSpell.SummonerSpellList;
import tsingularity.lolexplorer.Model.DTO.Stats.StatsRankedDTO;
import tsingularity.lolexplorer.Model.DTO.Stats.StatsSummaryDTO;
import tsingularity.lolexplorer.Model.DTO.Summoner.MasteryPages;
import tsingularity.lolexplorer.Model.DTO.Summoner.RunePages;
import tsingularity.lolexplorer.Model.DTO.Summoner.SummonerDTO;
import tsingularity.lolexplorer.Model.DTO.Team.Team;
import tsingularity.lolexplorer.Settings;

public class API_REST {

    public String key;
    public String mEndpoint;
    public String mHost;
    public String mSummoner;
    public String mSummonerSimple;
    public String mSummonerId;

    public API_Retrofit service;
    RestAdapter mRestAdapter;
    LOLEndpoint mLOLEndpoint;

    public API_REST() {
        this(Settings.API_KEY, Settings.DEFAULT_SERVER.getHost());
    }

    public API_REST(String host) {
        this(Settings.API_KEY, host);
    }

    public API_REST(String key, String mHost) {
        this.key = key;
        this.mHost = mHost;
        this.mEndpoint = mHost;

        initRetroFit();
    }

    public void initRetroFit() {

        mLOLEndpoint = new LOLEndpoint(API.root(mHost));

        mRestAdapter = new RestAdapter.Builder().setEndpoint(mLOLEndpoint).setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam(API.KEY_PARAM, Settings.API_KEY);
            }
        }).setLogLevel(LogLevel.FULL).build();

        service = mRestAdapter.create(API_Retrofit.class);
    }

    public void setServer(String newHost) {
        mHost = newHost;

        if (mEndpoint.equals(newHost)) return;

        mEndpoint = newHost;
        mLOLEndpoint.setEndpoint(API.root(newHost));
    }

    public API_REST setEndpoint(Server endpoint) {
        String newEndpoint = endpoint.getHost();

        if (mEndpoint.equals(newEndpoint)) return this;

        mEndpoint = newEndpoint;
        mLOLEndpoint.setEndpoint(API.root(newEndpoint));
        return this;
    }

    //summoner
    public Map<String, SummonerDTO> getSummonerDataByName(String summoner) {
        return service.getSummonerDataByName(mHost, summoner);
    }

    public Map<String, List<LeagueDTO>> getLeagueBySummoner(String summonerId) {
        return service.getLeagueBySummoner(mHost, summonerId);
    }

    public Map<String, SummonerDTO> getSummonerDataById(String summonerId) {
        return service.getSummonerDataById(mHost, summonerId);
    }

    public void getSummonerName(Callback<Map<String, String>> getSummonerNameCallback) {
        service.getSummonerName(mHost, mSummonerId, getSummonerNameCallback);
    }

    public void getSummonerMasteries(Callback<Map<String, MasteryPages>> getSummonerMasteriesCallback) {
        service.getSummonerMasteries(mHost, mSummonerId, getSummonerMasteriesCallback);
    }

    public void getSummonerRunes(Callback<Map<String, RunePages>> getSummonerRunesCallback) {
        service.getSummonerRunes(mHost, mSummonerId, getSummonerRunesCallback);
    }

    //champion
    public void getChampionsList(Callback<ChampionsList> getChampionsListCallback) {
        service.getChampionsList(mHost, getChampionsListCallback);
    }

    public void getChampion(String id, Callback<Champion> getChampionCallback) {
        service.getChampion(mHost, id, getChampionCallback);
    }

    //stats
    public StatsSummaryDTO getStatsSummary(String summonerId) {
        return service.getStatsSummary(mHost, summonerId);
    }

    public StatsRankedDTO getStatsRanked(String summonerId) {
        return service.getStatsRanked(mHost, summonerId);
    }

    //team
    public void getTeamsBySummonerIds(Callback<Map<String, List<Team>>> getTeamsBySummonerIdCallback) {
        service.getTeamsBySummonerId(mHost, mSummonerId, getTeamsBySummonerIdCallback);
    }

    public void getTeamByTeamId(String teamIds, Callback<Map<String, Team>> getTeamByTeamIdCallback) {
        service.getTeamByTeamId(mHost, teamIds, getTeamByTeamIdCallback);
    }

    //matchhistory
    public MatchesDTO getMatchHistoryRankedSolo(String summonerId) {
        return service.getMatchHistoryRankedSolo(mHost, summonerId);
    }

    public RecentGames getMatchHistoryAll(String summonerId) {
        return service.getMatchHistory_All(mHost, summonerId);
    }

    //match
    public MatchDTO getMatch(String matchId) {
        return service.getMatchDetail(mHost, matchId, false);
    }

    //status
    public List<Shards> getServersList() {
        return service.getServersList();
    }

    //static Realm
    public RealmDTO getRealm() {
        return service.getRealm(mHost);
    }

    public ChampionList static_getChampionsList(String vesion, String champData) {
        return service.static_getChampionsList(mHost, true, vesion, champData);
    }

    public SummonerSpellList static_getSummonersSpellsList(String vesion, String spellData) {
        return service.static_getSpellsList(mHost, true, vesion, spellData);
    }

    public ItemList static_getItemsList(String vesion, String itemListData) {
        return service.static_getItemsList(mHost, vesion, itemListData);
    }

    public CurrentGameDTO getCurrentGame(String summonerId) {
        return service.getCurrentGame(Server.getServerByHost(mHost).getPlatformId(), summonerId);
    }

    public final class LOLEndpoint implements Endpoint {
        private static final String DEFAULT_NAME = "default";
        private final String name;
        private       String apiUrl;

        LOLEndpoint(String apiUrl) {
            this.apiUrl = apiUrl;
            this.name = DEFAULT_NAME;
        }

        public void setEndpoint(String host) {
            this.apiUrl = host;
        }

        @Override
        public String getUrl() {
            return apiUrl;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
