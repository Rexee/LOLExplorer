package tsingularity.lolexplorer;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import retrofit.RetrofitError;
import tsingularity.lolexplorer.API.API;
import tsingularity.lolexplorer.API.API_REST;
import tsingularity.lolexplorer.API.API_Static_REST;
import tsingularity.lolexplorer.Model.ChampionIconStatic;
import tsingularity.lolexplorer.Model.Constants.ConstantsCommon;
import tsingularity.lolexplorer.Model.Constants.Servers.Server;
import tsingularity.lolexplorer.Model.CurrentGame;
import tsingularity.lolexplorer.Model.CurrentGameParticipant;
import tsingularity.lolexplorer.Model.DTO.Game.CurrentGameDTO;
import tsingularity.lolexplorer.Model.DTO.Game.Game;
import tsingularity.lolexplorer.Model.DTO.Game.RecentGames;
import tsingularity.lolexplorer.Model.DTO.League.LeagueDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchSummaryDTO;
import tsingularity.lolexplorer.Model.DTO.Match.MatchesDTO;
import tsingularity.lolexplorer.Model.DTO.StaticData.Champion.Champion;
import tsingularity.lolexplorer.Model.DTO.StaticData.Champion.ChampionList;
import tsingularity.lolexplorer.Model.DTO.StaticData.Item.Item;
import tsingularity.lolexplorer.Model.DTO.StaticData.Item.ItemList;
import tsingularity.lolexplorer.Model.DTO.StaticData.ProfileIcons.ProfileIconsDTO;
import tsingularity.lolexplorer.Model.DTO.StaticData.ProfileIcons.ProfileIconsData;
import tsingularity.lolexplorer.Model.DTO.StaticData.Realm.RealmDTO;
import tsingularity.lolexplorer.Model.DTO.StaticData.SummonerSpell.SummonerSpell;
import tsingularity.lolexplorer.Model.DTO.StaticData.SummonerSpell.SummonerSpellList;
import tsingularity.lolexplorer.Model.DTO.Stats.ChampionStats;
import tsingularity.lolexplorer.Model.DTO.Stats.PlayerStatsSummary;
import tsingularity.lolexplorer.Model.DTO.Stats.StatsRankedDTO;
import tsingularity.lolexplorer.Model.DTO.Stats.StatsSummaryDTO;
import tsingularity.lolexplorer.Model.DTO.Summoner.SummonerDTO;
import tsingularity.lolexplorer.Model.ItemsStatic;
import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Model.Match;
import tsingularity.lolexplorer.Model.MatchHistory;
import tsingularity.lolexplorer.Model.MatchParticipant;
import tsingularity.lolexplorer.Model.ProfileIcons;
import tsingularity.lolexplorer.Model.Realm;
import tsingularity.lolexplorer.Model.StatsRanked;
import tsingularity.lolexplorer.Model.StatsSummary;
import tsingularity.lolexplorer.Model.Summoner;
import tsingularity.lolexplorer.Model.SummonerSpellStatic;
import tsingularity.lolexplorer.Util.S;

import static cupboard.CupboardFactory.cupboard;

public class REST_Service extends IntentService {

    public static final String TAG                    = "Service ";
    public static final int    SLEEP_TIME             = 1000;
    public static final String API_SERVER_HOST        = "summoner.service.API_SERVER_HOST";
    public static final String API_FUNCTION_NAME      = "summoner.service.API_FUNCTION_NAME";
    public static final String API_FUNCTION_ARGUMENT1 = "summoner.service.API_FUNCTION_ARGUMENT1";
    public static final String API_FUNCTION_ARGUMENT2 = "summoner.service.API_FUNCTION_ARGUMENT2";
    public static final String API_FUNCTION_ARGUMENT3 = "summoner.service.API_FUNCTION_ARGUMENT3";
    public static final String SERVICE_CONTEXT        = "summoner.service.SERVICE_CONTEXT";
    public static final String SERVICE_RESULT_STATUS  = "summoner.service.SERVICE_RESULT_STATUS";
    public static final String SERVICE_RESULT_CODE    = "summoner.service.SERVICE_RESULT_CODE";
    public static final String SERVICE_RESULT_STRING  = "summoner.service.SERVICE_RESULT_STRING";
    public static final String SERVICE_RESULT_EXTRA   = "summoner.service.SERVICE_RESULT_EXTRA";
    public static final int    STATUS_OK              = 0;
    public static final int    STATUS_ERROR           = -1;
    public static final String DATA_NOT_CHANGED       = "DATA_NOT_CHANGED";
    public  API_REST REST;
    private int      mCurrentContext;
    private int      mApplicationContext;

