package tsingularity.lolexplorer.API;

public class API {

    public static final String ROOT                              = "https://%s.api.pvp.net";
    public static final String KEY_PARAM                         = "api_key";
    //champion
    public static final String LIST_CHAMPIONS                    = "/api/lol/{region}/v1.2/champion";
    public static final String CHAMPION                          = "/api/lol/{region}/v1.2/champion/{id}";
    //current-game
    public static final String CURRENT_GAME                      = "/observer-mode/rest/consumer/getSpectatorGameInfo/{platformId}/{summonerId}";
    //featured-games
    public static final String FEATURED_GAMES                    = "/observer-mode/rest/featured";
    //league
    public static final String LEAGUE_BY_SUMMONER_ALL            = "/api/lol/{region}/v2.5/league/by-summoner/{summonerIds}";
    public static final String LEAGUE_BY_SUMMONER                = "/api/lol/{region}/v2.5/league/by-summoner/{summonerIds}/entry";
    public static final String LEAGUE_BY_TEAM_ALL                = "/api/lol/{region}/v2.5/league/by-team/{teamIds}";
    public static final String LEAGUE_BY_TEAM                    = "/api/lol/{region}/v2.5/league/by-team/{teamIds}/entry";
    public static final String LEAGUE_CHALLENGER                 = "/api/lol/{region}/v2.5/league/challenger";
    //static-data
//    https://global.api.pvp.net/api/lol/static-data/eune/v1.2/realm
    public static final String STATIC_CHAMPIONS_DATA             = "/api/lol/static-data/{region}/v1.2/champion";
    public static final String STATIC_CHAMPION                   = "/api/lol/static-data/{region}/v1.2/champion/{id}";
    public static final String STATIC_ITEMS                      = "/api/lol/static-data/{region}/v1.2/item";
    public static final String STATIC_ITEM                       = "/api/lol/static-data/{region}/v1.2/item/{id}";
    public static final String STATIC_STRINGS                    = "/api/lol/static-data/{region}/v1.2/language-strings";
    public static final String STATIC_LANGUAGES                  = "/api/lol/static-data/{region}/v1.2/languages";
    public static final String STATIC_MAP                        = "/api/lol/static-data/{region}/v1.2/map";
    public static final String STATIC_MASTERIES                  = "/api/lol/static-data/{region}/v1.2/mastery";
    public static final String STATIC_MASTERY                    = "/api/lol/static-data/{region}/v1.2/mastery/{id}";
    public static final String STATIC_REALM                      = "/api/lol/static-data/{region}/v1.2/realm";
    public static final String STATIC_RUNES                      = "/api/lol/static-data/{region}/v1.2/rune";
    public static final String STATIC_RUNE                       = "/api/lol/static-data/{region}/v1.2/rune/{id}";
    public static final String STATIC_SPELLS                     = "/api/lol/static-data/{region}/v1.2/summoner-spell";
    public static final String STATIC_SPELL                      = "/api/lol/static-data/{region}/v1.2/summoner-spell/{id}";
    public static final String STATIC_VERSIONS                   = "/api/lol/static-data/{region}/v1.2/versions";
    //status
    public static final String SERVERS_LIST                      = "/shards";
    public static final String SERVERS_STATUS                    = "/shards/{region}";
    //match
    public static final String MATCH                             = "/api/lol/{region}/v2.2/match/{matchId}";
    //matchhistory
    public static final String MATCH_HISTORY_RANKED              = "/api/lol/{region}/v2.2/matchhistory/{summonerId}";
    public static final String MATCH_HISTORY_ALL                 = "/api/lol/{region}/v1.3/game/by-summoner/{summonerId}/recent";
    //stats
    public static final String STATS_RANKED                      = "/api/lol/{region}/v1.3/stats/by-summoner/{summonerId}/ranked";
    public static final String STATS_SUMMARY                     = "/api/lol/{region}/v1.3/stats/by-summoner/{summonerId}/summary";
    //summoner
    public static final String SUMMONER_DATA_BY_NAME             = "/api/lol/{region}/v1.4/summoner/by-name/{summonerNames}";
    public static final String SUMMONER_DATA                     = "/api/lol/{region}/v1.4/summoner/{summonerIds}";
    public static final String SUMMONER_NAME                     = "/api/lol/{region}/v1.4/summoner/{summonerIds}/name";
    public static final String SUMMONER_MASTERIES                = "/api/lol/{region}/v1.4/summoner/{summonerIds}/masteries";
    public static final String SUMMONER_RUNES                    = "/api/lol/{region}/v1.4/summoner/{summonerIds}/runes";
    //team
    public static final String TEAMS_BY_SUMMONER                 = "/api/lol/{region}/v2.4/team/by-summoner/{summonerIds}";
    public static final String TEAM_BY_TEAM                      = "/api/lol/{region}/v2.4/team/{teamIds}";
    //ID's
    //summoner
    public static final String REQUEST_SUMMONER_DATA_BY_NAME     = "service.SUMMONER_DATA_BY_NAME";
    public static final String REQUEST_SUMMONER_DATA_BY_ID       = "service.SUMMONER_DATA_BY_ID";
    public static final String REQUEST_MOST_PLAYED_RANKED        = "service.STATS_RANKED";
    public static final String REQUEST_LEAGUE                    = "service.LEAGUE";
    public static final String REQUEST_STATS_SUMMARY             = "service.SUMMARY";
    public static final String REQUEST_MATCH_HISTORY_RANKED      = "service.MATCH_HISTORY_RANKED";
    public static final String REQUEST_MATCH_HISTORY_ALL         = "service.MATCH_HISTORY_ALL";
    public static final String REQUEST_UPDATE_STATIC_DATA        = "service.STATIC_DATA_UPDATE";
    public static final String REQUEST_STATIC_DATA_CDN           = "service.STATIC_DATA_CDN";
    public static final String REQUEST_STATIC_DATA_PROFILE_ICONS = "service.STATIC_DATA_PROFILE_ICONS";
    public static final String REQUEST_STATIC_DATA_CHAMPIONS     = "service.STATIC_DATA_CHAMPIONS";
    public static final String REQUEST_STATIC_DATA_ITEMS         = "service.STATIC_DATA_ITEMS";
    public static final String REQUEST_STATIC_DATA_SUMMONERS     = "service.STATIC_DATA_SUMMONERS";
    //match
    public static final String REQUEST_MATCH                     = "service.MATCH_SUMMARY";
    //current game
    public static final String REQUEST_CURRENT_GAME_REFRESH      = "service.CURRENT_GAME_REFRESH";//notify about finishing first request
    public static final String REQUEST_CURRENT_GAME              = "service.CURRENT_GAME";
    public static final String REQUEST_IS_CURRENT_GAME_ACTIVE    = "service.IS_CURRENT_GAME_ACTIVE";

    public static String root(String server) {
        return String.format(ROOT, server);
    }
}
