package tsingularity.lolexplorer.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Material.Snackbar;
import tsingularity.lolexplorer.API.API;
import tsingularity.lolexplorer.API.StaticData;
import tsingularity.lolexplorer.DB;
import tsingularity.lolexplorer.Model.Constants.ConstantsCommon;
import tsingularity.lolexplorer.Model.Constants.MMR;
import tsingularity.lolexplorer.Model.CurrentGame;
import tsingularity.lolexplorer.Model.CurrentGameParticipant;
import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Model.StatsRanked;
import tsingularity.lolexplorer.R;
import tsingularity.lolexplorer.REST_Response;
import tsingularity.lolexplorer.REST_Service;
import tsingularity.lolexplorer.REST_ServiceManager;
import tsingularity.lolexplorer.REST_ServiceManager.SummonerDetailCallbackInterface;
import tsingularity.lolexplorer.Settings;
import tsingularity.lolexplorer.Util.StatusBar;
import tsingularity.lolexplorer.Util.Util;

public class CurrentGameActivity extends Activity implements SummonerDetailCallbackInterface {

    String                       mSummonerId;
    String                       mCurrentGameId;
    REST_ServiceManager          mRESTServiceManager;
    BroadcastReceiver            mReceiver;
    AQuery                       aq;
    StaticData                   mStaticData;
    String                       mMatchId;
    CurrentGame                  mCurrentGame;
    List<CurrentGameParticipant> mCurrentGameParticipants;
    Map<String, View>            mParticipantsViews;
    Timer                        mTimer;
    TimerTask                    mTimerTask;
    View                         mHeaderSummaryView;
    View                         mHeaderUpView;
    View                         mHeaderDownView;
    TextView                     mGameLen;

    int  mUpTeamLP;
    int  mBotTeamLP;
    MMR  mMMRManager;
    long mCurrentSummonerTeam;


    boolean mDynamicUpdate;

    int mTeamSize;
    int mCurrentParticipantNum;