    public REST_Service() {
        super("REST_Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mApplicationContext = intent.getIntExtra(SERVICE_CONTEXT, Settings.ACTIVITY_UNKNOWN);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApplicationContext = Settings.ACTIVITY_STOP_ALL;
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {

        mCurrentContext = requestIntent.getIntExtra(SERVICE_CONTEXT, Settings.ACTIVITY_UNKNOWN);
        if (mCurrentContext != mApplicationContext) return;

        String host = requestIntent.getStringExtra(API_SERVER_HOST);
        String functionId = requestIntent.getStringExtra(API_FUNCTION_NAME);
        String functionArgument1 = requestIntent.getStringExtra(API_FUNCTION_ARGUMENT1);
        String functionArgument2 = requestIntent.getStringExtra(API_FUNCTION_ARGUMENT2);
        String functionArgument3 = requestIntent.getStringExtra(API_FUNCTION_ARGUMENT3);

        if (REST == null) {
            REST = new API_REST(host);
        } else REST.setServer(host);

        switch (functionId) {
            case API.REQUEST_SUMMONER_DATA_BY_NAME:
                getSummonerByName(functionArgument1);
                break;
            case API.REQUEST_SUMMONER_DATA_BY_ID:
                getSummonerById(functionArgument1);
                break;
            case API.REQUEST_LEAGUE:
                getLeague(functionArgument1);
                break;
            case API.REQUEST_MOST_PLAYED_RANKED:
                getMostPlayedRanked(functionArgument1);
                break;
            case API.REQUEST_STATS_SUMMARY:
                getStatsSummary(functionArgument1);
                break;
            case API.REQUEST_MATCH_HISTORY_RANKED:
                getMatchHistoryRanked(functionArgument1);
                break;
            case API.REQUEST_UPDATE_STATIC_DATA:
                updateStaticData();
                break;
            case API.REQUEST_STATIC_DATA_CDN:
                getStaticData_CDN();
                break;
            case API.REQUEST_STATIC_DATA_PROFILE_ICONS:
                getStaticData_ProfileIcons(functionArgument1, functionArgument2, functionArgument3);
                break;
            case API.REQUEST_STATIC_DATA_CHAMPIONS:
                getStaticData_Champions(functionArgument1);
                break;
            case API.REQUEST_STATIC_DATA_ITEMS:
                getStaticData_Items(functionArgument1);
                break;
            case API.REQUEST_STATIC_DATA_SUMMONERS:
                getStaticData_SummonerSpells(functionArgument1);
                break;
            case API.REQUEST_MATCH:
                getMatch(functionArgument1);
                break;
            case API.REQUEST_MATCH_HISTORY_ALL:
                getMatchHistoryAll(functionArgument1);
                break;
            case API.REQUEST_CURRENT_GAME:
                getCurrentGame(functionArgument1);
                break;
            case API.REQUEST_IS_CURRENT_GAME_ACTIVE:
                checkIsCurrentGameActive(functionArgument1);
                break;
            default:
                break;
        }
    }

    private void checkIsCurrentGameActive(String summonerId) {
        CurrentGameDTO currentGameDTO;
        try {
            currentGameDTO = REST.getCurrentGame(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_IS_CURRENT_GAME_ACTIVE, error, summonerId);
            return;
        }

//        S.L(TAG + " checkIsCurrentGameActive. success: " + summonerId);

        if (currentGameDTO == null) return;

        CurrentGame currentGame = CurrentGame.fromDTO(currentGameDTO);

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();
        cupboard().withDatabase(db).updateInsert(currentGame);
        database.close();

        sendResultBroadcast_Success(API.REQUEST_IS_CURRENT_GAME_ACTIVE, Long.toString(currentGame.matchId));
    }

    void getCurrentGame(String summonerId) {
        CurrentGameDTO currentGameDTO;
        try {
            currentGameDTO = REST.getCurrentGame(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_CURRENT_GAME, error, summonerId);
            return;
        }

//        S.L(TAG + " checkIsCurrentGameActive. success: " + summonerId);

        if (currentGameDTO == null) return;

        CurrentGame currentGame = CurrentGame.fromDTO(currentGameDTO);
        List<CurrentGameParticipant> currentGameParticipantList = CurrentGame.participantListFromDTO(currentGameDTO);

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        cupboard().withDatabase(db).updateInsert(currentGame);

        int maxI = currentGameParticipantList.size();
        for (int i = 0; i < maxI; i++) {
            CurrentGameParticipant matchParticipant = currentGameParticipantList.get(i);
            cupboard().withDatabase(db).updateInsert(matchParticipant);
        }

        database.close();

        sendResultBroadcast_Success(API.REQUEST_CURRENT_GAME_REFRESH, Long.toString(currentGame.matchId));

        for (int i = 0; i < maxI; i++) {
            if (mCurrentContext != mApplicationContext) return;

            CurrentGameParticipant matchParticipant = currentGameParticipantList.get(i);

            getLeague(Long.toString(matchParticipant.summonerId));
            getMostPlayedRanked(Long.toString(matchParticipant.summonerId));
        }

        sendResultBroadcast_Success(API.REQUEST_CURRENT_GAME, Long.toString(currentGame.matchId));

    }

    void getMatchHistoryAll(String summonerId) {
        RecentGames matches_all;
        try {
            matches_all = REST.getMatchHistoryAll(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_MATCH_HISTORY_ALL, error, summonerId);
            return;
        }

//        S.L(TAG + " getMatchHistoryAll. success: " + summonerId);

        if (matches_all == null) return;

        Iterator<Game> iterator = matches_all.games.iterator();
        Game entry;

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        while (iterator.hasNext()) {
            entry = iterator.next();

            MatchHistory matchHistory = MatchHistory.fromDTO(entry, matches_all.summonerId);

            cupboard().withDatabase(db).updateInsert(matchHistory);

            //add only if not exist match
            Match res = cupboard().withDatabase(db).query(Match.class).withSelection("matchId = ?", Long.toString(entry.gameId)).limit(1).get();
            if (res == null) {

                List<MatchParticipant> matchParticipantList = MatchHistory.participantListFromDTO(entry, matches_all.summonerId);
                int maxI = matchParticipantList.size();
                for (int i = 0; i < maxI; i++) {
                    MatchParticipant matchParticipant = matchParticipantList.get(i);
                    cupboard().withDatabase(db).updateInsert(matchParticipant);
                }
            }
        }

        database.close();

        sendResultBroadcast_Success(API.REQUEST_MATCH_HISTORY_ALL, summonerId);
    }

    List<Summoner> getSummonersById(String summonersId) {
        List<Summoner> res = new ArrayList<>();

        Map<String, SummonerDTO> summonerData;
        try {
            summonerData = REST.getSummonerDataById(summonersId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_SUMMONER_DATA_BY_ID, error, summonersId);
            return null;
        }

//        S.L(TAG + " getSummonersById. success");

        if (summonerData == null || summonerData.size() == 0) return null;

        SummonerDTO summonerDTO;

        Iterator<Entry<String, SummonerDTO>> iterator = summonerData.entrySet().iterator();
        Map.Entry<String, SummonerDTO> entry;

        while (iterator.hasNext()) {
            entry = iterator.next();
            summonerDTO = entry.getValue();

            Summoner summoner = Summoner.fromDTO(summonerDTO, REST.mHost);

            DB.putSummoner(REST_Service.this, summoner);
            res.add(summoner);

        }

        return res;
    }

    void getSummonerById(String summonerId) {

        List<Summoner> listSummoners = getSummonersById(summonerId);
        if (listSummoners == null) return;

        String summonersRes = "";
        for (Summoner curSummoner : listSummoners) {
            summonersRes = summonersRes + ConstantsCommon.SEPARATOR + Long.toString(curSummoner.summonerId);
        }
        summonersRes = summonersRes.substring(ConstantsCommon.SEPARATOR.length());

        sendResultBroadcast_Success(API.REQUEST_SUMMONER_DATA_BY_ID, summonersRes);
    }

    void getStaticData_SummonerSpells(String version) {
        SummonerSpellList summonerSpellList;
        try {
            summonerSpellList = REST.static_getSummonersSpellsList(version, "image");
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_STATIC_DATA_SUMMONERS, error, "");
            return;
        }

//        S.L(TAG + " getStaticData_SummonerSpells. success");

        Iterator<Entry<String, SummonerSpell>> iterator = summonerSpellList.data.entrySet().iterator();
        Map.Entry<String, SummonerSpell> entry;

        SummonerSpell summonerSpell;

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        while (iterator.hasNext()) {
            entry = iterator.next();
            summonerSpell = entry.getValue();

            SummonerSpellStatic summonerSpellStatic = SummonerSpellStatic.fromDTO(summonerSpell);

            cupboard().withDatabase(db).updateInsert(summonerSpellStatic);

        }

        db.close();
        database.close();

        sendResultBroadcast_Success(API.REQUEST_STATIC_DATA_SUMMONERS);
    }

    void getStaticData_Items(String version) {
        ItemList itemList;
        try {
            itemList = REST.static_getItemsList(version, "image");
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_STATIC_DATA_ITEMS, error, "");
            return;
        }

//        S.L(TAG + " getStaticData_Items. success");

        Iterator<Entry<String, Item>> iterator = itemList.data.entrySet().iterator();
        Map.Entry<String, Item> entry;

        Item item;

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        while (iterator.hasNext()) {
            entry = iterator.next();
            item = entry.getValue();

            ItemsStatic itemsStatic = ItemsStatic.fromDTO(item);

            cupboard().withDatabase(db).updateInsert(itemsStatic);

        }

        db.close();
        database.close();

        sendResultBroadcast_Success(API.REQUEST_STATIC_DATA_ITEMS);
    }

