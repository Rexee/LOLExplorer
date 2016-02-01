package tsingularity.lolexplorer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import cupboard.CupboardBuilder;
import cupboard.CupboardFactory;
import tsingularity.lolexplorer.Model.ChampionIconStatic;
import tsingularity.lolexplorer.Model.CurrentGame;
import tsingularity.lolexplorer.Model.CurrentGameParticipant;
import tsingularity.lolexplorer.Model.DTO.Game.RecentGames;
import tsingularity.lolexplorer.Model.ItemsStatic;
import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Model.Match;
import tsingularity.lolexplorer.Model.MatchHistory;
import tsingularity.lolexplorer.Model.MatchParticipant;
import tsingularity.lolexplorer.Model.ProfileIcons;
import tsingularity.lolexplorer.Model.Realm;
import tsingularity.lolexplorer.Model.SearchHistory;
import tsingularity.lolexplorer.Model.StatsRanked;
import tsingularity.lolexplorer.Model.StatsSummary;
import tsingularity.lolexplorer.Model.Summoner;
import tsingularity.lolexplorer.Model.SummonerSpellStatic;

import static cupboard.CupboardFactory.cupboard;

public class DB extends SQLiteOpenHelper {
    public static final String DB_NAME    = "main.db";
    public static final int    DB_VERSION = 65;

    static {
        CupboardFactory.setCupboard(new CupboardBuilder().useAnnotations().build());
        cupboard().register(League.class);
        cupboard().register(Summoner.class);
        cupboard().register(StatsRanked.class);
        cupboard().register(StatsSummary.class);
        cupboard().register(Realm.class);
        cupboard().register(ProfileIcons.class);
        cupboard().register(ChampionIconStatic.class);
        cupboard().register(SummonerSpellStatic.class);
        cupboard().register(ItemsStatic.class);
        cupboard().register(MatchHistory.class);
        cupboard().register(Match.class);
        cupboard().register(MatchParticipant.class);
        cupboard().register(CurrentGame.class);
        cupboard().register(CurrentGameParticipant.class);
        cupboard().register(SearchHistory.class);
    }

    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static boolean putLeague(Context context, League league) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean res = cupboard().withDatabase(db).updateInsert(league);

        database.close();
        return res;

    }

    public static Summoner getSummoner(Context context, String summonerId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        Summoner res = cupboard().withDatabase(db).query(Summoner.class).withSelection("summonerId = ?", summonerId).limit(1).get();

        database.close();
        return res;
    }

    public static Summoner getSummonerByName(Context context, String summonerName) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        Summoner res = cupboard().withDatabase(db).query(Summoner.class).withSelection("name = ? OR nameSimple = ?", summonerName, summonerName).limit(1).get();

        database.close();
        return res;
    }

    public static List<League> getLeagues(Context context, long summonerId) {
        return getLeagues(context, Long.toString(summonerId));
    }

    public static List<League> getLeagues(Context context, String summonerId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<League> res = cupboard().withDatabase(db).query(League.class).withSelection("summonerId = ?", summonerId).list();

        database.close();
        return res;
    }

    public static List<StatsRanked> getRankedStats(Context context, String summonerId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<StatsRanked> res = cupboard().withDatabase(db).query(StatsRanked.class).
                withSelection("summonerId = ? AND championId > 0", summonerId).orderBy("totalSessionsPlayed DESC").limit(5).list();

        database.close();
        return res;
    }

    public static List<StatsRanked> getRankedStatsByChampionAndTotal(Context context, String summonerId, int championId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<StatsRanked> res = cupboard().withDatabase(db).query(StatsRanked.class).
                withSelection("summonerId = ? AND (championId = ? OR championId = 0)", summonerId, Integer.toString(championId)).orderBy("totalSessionsPlayed DESC").limit(2).list();

        database.close();
        return res;
    }

    public static boolean putStatsRanked(Context context, StatsRanked statsRanked) {

        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean res = cupboard().withDatabase(db).updateInsert(statsRanked);

        database.close();
        return res;
    }

    public static boolean putStatsSummary(Context context, StatsSummary statsSummary) {

        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean res = cupboard().withDatabase(db).updateInsert(statsSummary);

        database.close();
        return res;
    }

    public static boolean putSummoner(Context context, Summoner summoner) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean res = cupboard().withDatabase(db).updateInsert(summoner);

        database.close();
        return res;
    }

    public static boolean putRealm(Context context, Realm realm) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean res = cupboard().withDatabase(db).updateInsert(realm);

        database.close();
        return res;
    }

    public static Realm getRealm(Context context) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        Realm res = cupboard().withDatabase(db).query(Realm.class).withSelection("id = 0").limit(1).get();

        database.close();
        return res;
    }

    public static List<ProfileIcons> getProfileIconList(Context context) {

        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<ProfileIcons> res = cupboard().withDatabase(db).query(ProfileIcons.class).list();

        database.close();
        return res;
    }

    public static List<MatchHistory> getMatchHistory(Context context, String summonerId) {

        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<MatchHistory> res = cupboard().withDatabase(db).query(MatchHistory.class).withSelection("summonerId = ?", summonerId).orderBy("matchCreation DESC").list();

        database.close();
        return res;

    }

    public static Match getMatch(Context context, String matchId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        Match res = cupboard().withDatabase(db).query(Match.class).withSelection("matchId = ?", matchId).limit(1).get();

        database.close();
        return res;
    }

    public static CurrentGame getCurrentGame(Context context, String matchId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        CurrentGame res = cupboard().withDatabase(db).query(CurrentGame.class).withSelection("matchId = ?", matchId).limit(1).get();

        database.close();
        return res;
    }

    public static List<MatchParticipant> getMatchParticipants(Context context, String matchId) {

        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<MatchParticipant> res = cupboard().withDatabase(db).query(MatchParticipant.class).withSelection("matchId = ?", matchId).orderBy("teamId, participantId").list();

        database.close();
        return res;

    }

    public static List<ChampionIconStatic> getChampionIconList(Context context) {

        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<ChampionIconStatic> res = cupboard().withDatabase(db).query(ChampionIconStatic.class).list();

        database.close();
        return res;
    }

    public static List<ItemsStatic> getItemsIconList(Context context) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<ItemsStatic> res = cupboard().withDatabase(db).query(ItemsStatic.class).list();

        database.close();
        return res;
    }

    public static List<SummonerSpellStatic> getSummonerSpellsIconList(Context context) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<SummonerSpellStatic> res = cupboard().withDatabase(db).query(SummonerSpellStatic.class).list();

        database.close();
        return res;
    }

    public static RecentGames getRecentMatches(Context context, String summonerId) {
        return null;
    }

    public static List<CurrentGameParticipant> getCurrentGameParticipants(Context context, String matchId) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<CurrentGameParticipant> res = cupboard().withDatabase(db).query(CurrentGameParticipant.class).withSelection("matchId = ?", matchId).orderBy("teamId").list();

        database.close();
        return res;
    }

    public static boolean putSearchHistory(Context context, SearchHistory summoner) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean res = cupboard().withDatabase(db).updateInsert(summoner);

        database.close();
        return res;
    }

    public static List<SearchHistory> getSearchHistory(Context context) {
        DB database = new DB(context);
        SQLiteDatabase db = database.getWritableDatabase();

        List<SearchHistory> res = cupboard().withDatabase(db).query(SearchHistory.class).orderBy("searchDate ASC").limit(10).list();

        database.close();
        return res;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();

//        cupboard().withDatabase(db).clearTable(Match.class);
//        cupboard().withDatabase(db).dropAllTables();
//        cupboard().withDatabase(db).createTables();
    }
}
