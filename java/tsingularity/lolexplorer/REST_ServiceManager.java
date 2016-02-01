package tsingularity.lolexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import tsingularity.lolexplorer.API.API;
import tsingularity.lolexplorer.Activities.CurrentGameActivity;
import tsingularity.lolexplorer.Activities.MatchActivity;
import tsingularity.lolexplorer.Activities.SearchActivity;
import tsingularity.lolexplorer.Activities.SummonerActivity;
import tsingularity.lolexplorer.Model.CurrentGame;
import tsingularity.lolexplorer.Model.CurrentGameParticipant;
import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Model.Match;
import tsingularity.lolexplorer.Model.MatchHistory;
import tsingularity.lolexplorer.Model.StatsRanked;
import tsingularity.lolexplorer.Model.Summoner;
import tsingularity.lolexplorer.Util.S;
import tsingularity.lolexplorer.Util.Util;

public class REST_ServiceManager {

    public static final String TAG = "ServiceHelper ";
    public static volatile REST_ServiceManager instance;
    public                 Context             mContext;
    public                 String              mHost;
    public                 List<REST_Message>  mMessagesQueue;
    API_ResultReciever mReciever;

    public REST_ServiceManager(Context context) {
        this.mContext = context;
        this.mHost = Settings.DEFAULT_SERVER.getHost();

        mMessagesQueue = new ArrayList<REST_Message>();
    }