    void getStaticData_Champions(String version) {

        ChampionList championList;
        try {
            championList = REST.static_getChampionsList(version, "image");
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_STATIC_DATA_CHAMPIONS, error, "");
            return;
        }

//        S.L(TAG + " getStaticData_Champions. success");

        Iterator<Entry<String, Champion>> iterator = championList.data.entrySet().iterator();
        Map.Entry<String, Champion> entry;

        Champion champion;

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        while (iterator.hasNext()) {
            entry = iterator.next();
            champion = entry.getValue();

            ChampionIconStatic championIconStatic = ChampionIconStatic.fromDTO(champion);

            cupboard().withDatabase(db).updateInsert(championIconStatic);

        }

        db.close();
        database.close();

        sendResultBroadcast_Success(API.REQUEST_STATIC_DATA_CHAMPIONS);
    }

    void getMatch(String matchId) {

        MatchDTO matchDTO;
        try {
            matchDTO = REST.getMatch(matchId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_MATCH, error, matchId);
            return;
        }

//        S.L(TAG + " getMatch. success");

        if (matchDTO == null) return;

        Match match = Match.fromDTO(matchDTO);
        List<MatchParticipant> matchParticipantList = Match.participantListFromDTO(matchDTO);

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        cupboard().withDatabase(db).updateInsert(match);

        //get existing and update with "summonerId"
        List<MatchParticipant> existingParticipants = cupboard().withDatabase(db).query(MatchParticipant.class).withSelection("matchId = ? AND summonerId <> 0", matchId).list();

        String summonersToGetNames = "";

        int maxI = matchParticipantList.size();
        for (int i = 0; i < maxI; i++) {
            MatchParticipant matchParticipant = matchParticipantList.get(i);
            if (matchParticipant.summonerId == 0) {
                for (MatchParticipant curExistP : existingParticipants) {
                    if (curExistP.championId == matchParticipant.championId && curExistP.teamId == matchParticipant.teamId) {
                        matchParticipant.summonerId = curExistP.summonerId;
                        matchParticipant.summonerName = curExistP.summonerName;
                        break;
                    }
                }
            }

            if (matchParticipant.summonerName == null || matchParticipant.summonerName.isEmpty()) {
                String summonerId = Long.toString(matchParticipant.summonerId);

                Summoner summoner = cupboard().withDatabase(db).query(Summoner.class).withSelection("summonerId = ?", summonerId).get();
                if (summoner == null) summonersToGetNames = summonersToGetNames + summonerId + ConstantsCommon.SEPARATOR;
                else matchParticipant.summonerName = summoner.name;
            }

            cupboard().withDatabase(db).updateInsert(matchParticipant);
        }

        sendResultBroadcast_Success(API.REQUEST_MATCH, matchId);

        if (!summonersToGetNames.isEmpty()) {
            List<Summoner> listSummoners = getSummonersById(summonersToGetNames);
            if (listSummoners == null) return;
            for (MatchParticipant matchParticipant : matchParticipantList) {
                if (matchParticipant.summonerName != null) continue;

                for (Summoner curSummoner : listSummoners) {
                    if (curSummoner.summonerId == matchParticipant.summonerId) {
                        matchParticipant.summonerName = curSummoner.name;
                        cupboard().withDatabase(db).updateInsert(matchParticipant);
                    }
                }
            }

            sendResultBroadcast_Success(API.REQUEST_SUMMONER_DATA_BY_ID, summonersToGetNames);

        }

        database.close();

    }

    void getStaticData_ProfileIcons(String baseCDN, String version, String locale) {

        API_Static_REST REST_Static = new API_Static_REST(baseCDN);

        ProfileIconsDTO profileIcons;
        try {
            profileIcons = REST_Static.mService.getProfileIconJSON(version, locale);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_STATIC_DATA_PROFILE_ICONS, error, "");
            return;
        }