    String sLP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_game);

        initObjects();
        loadInputParams();

        mRESTServiceManager.getCurrentGame(mSummonerId, mCurrentGameId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerRecievers();
        registerTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRESTServiceManager.stopRequests(this);
        unregisterReceivers();
        unregisterTimers();
    }

    public void initObjects() {

        aq = new AQuery(this);

        registerRecievers();

        mRESTServiceManager = REST_ServiceManager.getInstance(this);
        mRESTServiceManager.setCallbackContext(this);

        mStaticData = StaticData.getInstance(this);

        sLP = getString(R.string.lp);

        mUpTeamLP = 0;
        mBotTeamLP = 0;
        mCurrentParticipantNum = 0;
        mTeamSize = 0;

        mDynamicUpdate = true;

        mHeaderSummaryView = findViewById(R.id.curGameSummaryView);
        mHeaderUpView = findViewById(R.id.curGameUpHeader);
        mHeaderDownView = findViewById(R.id.curGameBottomHeader);
        mGameLen = (TextView) mHeaderSummaryView.findViewById(R.id.curGameLength);

        StatusBar.setStatusBarBackgroundColor(this, Settings.COLOR_TOP_HEADER);
    }

    private void loadInputParams() {
        Bundle args = getIntent().getExtras();
        mSummonerId = args.getString("summonerId", "");
        mCurrentGameId = args.getString("currentGameId", "");

        if (mSummonerId.isEmpty() || mCurrentGameId.isEmpty()) finish();
    }

    private void registerRecievers() {
        if (mReceiver != null) return;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(API.REQUEST_CURRENT_GAME_REFRESH);
        intentFilter.addAction(API.REQUEST_LEAGUE);
        intentFilter.addAction(API.REQUEST_MOST_PLAYED_RANKED);
        intentFilter.addAction(Settings.STOP_PROGRESSBAR);
        intentFilter.addAction(Settings.START_PROGRESSBAR);

        mReceiver = new API_ResultReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    private void unregisterTimers() {
        mTimer.cancel();
    }

    private void registerTimers() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {

                if (mCurrentGame == null || mGameLen == null) return;

                CurrentGameActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long gameLen = System.currentTimeMillis() / 1000L - mCurrentGame.gameStart / 1000L;
                        mGameLen.setText("Game Length: " + Util.secondsToTime(gameLen));
                    }
                });

            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    public void getCurrentGameCallback(CurrentGame currentGame, List<CurrentGameParticipant> currentGameParticipants) {
        mCurrentGame = currentGame;
        mCurrentGameParticipants = currentGameParticipants;

        updateCurrentGame();
    }

    public void getStatsByChampionAndTotalCallback(String summonerId, int championId, List<StatsRanked> statsRanked) {
        showStats(summonerId, statsRanked);
    }

    @Override
    public void getLeaguesCallback(String summonerId, List<League> leagues) {
        showLeagues(summonerId, leagues);
    }

    public void updateLeagueAndStatsCallback(CurrentGameParticipant currentGameParticipant, List<League> leagues, List<StatsRanked> statsRanked) {
        if (currentGameParticipant == null) return;

        View participantView = mParticipantsViews.get(Long.toString(currentGameParticipant.summonerId));
        if (participantView == null) return;

        updateLeagues(leagues, participantView, currentGameParticipant.teamId);
        updateChampionStats(statsRanked, participantView);

    }

    private void showStats(String summonerId) {
        int participantChampionId = getParticipantChampionId(summonerId);
        if (participantChampionId == 0) return;

        List<StatsRanked> statsRanked = DB.getRankedStatsByChampionAndTotal(CurrentGameActivity.this, summonerId, participantChampionId);
        showStats(summonerId, statsRanked);
    }

    private void showStats(String summonerId, List<StatsRanked> statsRanked) {
        View participantView = mParticipantsViews.get(summonerId);
        updateChampionStats(statsRanked, participantView);
    }

    int getParticipantChampionId(String summonerId) {
        int res = 0;

        if (mCurrentGameParticipants == null || mCurrentGameParticipants.isEmpty()) return res;

        for (int i = 0; i < mCurrentGameParticipants.size(); i++) {
            CurrentGameParticipant participant = mCurrentGameParticipants.get(i);
            if (!summonerId.equals(Long.toString(participant.summonerId))) continue;

            res = participant.championId;
            break;
        }

        return res;
    }

    public void showLeagues(String summonerId) {
        long participantTeamId = getParticipantTeamId(summonerId);
        if (participantTeamId == 0) return;

        List<League> leagues = DB.getLeagues(CurrentGameActivity.this, summonerId);
        View participantView = mParticipantsViews.get(summonerId);
        updateLeagues(leagues, participantView, participantTeamId);
    }

    private void showLeagues(String summonerId, List<League> leagues) {
        long participantTeamId = getParticipantTeamId(summonerId);
        if (participantTeamId == 0) return;

        View participantView = mParticipantsViews.get(summonerId);
        updateLeagues(leagues, participantView, participantTeamId);
    }

    long getParticipantTeamId(String summonerId) {
        long res = 0;
        if (mCurrentGameParticipants == null || mCurrentGameParticipants.isEmpty()) return res;

        for (int i = 0; i < mCurrentGameParticipants.size(); i++) {
            CurrentGameParticipant participant = mCurrentGameParticipants.get(i);
            if (!summonerId.equals(Long.toString(participant.summonerId))) continue;

            res = participant.teamId;
            break;
        }
        return res;
    }

    private void updateChampionStats(List<StatsRanked> statsRanked, View participantView) {
        if (participantView == null) return;

        AQuery aqv = aq.recycle(participantView);
        if (statsRanked == null || statsRanked.isEmpty()) {
            return;
        }

        for (int i = 0; i < statsRanked.size(); i++) {
            StatsRanked curStat = statsRanked.get(i);
            if (curStat.championId == 0) {
                float winRate = (float) curStat.totalSessionsWon / curStat.totalSessionsPlayed * 100;

                aqv.id(R.id.curGameParticipantRankedWinratio).text(Util.BuildSpannable("Total games: ", curStat.totalSessionsPlayed, " (Win: ", Math.round(winRate), "%)"));
            } else {
                float winRate = (float) curStat.totalSessionsWon / curStat.totalSessionsPlayed * 100;
                aqv.id(R.id.curGameParticipantChampWinratio).text(Util.BuildSpannable("Games: ", curStat.totalSessionsPlayed, " (Win: ", Math.round(winRate), "%)"));
                aqv.id(R.id.curGameParticipantChamKDA).text("KDA: " + String.format("%.1f", curStat.avgKDA));
            }

        }

    }

    private void updateLeagues(List<League> leagues, View participantView, long teamId) {

        if (participantView == null) return;

        if (mMMRManager == null) mMMRManager = new MMR();

        League league = MMR.getLeague(leagues);
        int mmr = mMMRManager.getMMR(league);

        AQuery aqv = aq.recycle(participantView);
        aqv.id(R.id.curGameParticipantRank).text(MMR.getLeagueText(league, sLP));

        if (teamId == mCurrentSummonerTeam) {
            mBotTeamLP = mBotTeamLP + mmr;
        } else {
            mUpTeamLP = mUpTeamLP + mmr;
        }

        //dynamic update only on first pass and on last participant
        mCurrentParticipantNum++;
        if (mCurrentParticipantNum == mTeamSize * 2) {
            mDynamicUpdate = false;
            mCurrentParticipantNum = 0;
            updateTeamPower();
            return;
        }

        if (mDynamicUpdate) updateTeamPower();
    }

    private void updateTeamPower() {
        if (mTeamSize == 0) return;
        if (mMMRManager == null) mMMRManager = new MMR();

        int upMMR = mUpTeamLP / mTeamSize;
        int botMMR = mBotTeamLP / mTeamSize;
        aq.recycle(mHeaderUpView).id(R.id.curGameUpTeamInfo).text("Average league: " + mMMRManager.getLeagueFromMMR(upMMR) + " (MMR: " + upMMR + ")");
        aq.recycle(mHeaderDownView).id(R.id.curGameBottomTeamInfo).text("Average league: " + mMMRManager.getLeagueFromMMR(botMMR) + " (MMR: " + botMMR + ")");
    }

    private void showCurrentGame() {
        mCurrentGame = DB.getCurrentGame(CurrentGameActivity.this, mMatchId);
        mCurrentGameParticipants = DB.getCurrentGameParticipants(CurrentGameActivity.this, mMatchId);

        updateCurrentGame();
    }

    private void updateCurrentGame() {

        if (mCurrentGame == null) return;
        if (mCurrentGameParticipants == null) return;

        mTeamSize = mCurrentGameParticipants.size() / 2;
        mUpTeamLP = 0;
        mBotTeamLP = 0;

        aq.recycle(mHeaderSummaryView).id(R.id.curGameGameType).text(mCurrentGame.queueType.getName());

        mParticipantsViews = new HashMap<>(mCurrentGameParticipants.size());

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout participantsBot = (LinearLayout) findViewById(R.id.curGameParticipantsBottom);
        LinearLayout participantsUp = (LinearLayout) findViewById(R.id.curGameParticipantsUp);

        int botK = 0;
        int topK = 0;

        for (CurrentGameParticipant participant : mCurrentGameParticipants) {
            if (Long.toString(participant.summonerId).equals(mSummonerId)) {
                mCurrentSummonerTeam = participant.teamId;
                if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) botK++;
                else topK++;
                addView(participant, layoutInflater, participantsBot, participantsUp, botK, topK, true);
                break;
            }
        }

        for (CurrentGameParticipant participant : mCurrentGameParticipants) {
            if (Long.toString(participant.summonerId).equals(mSummonerId)) continue;
            if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) botK++;
            else topK++;

            addView(participant, layoutInflater, participantsBot, participantsUp, botK, topK, false);
        }
    }

    private void addView(CurrentGameParticipant participant, LayoutInflater layoutInflater, LinearLayout participantsBot, LinearLayout participantsUp, int botK, int topK, boolean isCurrentSummoner) {

        class ViewTag {
            String position;
            String summonerId;
        }

        LinearLayout parentView;
        String positionIdentifier;
        int curK;

        if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) {
            parentView = participantsBot;
            positionIdentifier = "bottom";
            curK = botK;
        } else {
            parentView = participantsUp;
            positionIdentifier = "up";
            curK = topK;
        }

        View v = null;
        for (int i = 0; i < parentView.getChildCount(); i++) {
            final View child = parentView.getChildAt(i);
            final ViewTag vTag = (ViewTag) child.getTag();
            if (vTag != null && vTag.position.equals(positionIdentifier + curK)) {
                v = child;
                break;
            }
        }

        boolean viewCreated = false;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.current_game_list_item, parentView, false);
            viewCreated = true;
        }

        if (isCurrentSummoner) v.setBackground(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{0x00000000, 0x80000000}));

        AQuery aqv = aq.recycle(v);

        aqv.id(R.id.curGameParticipantName).text(participant.summonerName);
        aqv.id(R.id.curGameParticipantName).tag(participant.summonerId);
        aqv.id(R.id.curGameParticipantChampIcon).image(mStaticData.getChampionIconURL(participant.championId));
        aqv.id(R.id.curGameSpell1).image(mStaticData.getSummonerSpellIconURL(participant.spell1Id));
        aqv.id(R.id.curGameSpell2).image(mStaticData.getSummonerSpellIconURL(participant.spell2Id));

        final ViewTag vTag = new ViewTag();
        vTag.position = positionIdentifier + curK;
        vTag.summonerId = Long.toString(participant.summonerId);

        v.setTag(vTag);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewTag vTag = (ViewTag) v.getTag();
                if (vTag != null) {
                    if (vTag.summonerId == null || vTag.summonerId.isEmpty() || vTag.summonerId.equals("0")) return;

                    Intent intent = new Intent();
                    intent.setClass(CurrentGameActivity.this, SummonerActivity.class);
                    intent.putExtra("summonerId", vTag.summonerId);
                    startActivity(intent);
                }
            }
        });

        mParticipantsViews.put(Long.toString(participant.summonerId), v);

        if (viewCreated) parentView.addView(v);

    }

    private class API_ResultReciever extends BroadcastReceiver {
        public static final String TAG = "CurrentGameActivity. StatusReceiver ";

        @Override
        public void onReceive(Context context, Intent intent) {
//            S.L(TAG + "onReceive");

            if (CurrentGameActivity.this.isFinishing()) return;

            String intentAction = intent.getAction();
            if (intentAction.equals(Settings.START_PROGRESSBAR)) {
                aq.recycle(mHeaderSummaryView).id(R.id.progressBar).visible();
                return;
            }
            if (intentAction.equals(Settings.STOP_PROGRESSBAR)) {
                aq.recycle(mHeaderSummaryView).id(R.id.progressBar).invisible();
                return;
            }

            Bundle extras = intent.getExtras();
            int resultStatus = extras.getInt(REST_Service.SERVICE_RESULT_STATUS);
            String resultExtra = extras.getString(REST_Service.SERVICE_RESULT_EXTRA);

            if (resultStatus == REST_Service.STATUS_ERROR) {
                int resultCode = extras.getInt(REST_Service.SERVICE_RESULT_CODE);

                //It's not errors:
                if (resultCode == REST_Response.NO_DATA_FOUND) {
                    switch (intentAction) {
                        case API.REQUEST_LEAGUE:
                            showLeagues(resultExtra);
                            return;
                        case API.REQUEST_MOST_PLAYED_RANKED:
                            showStats(resultExtra);
                            return;
                    }
                }

                String resultString = extras.getString(REST_Service.SERVICE_RESULT_STRING);
                Snackbar.showError(CurrentGameActivity.this, resultString, null);

                return;
            }

            if (resultExtra == null || resultExtra.isEmpty()) return;

//            S.L(TAG + intentAction);
            switch (intentAction) {
                case API.REQUEST_CURRENT_GAME_REFRESH:
                    mMatchId = resultExtra;
                    showCurrentGame();
                    break;
                case API.REQUEST_LEAGUE:
                    showLeagues(resultExtra);
                    break;
                case API.REQUEST_MOST_PLAYED_RANKED:
                    showStats(resultExtra);
                    break;
            }
        }
    }

}
