package tsingularity.lolexplorer.API;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import tsingularity.lolexplorer.Model.DTO.Champion.Champion;
import tsingularity.lolexplorer.Model.DTO.Champion.ChampionsList;
import tsingularity.lolexplorer.Model.DTO.Game.CurrentGameDTO;
import tsingularity.lolexplorer.Model.DTO.Game.FeaturedGames;
import tsingularity.lolexplorer.Model.DTO.Game.RecentGames;
import tsingularity.lolexplorer.Model.DTO.League.LeagueDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchesDTO;
import tsingularity.lolexplorer.Model.DTO.Server.ShardStatus;
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

public interface API_Retrofit {

    //summoner
    @GET(API.SUMMONER_DATA_BY_NAME)
    Map<String, SummonerDTO> getSummonerDataByName(@Path("region") String region, @Path("summonerNames") String summonerNames);

    @GET(API.SUMMONER_DATA)
    Map<String, SummonerDTO> getSummonerDataById(@Path("region") String region, @Path("summonerIds") String summonerIds);

    @GET(API.SUMMONER_NAME)
    void getSummonerName(@Path("region") String region, @Path("summonerIds") String summonerIds, Callback<Map<String, String>> getSummonerNameCallback);

    @GET(API.SUMMONER_MASTERIES)
    void getSummonerMasteries(@Path("region") String region, @Path("summonerIds") String summonerIds, Callback<Map<String, MasteryPages>> getSummonerMasteriesCallback);

    @GET(API.SUMMONER_RUNES)
    void getSummonerRunes(@Path("region") String region, @Path("summonerIds") String summonerIds, Callback<Map<String, RunePages>> getSummonerRunesCallback);

    //champion
    @GET(API.LIST_CHAMPIONS)
    void getChampionsList(@Path("region") String region, Callback<ChampionsList> getListChampionsCallback);

    @GET(API.CHAMPION)
    void getChampion(@Path("region") String region, @Path("id") String id, Callback<Champion> getChampionCallback);

    //stats
    @GET(API.STATS_SUMMARY)
    StatsSummaryDTO getStatsSummary(@Path("region") String region, @Path("summonerId") String summonerId);

    @GET(API.STATS_RANKED)
    StatsRankedDTO getStatsRanked(@Path("region") String region, @Path("summonerId") String summonerId);

    //team
    @GET(API.TEAMS_BY_SUMMONER)
    void getTeamsBySummonerId(@Path("region") String region, @Path("summonerIds") String summonerIds, Callback<Map<String, List<Team>>> getTeamsBySummonerIdCallback);

    @GET(API.TEAM_BY_TEAM)
    void getTeamByTeamId(@Path("region") String region, @Path("teamIds") String teamIds, Callback<Map<String, Team>> getTeamByTeamIdCallback);

    //matchhistory
    @GET(API.MATCH_HISTORY_RANKED)
    MatchesDTO getMatchHistoryRankedSolo(@Path("region") String region, @Path("summonerId") String summonerId);

    //match
    @GET(API.MATCH)
    MatchDTO getMatchDetail(@Path("region") String region, @Path("matchId") String matchId, @Query("includeTimeline") boolean includeTimeline);

    //status
    @GET(API.SERVERS_LIST)
    List<Shards> getServersList();

    @GET(API.SERVERS_STATUS)
    ShardStatus getServerStatus(@Path("region") String region);

    //current-game
    @GET(API.CURRENT_GAME)
    CurrentGameDTO getCurrentGame(@Path("platformId") String platformId, @Path("summonerId") String summonerId);

    //featured-games
    @GET(API.FEATURED_GAMES)
    FeaturedGames getFeaturedGames();

    //game
    @GET(API.MATCH_HISTORY_ALL)
    RecentGames getMatchHistory_All(@Path("region") String region, @Path("summonerId") String summonerId);

    //league
    @GET(API.LEAGUE_BY_SUMMONER_ALL)
    Map<String, List<LeagueDTO>> getAllLeaguesBySummonerId(@Path("region") String region, @Path("summonerIds") String summonerIds);

    @GET(API.LEAGUE_BY_SUMMONER)
    Map<String, List<LeagueDTO>> getLeagueBySummoner(@Path("region") String region, @Path("summonerIds") String summonerIds);

    @GET(API.LEAGUE_BY_TEAM_ALL)
    Map<String, List<LeagueDTO>> getAllLeaguesByTeamId(@Path("region") String region, @Path("teamIds") String teamIds);

    @GET(API.LEAGUE_BY_TEAM)
    Map<String, List<LeagueDTO>> getLeagueByTeamId(@Path("region") String region, @Path("teamIds") String teamIds);

    @GET(API.LEAGUE_CHALLENGER)
    LeagueDTO getChallengerLeague();

    //static-data
    @GET(API.STATIC_CHAMPIONS_DATA)
    ChampionList static_getChampionsList(@Path("region") String region, @Query("dataById") boolean dataById, @Query("version") String version, @Query("champData") String champData);

    @GET(API.STATIC_ITEMS)
    ItemList static_getItemsList(@Path("region") String region, @Query("version") String version, @Query("itemListData") String itemListData);

    @GET(API.STATIC_SPELLS)
    SummonerSpellList static_getSpellsList(@Path("region") String region, @Query("dataById") boolean dataById, @Query("version") String version, @Query("spellData") String spellData);

    //static data
    @GET(API.STATIC_REALM)
    RealmDTO getRealm(@Path("region") String region);

}