//        S.L(TAG + " getStaticData_ProfileIcons. success");

        Iterator<Entry<String, ProfileIconsData>> iterator = profileIcons.data.entrySet().iterator();
        Map.Entry<String, ProfileIconsData> entry;

        ProfileIconsData profileIconsData;

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        while (iterator.hasNext()) {
            entry = iterator.next();
            profileIconsData = entry.getValue();

            ProfileIcons profileIcon = ProfileIcons.fromDTO(profileIconsData);

            cupboard().withDatabase(db).updateInsert(profileIcon);

        }

        database.close();

        sendResultBroadcast_Success(API.REQUEST_STATIC_DATA_PROFILE_ICONS);

    }

    void getStaticData_CDN() {

        RealmDTO realmDTO;
        try {
            realmDTO = REST.getRealm();
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_STATIC_DATA_CDN, error, "");
            return;
        }

//        S.L(TAG + " getStaticData_CDN. success");

        Realm realm = Realm.fromDTO(realmDTO);

        DB.putRealm(REST_Service.this, realm);
        sendResultBroadcast_Success(API.REQUEST_STATIC_DATA_CDN);

    }

    void updateStaticData() {

        RealmDTO realmDTO;
        try {
            realmDTO = REST.setEndpoint(Server.GLOBAL).getRealm();
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_UPDATE_STATIC_DATA, error, "");
            return;
        }