    public static REST_ServiceManager getInstance(Context context) {

        REST_ServiceManager localInstance = instance;
        if (instance == null) {
            synchronized (REST_ServiceManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new REST_ServiceManager(context);
                }
            }
        }
        return localInstance;
    }

    public void stopRequests(Context context) {

        Intent intent = new Intent(context, REST_Service.class);
        context.stopService(intent);

        int activityID = getActivityID(context);

        for (int i = 0; i < mMessagesQueue.size(); i++) {
            REST_Message curMessage = mMessagesQueue.get(i);
            if (curMessage.activityID == activityID) {
                mMessagesQueue.remove(i);
            }
        }
    }

    public void setCallbackContext(Context context) {

        if (this.mContext != context) {
            this.mContext = context;
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(mReciever);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(API.REQUEST_SUMMONER_DATA_BY_NAME);
        intentFilter.addAction(API.REQUEST_SUMMONER_DATA_BY_ID);
        intentFilter.addAction(API.REQUEST_IS_CURRENT_GAME_ACTIVE);
        intentFilter.addAction(API.REQUEST_LEAGUE);
        intentFilter.addAction(API.REQUEST_MOST_PLAYED_RANKED);
        intentFilter.addAction(API.REQUEST_MATCH_HISTORY_RANKED);
        intentFilter.addAction(API.REQUEST_MATCH_HISTORY_ALL);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_CDN);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_CHAMPIONS);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_ITEMS);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_PROFILE_ICONS);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_SUMMONERS);
        intentFilter.addAction(API.REQUEST_UPDATE_STATIC_DATA);
        intentFilter.addAction(API.REQUEST_MATCH);
        intentFilter.addAction(API.REQUEST_CURRENT_GAME);

        mReciever = new API_ResultReciever();
        LocalBroadcastManager.getInstance(context).registerReceiver(mReciever, intentFilter);
    }

    public long runService(String requestID, String extraArg1, String extraArg2, String extraArg3) {

        Intent intent = new Intent(mContext, REST_Service.class);
        intent.putExtra(REST_Service.API_SERVER_HOST, mHost);
        intent.putExtra(REST_Service.API_FUNCTION_NAME, requestID);
        intent.putExtra(REST_Service.API_FUNCTION_ARGUMENT1, extraArg1);
        intent.putExtra(REST_Service.API_FUNCTION_ARGUMENT2, extraArg2);
        intent.putExtra(REST_Service.API_FUNCTION_ARGUMENT3, extraArg3);

        int activityID = getActivityID(mContext);

        intent.putExtra(REST_Service.SERVICE_CONTEXT, activityID);

        mMessagesQueue.add(new REST_Message(requestID, activityID));

        Intent resultIntent = new Intent(Settings.START_PROGRESSBAR);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(resultIntent);

        mContext.startService(intent);

        return 0;
    }

    int getActivityID(Context context) {
        if (context instanceof SummonerActivity) {
            return Settings.ACTIVITY_SUMMONER;
        } else if (context instanceof MatchActivity) {
            return Settings.ACTIVITY_MATCH;
        } else if (context instanceof CurrentGameActivity) {
            return Settings.ACTIVITY_CURRENTGAME;
        } else if (context instanceof SearchActivity) {
            return Settings.ACTIVITY_SEARCH;
        }

        return Settings.ACTIVITY_UNKNOWN;
    }

    public long runService(String APIfunction, String extraArg) {
        return runService(APIfunction, extraArg, "", "");
    }

    public long runService(String APIfunction) {
        return runService(APIfunction, "");
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public void getSummonerDataById(String summonerId) {

        Summoner summoner = DB.getSummoner(mContext, summonerId);
        if (summoner != null) {
            try {
                SummonerCallbackInterface mCallback = (SummonerCallbackInterface) mContext;
                mCallback.getSummonerDataCallback(summoner);
            } catch (ClassCastException e) {
                S.L("getSummonerDataById. ERROR: callbackInterface not implemented. summonerId: " + summonerId);

            }
        }

//        NotificationManager.addMessage(mContext, TAG + "getSummonerDataById: " + summonerId, API.REQUEST_SUMMONER_DATA_BY_ID);
        runService(API.REQUEST_SUMMONER_DATA_BY_ID, summonerId);
    }

    public void getSummonerDataByName(String summonerName) {

        Summoner summoner = DB.getSummonerByName(mContext, summonerName);
        if (summoner != null) {
            try {
                SummonerCallbackInterface mCallback = (SummonerCallbackInterface) mContext;
                mCallback.getSummonerDataCallback(summoner);
            } catch (ClassCastException e) {
                S.L("getSummonerDataByName. ERROR: callbackInterface not implemented. summonerName: " + summonerName);

            }
        }

//        NotificationManager.addMessage(mContext, TAG + "getSummonerDataByName: " + summonerName, API.REQUEST_SUMMONER_DATA_BY_NAME);
        runService(API.REQUEST_SUMMONER_DATA_BY_NAME, Util.normalize(summonerName));
    }

    public void getCurrentGame(String summonerId, String currentGameId) {

        if (currentGameId != null) {

            CurrentGame currentGame = DB.getCurrentGame(mContext, currentGameId);
            if (currentGame != null) {
                try {
                    List<CurrentGameParticipant> currentGameParticipants = DB.getCurrentGameParticipants(mContext, Long.toString(currentGame.matchId));
                    CurrentGameActivity mCallback = (CurrentGameActivity) mContext;
                    mCallback.getCurrentGameCallback(currentGame, currentGameParticipants);

                    if (currentGameParticipants != null && currentGameParticipants.size() > 0) {
                        for (int i = 0; i < currentGameParticipants.size(); i++) {
                            CurrentGameParticipant currentGameParticipant = currentGameParticipants.get(i);

                            String summonerIdStr = Long.toString(currentGameParticipant.summonerId);
                            List<League> leagues = DB.getLeagues(mContext, currentGameParticipant.summonerId);
                            List<StatsRanked> statsRanked = DB.getRankedStatsByChampionAndTotal(mContext, summonerIdStr, currentGameParticipant.championId);

                            if (leagues.size() == 0) runService(API.REQUEST_LEAGUE, summonerIdStr);
                            if (statsRanked.size() == 0) runService(API.REQUEST_MOST_PLAYED_RANKED, summonerIdStr);

                            if (leagues.size() > 0 || statsRanked.size() > 0) mCallback.updateLeagueAndStatsCallback(currentGameParticipant, leagues, statsRanked);

                        }
                        return;
                    }

                } catch (ClassCastException e) {
                    S.L("getCurrentGame. ERROR: callbackInterface not implemented. SummonerId: " + summonerId);

                }
            }
        }

//        NotificationManager.addMessage(mContext, TAG + "checkIsCurrentGameActive: " + summonerId + " " + currentGameId, API.REQUEST_CURRENT_GAME);
        runService(API.REQUEST_CURRENT_GAME, summonerId);
    }

    public void checkIsCurrentGameActive(String mSummonerId) {
        S.L(TAG + "checkIsCurrentGameActive: " + mSummonerId);

        runService(API.REQUEST_IS_CURRENT_GAME_ACTIVE, mSummonerId);
    }

    //************************************
    //USER FUNCTIONS
    //************************************

    public void getMatchHistory_All(String summonerId) {

        List<MatchHistory> matchHistory = DB.getMatchHistory(mContext, summonerId);
        if (matchHistory != null && !matchHistory.isEmpty()) {
            try {
                SummonerCallbackInterface mCallback = (SummonerCallbackInterface) mContext;
                mCallback.getRecentMatchesCallback(matchHistory);

                if (Settings.DEBUG) return;

            } catch (ClassCastException e) {
                S.L("getMatchHistory_AllCallback. ERROR: callbackInterface not implemented. SummonerID: " + summonerId);

            }
        }

//        NotificationManager.addMessage(mContext, TAG + "getMatchHistoryAll: " + summonerId, API.REQUEST_MATCH_HISTORY_ALL);
        runService(API.REQUEST_MATCH_HISTORY_ALL, summonerId);
    }

    //************************************
    // API
    //************************************

    public void getLeagues(String summonerId) {

        List<League> leagues = DB.getLeagues(mContext, summonerId);
        if (leagues != null && leagues.size() > 0) {
            try {
                SummonerDetailCallbackInterface mCallback = (SummonerDetailCallbackInterface) mContext;
                mCallback.getLeaguesCallback(summonerId, leagues);
            } catch (ClassCastException e) {
                S.L("getLeagues. ERROR: callbackInterface not implemented. summonerId: " + summonerId);

            }
        }

//        NotificationManager.addMessage(mContext, TAG + "getLeagues: " + summonerId, API.REQUEST_LEAGUE);
        runService(API.REQUEST_LEAGUE, summonerId);
    }

    public void getMostPlayed(String summonerId) {

        List<StatsRanked> statsRanked = DB.getRankedStats(mContext, summonerId);
        if (statsRanked != null && statsRanked.size() > 0) {
            try {
                SummonerCallbackInterface mCallback = (SummonerCallbackInterface) mContext;
                mCallback.getMostPlayedCallback(summonerId, statsRanked);
            } catch (ClassCastException e) {
                S.L("getMostPlayed. ERROR: callbackInterface not implemented. summonerId: " + summonerId);

            }
        }

//        NotificationManager.addMessage(mContext, TAG + "getMostPlayed: " + summonerId, API.REQUEST_MOST_PLAYED_RANKED);
        runService(API.REQUEST_MOST_PLAYED_RANKED, summonerId);
    }

    public void getStatsByChampionAndTotal(String summonerId, int championId) {
//        NotificationManager.addMessage(mContext, TAG + "getMostPlayed: " + summonerId, API.REQUEST_MOST_PLAYED_RANKED);

        List<StatsRanked> statsRanked = DB.getRankedStatsByChampionAndTotal(mContext, summonerId, championId);
        if (statsRanked != null && statsRanked.size() > 0) {
            try {
                CurrentGameActivity mCallback = (CurrentGameActivity) mContext;
                mCallback.getStatsByChampionAndTotalCallback(summonerId, championId, statsRanked);
            } catch (ClassCastException e) {
                S.L("getMostPlayed. ERROR: getStatsByChampionAndTotalCallback not implemented. summonerId: " + summonerId);

            }
        }

        runService(API.REQUEST_MOST_PLAYED_RANKED, summonerId);
    }

    public void getStaticData() {
//        NotificationManager.addMessage(mContext, TAG + "getStaticData: ", API.REQUEST_STATIC_DATA_CDN);

        runService(API.REQUEST_STATIC_DATA_CDN);
    }

    public void updateStaticData() {
        S.L(TAG + "updateStaticData");

        runService(API.REQUEST_UPDATE_STATIC_DATA);
    }

    public void getStaticDataProfileIcons(String baseCDN, String mVer, String mBaseLanguage) {
//        NotificationManager.addMessage(mContext, TAG + "getStaticDataProfileIcons: ", API.REQUEST_STATIC_DATA_PROFILE_ICONS);

        runService(API.REQUEST_STATIC_DATA_PROFILE_ICONS, baseCDN, mVer, mBaseLanguage);
    }

    public void getStaticDataChampions(String mVer) {
//        NotificationManager.addMessage(mContext, TAG + "getStaticDataChampions: ", API.REQUEST_STATIC_DATA_CHAMPIONS);

        runService(API.REQUEST_STATIC_DATA_CHAMPIONS, mVer);
    }

    public void getStaticDataItems(String mVer) {
//        NotificationManager.addMessage(mContext, TAG + "getStaticDataItems: ", API.REQUEST_STATIC_DATA_ITEMS);

        runService(API.REQUEST_STATIC_DATA_ITEMS, mVer);
    }

    public void getStaticDataSummonerSpells(String mVer) {
//        NotificationManager.addMessage(mContext, TAG + "getStaticDataSummonerSpells: ", API.REQUEST_STATIC_DATA_SUMMONERS);

        runService(API.REQUEST_STATIC_DATA_SUMMONERS, mVer);
    }

    public void getMatch(String matchId) {

        Match mMatch = DB.getMatch(mContext, matchId);
        if (mMatch != null) {
            try {
                MatchCallbackInterface mCallback = (MatchCallbackInterface) mContext;
                mCallback.getMatchSummaryCallback(mMatch);
                return;

            } catch (ClassCastException e) {
                S.L("getMatch. ERROR: callbackInterface not implemented. MatchID: " + matchId);

            }

        }

//        NotificationManager.addMessage(mContext, TAG + "getMatch: " + matchId, API.REQUEST_MATCH);
        runService(API.REQUEST_MATCH, matchId);
    }

    public interface MatchCallbackInterface {
        void getMatchSummaryCallback(Match mMatch);
    }

    public interface SummonerCallbackInterface {
        void getSummonerDataCallback(Summoner summoner);

        void getRecentMatchesCallback(List<MatchHistory> recentMatches);

        void getMostPlayedCallback(String summonerId, List<StatsRanked> statsRanked);
    }

    public interface SummonerDetailCallbackInterface {

        void getLeaguesCallback(String summonerId, List<League> leagues);
    }

    class REST_Message {
        public String requestID;
        public int    activityID;

        public REST_Message(String inRequest, int inActivityID) {
            requestID = inRequest;
            activityID = inActivityID;
        }
    }

    private class API_ResultReciever extends BroadcastReceiver {
        public static final String TAGR = "ServiceHelper. StatusReceiver ";

        @Override
        public void onReceive(Context context, Intent intent) {

            String inRequestID = intent.getAction();

            S.L(TAGR + inRequestID);
            int activityID = intent.getIntExtra(REST_Service.SERVICE_CONTEXT, Settings.ACTIVITY_UNKNOWN);

            for (int i = 0; i < mMessagesQueue.size(); i++) {
                REST_Message curMessage = mMessagesQueue.get(i);
                if (curMessage.requestID.equals(inRequestID) && curMessage.activityID == activityID) {
                    mMessagesQueue.remove(i);
                    break;
                }
            }

            if (mMessagesQueue.isEmpty()) {
                Intent resultIntent = new Intent(Settings.STOP_PROGRESSBAR);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(resultIntent);
            }
        }
    }

}