//        S.L(TAG + " updateStaticData. success");

        Realm realm = Realm.fromDTO(realmDTO);
        Realm realmDB = DB.getRealm(REST_Service.this);

        if (realmDB != null && realm.version.equals(realmDB.version)) {
            sendResultBroadcast_Success(API.REQUEST_UPDATE_STATIC_DATA, DATA_NOT_CHANGED);
            return; //realm data is not changed
        }

        DB.putRealm(REST_Service.this, realm);

        sendResultBroadcast_Success(API.REQUEST_UPDATE_STATIC_DATA);

        getStaticData_ProfileIcons(realm.cdn, realm.profileiconVer, realm.language);
        getStaticData_Champions(realm.championVer);
        getStaticData_Items(realm.itemsVer);
        getStaticData_SummonerSpells(realm.summonerSpellsVer);

    }

    void getSummonerByName(String summonerName) {

        Map<String, SummonerDTO> summonerData;

        try {
            summonerData = REST.getSummonerDataByName(summonerName);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_SUMMONER_DATA_BY_NAME, error, summonerName);
            return;
        }

//        S.L(TAG + " getSummonerByName. success");

        if (summonerData == null || summonerData.size() == 0) return;

        SummonerDTO summonerDTO;

        Iterator<Entry<String, SummonerDTO>> iterator = summonerData.entrySet().iterator();
        Map.Entry<String, SummonerDTO> entry;

        while (iterator.hasNext()) {
            entry = iterator.next();
            summonerDTO = entry.getValue();

            Summoner summoner = Summoner.fromDTO(summonerDTO, REST.mHost);

            DB.putSummoner(REST_Service.this, summoner);
            sendResultBroadcast_Success(API.REQUEST_SUMMONER_DATA_BY_NAME, Long.toString(summoner.summonerId));

        }
    }

    void getLeague(String summonerId) {

//        NotificationManager.addMessage(TAG + "getLeague: " + summonerId, API.REQUEST_LEAGUE);

        Map<String, List<LeagueDTO>> leaguesData;

        try {
            leaguesData = REST.getLeagueBySummoner(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_LEAGUE, error, summonerId);
            return;
        }

//        S.L(TAG + " getLeague. success");

        if (leaguesData == null || leaguesData.size() == 0) return;

        List<LeagueDTO> leagues;

        Iterator<Entry<String, List<LeagueDTO>>> iterator = leaguesData.entrySet().iterator();
        Map.Entry<String, List<LeagueDTO>> entry;

        boolean updated = false;
        while (iterator.hasNext()) {
            entry = iterator.next();
            String summonerId_DTO = entry.getKey();
            leagues = entry.getValue();

            for (LeagueDTO leagueDTO : leagues) {

                League league = League.fromDTO(summonerId_DTO, leagueDTO);
                updated = DB.putLeague(REST_Service.this, league);
            }
        }

        if (updated) sendResultBroadcast_Success(API.REQUEST_LEAGUE, summonerId);

    }

    void getMostPlayedRanked(String summonerId) {
//        NotificationManager.addMessage(TAG + "getMostPlayedRanked: " + summonerId, API.REQUEST_MOST_PLAYED_RANKED);

        StatsRankedDTO statsRankedDTO;
        try {
            statsRankedDTO = REST.getStatsRanked(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_MOST_PLAYED_RANKED, error, summonerId);
            return;
        }

//        S.L(TAG + " getMostPlayedRanked. success");

        if (statsRankedDTO == null) return;

        long summonerId_DTO = statsRankedDTO.summonerId;
        long modifyDate = statsRankedDTO.modifyDate;
        boolean updated = false;

        int maxI = statsRankedDTO.champions.size();
        for (int i = 0; i < maxI; i++) {
            ChampionStats entry = statsRankedDTO.champions.get(i);
            StatsRanked statsRanked = StatsRanked.fromDTO(summonerId_DTO, modifyDate, entry);

            updated = DB.putStatsRanked(REST_Service.this, statsRanked);

        }

        if (updated) sendResultBroadcast_Success(API.REQUEST_MOST_PLAYED_RANKED, summonerId);

    }

    void getMatchHistoryRanked(String summonerId) {

        MatchesDTO matchesDTO;
        try {
            matchesDTO = REST.getMatchHistoryRankedSolo(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_MATCH_HISTORY_RANKED, error, summonerId);
            return;
        }

//        S.L(TAG + " getMatchHistoryRanked. success: " + summonerId);

        if (matchesDTO == null) return;

        int maxI = matchesDTO.matches.size();

        DB database = new DB(REST_Service.this);
        SQLiteDatabase db = database.getWritableDatabase();

        for (int i = 0; i < maxI; i++) {
            MatchSummaryDTO entry = matchesDTO.matches.get(i);

            MatchHistory matchHistory = MatchHistory.fromDTO(entry);

            cupboard().withDatabase(db).updateInsert(matchHistory);
        }

        database.close();

        sendResultBroadcast_Success(API.REQUEST_MATCH_HISTORY_RANKED, summonerId);
    }

    void getStatsSummary(String summonerId) {

        StatsSummaryDTO statsSummaryDTO;
        try {
            statsSummaryDTO = REST.getStatsSummary(summonerId);
        } catch (RetrofitError error) {
            REST_ErrorHandling(API.REQUEST_STATS_SUMMARY, error, summonerId);
            return;
        }

//        S.L(TAG + " getStatsSummary. success");

        if (statsSummaryDTO == null) return;

        long summonerId_DTO = statsSummaryDTO.summonerId;
        boolean updated = false;

        if (statsSummaryDTO.playerStatSummaries != null) {
            int maxI = statsSummaryDTO.playerStatSummaries.size();
            for (int i = 0; i < maxI; i++) {
                PlayerStatsSummary entry = statsSummaryDTO.playerStatSummaries.get(i);
                StatsSummary statsSummary = StatsSummary.fromDTO(summonerId_DTO, entry);

                updated = DB.putStatsSummary(REST_Service.this, statsSummary);
            }
        }

        if (updated) sendResultBroadcast_Success(API.REQUEST_STATS_SUMMARY, summonerId);
    }

    void REST_ErrorHandling(String requestType, RetrofitError error, String resultArg) {

        REST_Response response = REST_Response.getResponseString(error, requestType);
        S.L(TAG + " sendResultBroadcast. Failure: " + requestType + ", code:" + response.code + ", detail: " + response.description);
        sendResultBroadcast_Failure(requestType, response, resultArg);

        if (response.code == REST_Response.RATE_LIMIT_EXCEEDED && mCurrentContext == mApplicationContext) {
            S.L("RATE_LIMIT_EXCEEDED: SLEEP ITERATION. CC:" + mCurrentContext + " APPC:" + mApplicationContext);
            SystemClock.sleep(SLEEP_TIME);
            switch (requestType) {
                case API.REQUEST_SUMMONER_DATA_BY_NAME:
                    getSummonerByName(resultArg);
                    break;
                case API.REQUEST_SUMMONER_DATA_BY_ID:
                    getSummonersById(resultArg);
                    break;
                case API.REQUEST_LEAGUE:
                    getLeague(resultArg);
                    break;
                case API.REQUEST_MOST_PLAYED_RANKED:
                    getMostPlayedRanked(resultArg);
                    break;
                case API.REQUEST_IS_CURRENT_GAME_ACTIVE:
                    checkIsCurrentGameActive(resultArg);
                    break;
                case API.REQUEST_CURRENT_GAME:
                    getCurrentGame(resultArg);
                    break;
                case API.REQUEST_MATCH_HISTORY_ALL:
                    getMatchHistoryAll(resultArg);
                    break;
                case API.REQUEST_MATCH:
                    getMatch(resultArg);
                    break;
                case API.REQUEST_MATCH_HISTORY_RANKED:
                    getMatchHistoryRanked(resultArg);
                    break;
                case API.REQUEST_STATS_SUMMARY:
                    getStatsSummary(resultArg);
                    break;
            }
        }
    }

    void sendResultBroadcast_Failure(String requestType, REST_Response response, String resultArg) {
        Intent resultIntent = new Intent(requestType);
        resultIntent.putExtra(SERVICE_RESULT_STATUS, STATUS_ERROR);
        resultIntent.putExtra(SERVICE_RESULT_CODE, response.code);
        resultIntent.putExtra(SERVICE_RESULT_STRING, response.description);
        resultIntent.putExtra(SERVICE_RESULT_EXTRA, resultArg);
        resultIntent.putExtra(SERVICE_CONTEXT, mCurrentContext);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    void sendResultBroadcast_Success(String requestType, String resultArg) {
        S.L(TAG + " sendResultBroadcast. Success: " + requestType);

        Intent resultIntent = new Intent(requestType);
        resultIntent.putExtra(SERVICE_RESULT_STATUS, STATUS_OK);
        resultIntent.putExtra(SERVICE_RESULT_STRING, "");
        resultIntent.putExtra(SERVICE_RESULT_EXTRA, resultArg);
        resultIntent.putExtra(SERVICE_CONTEXT, mCurrentContext);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    void sendResultBroadcast_Success(String intentType) {
        sendResultBroadcast_Success(intentType, "");
    }
}
