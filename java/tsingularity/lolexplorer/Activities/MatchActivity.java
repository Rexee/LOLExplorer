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

import com.androidquery.AQuery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Material.Snackbar;
import tsingularity.lolexplorer.API.API;
import tsingularity.lolexplorer.API.StaticData;
import tsingularity.lolexplorer.DB;
import tsingularity.lolexplorer.Model.Constants.ConstantsCommon;
import tsingularity.lolexplorer.Model.Match;
import tsingularity.lolexplorer.Model.MatchParticipant;
import tsingularity.lolexplorer.Model.Summoner;
import tsingularity.lolexplorer.R;
import tsingularity.lolexplorer.REST_Service;
import tsingularity.lolexplorer.REST_ServiceManager;
import tsingularity.lolexplorer.REST_ServiceManager.MatchCallbackInterface;
import tsingularity.lolexplorer.Settings;
import tsingularity.lolexplorer.Util.S;
import tsingularity.lolexplorer.Util.SB;
import tsingularity.lolexplorer.Util.StatusBar;
import tsingularity.lolexplorer.Util.Util;


public class MatchActivity extends Activity implements MatchCallbackInterface {

    public static final String TAG = "MatchActivity ";

    AQuery                 aq;
    StaticData             mStaticData;
    REST_ServiceManager    mRESTServiceManager;
    String                 mMatchId;
    Match                  mMatch;
    BroadcastReceiver      mReceiver;
    String                 mSummonerId;
    List<MatchParticipant> mMatchParticipantList;
    Map<String, View>      mParticipantsViews;
    String                 sKilo;

    View mHeaderSummaryView;
    View mHeaderUpView;
    View mHeaderDownView;
    View mHeaderUpView_Gradient;
    View mHeaderDownView_Gradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_game);

        loadInputParams();
        initObjects();

        mRESTServiceManager.getMatch(mMatchId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerRecievers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRESTServiceManager.stopRequests(this);
        unregisterReceivers();
    }

    private void loadInputParams() {
        Bundle args = getIntent().getExtras();
        long matchId = args.getLong("matchId", 0);
        mSummonerId = args.getString("summonerId", "");
        if (matchId == 0) finish();

        mMatchId = Long.toString(matchId);
    }

    private void registerRecievers() {
        if (mReceiver != null) return;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(API.REQUEST_MATCH);
        intentFilter.addAction(API.REQUEST_SUMMONER_DATA_BY_ID);
        intentFilter.addAction(Settings.STOP_PROGRESSBAR);
        intentFilter.addAction(Settings.START_PROGRESSBAR);

        mReceiver = new API_ResultReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    public void initObjects() {

        aq = new AQuery(this);

        registerRecievers();

        mRESTServiceManager = REST_ServiceManager.getInstance(this);
        mRESTServiceManager.setCallbackContext(this);

        mStaticData = StaticData.getInstance(this);

        mHeaderSummaryView = findViewById(R.id.curGameSummaryView);
        mHeaderUpView = findViewById(R.id.curGameUpHeader);
        mHeaderDownView = findViewById(R.id.curGameBottomHeader);
        mHeaderUpView_Gradient = findViewById(R.id.curGameUpHeaderGradient);
        mHeaderDownView_Gradient = findViewById(R.id.curGameBottomHeaderGradient);

        sKilo = getString(R.string.thousand);

        StatusBar.setStatusBarBackgroundColor(this, Settings.COLOR_TOP_HEADER);
    }

    @Override
    public void getMatchSummaryCallback(Match match) {
        mMatch = match;
        mMatchParticipantList = DB.getMatchParticipants(this, mMatchId);

        showMatchSummary();
        showMatchParticipants();
    }

    private void updateParticipantNames(String summonersIdsCommaSeparated) {
        if (mParticipantsViews == null) return;

        List<String> summonersIdList = Arrays.asList(summonersIdsCommaSeparated.split(ConstantsCommon.SEPARATOR));
        for (String summonerId : summonersIdList) {
            Summoner summoner = DB.getSummoner(MatchActivity.this, summonerId);
            if (summoner == null) continue;

            View participantView = mParticipantsViews.get(summonerId);
            if (participantView == null) continue;

            AQuery aqv = aq.recycle(participantView);
            aqv.id(R.id.participantName).text(summoner.name);
        }
    }

    private void showMatchParticipants() {

        if (mMatch == null) return;
        if (mMatchParticipantList == null) return;

        mParticipantsViews = new HashMap<>(mMatchParticipantList.size());

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout participantsBot = (LinearLayout) findViewById(R.id.curGameParticipantsBottom);
        LinearLayout participantsUp = (LinearLayout) findViewById(R.id.curGameParticipantsUp);

        if (mMatch.bottom_team_winner) {
            aq.recycle(mHeaderDownView).id(R.id.curGameBottomTeamAddInfo).text("Victory");
            aq.recycle(mHeaderDownView).id(R.id.curGameBottomTeamAddInfo).textColor(Settings.COLOR_WINNER_TEXT);
            mHeaderDownView_Gradient.setBackground(new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{Settings.COLOR_BOTTOM_TEAM_HEADER, Settings.COLOR_WINNER, Settings.COLOR_WINNER}));

            aq.recycle(mHeaderUpView).id(R.id.curGameUpTeamAddInfo).text("Defeat");
            aq.recycle(mHeaderUpView).id(R.id.curGameUpTeamAddInfo).textColor(Settings.COLOR_LOOSER_TEXT);
            mHeaderUpView_Gradient.setBackground(new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{Settings.COLOR_UP_TEAM_HEADER, Settings.COLOR_LOOSER, Settings.COLOR_LOOSER}));

        } else {
            aq.recycle(mHeaderDownView).id(R.id.curGameBottomTeamAddInfo).text("Defeat");
            aq.recycle(mHeaderDownView).id(R.id.curGameBottomTeamAddInfo).textColor(Settings.COLOR_LOOSER_TEXT);
            mHeaderDownView_Gradient.setBackground(new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{Settings.COLOR_BOTTOM_TEAM_HEADER, Settings.COLOR_LOOSER, Settings.COLOR_LOOSER}));

            aq.recycle(mHeaderUpView).id(R.id.curGameUpTeamAddInfo).text("Victory");
            aq.recycle(mHeaderUpView).id(R.id.curGameUpTeamAddInfo).textColor(Settings.COLOR_WINNER_TEXT);
            mHeaderUpView_Gradient.setBackground(new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{Settings.COLOR_UP_TEAM_HEADER, Settings.COLOR_WINNER, Settings.COLOR_WINNER}));
        }

        int botK = 0;
        int topK = 0;

        for (MatchParticipant participant : mMatchParticipantList) {
            if (Long.toString(participant.summonerId).equals(mSummonerId)) {
                if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) botK++;
                else topK++;
                addView(participant, layoutInflater, participantsBot, participantsUp, botK, topK, true);
                break;
            }
        }

        for (MatchParticipant participant : mMatchParticipantList) {
            if (Long.toString(participant.summonerId).equals(mSummonerId)) continue;
            if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) botK++;
            else topK++;
            addView(participant, layoutInflater, participantsBot, participantsUp, botK, topK, false);
        }
    }

    private void addView(MatchParticipant participant, LayoutInflater layoutInflater, LinearLayout participantsBot, LinearLayout participantsUp, int botK, int topK, boolean isCurrentSummoner) {

        class ViewTag {
            String position;
            String summonerId;
        }

        LinearLayout parentView;
        float teamKills;
        String positionIdentifier;
        int curK;

        if (participant.teamId == ConstantsCommon.TEAM_BOTTOM) {
            parentView = participantsBot;
            teamKills = mMatch.bottom_team_kills;
            positionIdentifier = "bottom";
            curK = botK;

            if (mMatch.bottom_team_winner) parentView.setBackgroundColor(Settings.COLOR_WINNER);
            else parentView.setBackgroundColor(Settings.COLOR_LOOSER);

        } else {
            parentView = participantsUp;
            teamKills = mMatch.upper_team_kills;
            positionIdentifier = "up";
            curK = topK;

            if (mMatch.bottom_team_winner) parentView.setBackgroundColor(Settings.COLOR_LOOSER);
            else parentView.setBackgroundColor(Settings.COLOR_WINNER);

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
            v = layoutInflater.inflate(R.layout.match_list_item, parentView, false);
            viewCreated = true;
        }

        if (isCurrentSummoner) v.setBackground(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{0x00FFFFFF, 0xA0FFFFFF}));

        AQuery aqv = aq.recycle(v);
        if (participant.summonerId == 0) aqv.id(R.id.participantName).text("Bot");
        else aqv.id(R.id.participantName).text(participant.summonerName);

        aqv.id(R.id.participantChampIcon).image(mStaticData.getChampionIconURL(participant.championId));
        aqv.id(R.id.spell1).image(mStaticData.getSummonerSpellIconURL(participant.spell1Id));
        aqv.id(R.id.spell2).image(mStaticData.getSummonerSpellIconURL(participant.spell2Id));
        aqv.id(R.id.item0).image(mStaticData.getItemIconURL(participant.item0));
        aqv.id(R.id.item1).image(mStaticData.getItemIconURL(participant.item1));
        aqv.id(R.id.item2).image(mStaticData.getItemIconURL(participant.item2));
        aqv.id(R.id.item3).image(mStaticData.getItemIconURL(participant.item3));
        aqv.id(R.id.item4).image(mStaticData.getItemIconURL(participant.item4));
        aqv.id(R.id.item5).image(mStaticData.getItemIconURL(participant.item5));
        aqv.id(R.id.item6).image(mStaticData.getItemIconURL(participant.item6));

        aqv.id(R.id.kda).text(SB.KDA(participant));

        float kp = (float) 100 * ((float) participant.kills + (float) participant.assists) / teamKills;
        aqv.id(R.id.killParticipation).text("Kill participation: " + Math.round(kp) + "%");

        float dmg = (float) participant.totalDamageDealtToChampions / 1000;
        aqv.id(R.id.dmgDealt).text("Dmg. dealt: " + Math.round(dmg) + sKilo);

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
                    intent.setClass(MatchActivity.this, SummonerActivity.class);
                    intent.putExtra("summonerId", vTag.summonerId);
                    startActivity(intent);
                }

            }
        });

        mParticipantsViews.put(Long.toString(participant.summonerId), v);
        if (viewCreated) parentView.addView(v);

    }

    private void showMatchSummary() {

        if (mMatch == null) return;

        aq.recycle(mHeaderSummaryView).id(R.id.curGameGameType).text(mMatch.queueType.getName());
        aq.recycle(mHeaderSummaryView).id(R.id.curGameLength).text("Game Length: " + Util.secondsToTime(mMatch.matchDuration));

        aq.recycle(mHeaderUpView).id(R.id.curGameUpTeamInfo).text(SB.upTeamKDA(mMatch));
        aq.recycle(mHeaderDownView).id(R.id.curGameBottomTeamInfo).text(SB.bottomTeamKDA(mMatch));

        if (mMatch.bottom_team_winner) {
//            aq.id(R.id.participantsBottom).backgroundColor(Settings.COLOR_WINNER);
//            aq.id(R.id.participantsUp).backgroundColor(Settings.COLOR_LOOSER);
        } else {
//            aq.id(R.id.participantsBottom).backgroundColor(Settings.COLOR_LOOSER);
//            aq.id(R.id.participantsUp).backgroundColor(Settings.COLOR_WINNER);
        }

    }

    private class API_ResultReciever extends BroadcastReceiver {
        public static final String TAGR = "MatchActivity. StatusReceiver: ";

        @Override
        public void onReceive(Context context, Intent intent) {

            if (MatchActivity.this.isFinishing()) return;

            String intentAction = intent.getAction();

            S.L(TAGR + intentAction);

            if (intentAction.equals(Settings.START_PROGRESSBAR)) {
                aq.recycle(mHeaderSummaryView).id(R.id.progressBar).visible();
                return;
            }
            if (intentAction.equals(Settings.STOP_PROGRESSBAR)) {
                aq.recycle(mHeaderSummaryView).id(R.id.progressBar).invisible();
                return;
            }

            Bundle extras = intent.getExtras();

            int resultCode = extras.getInt(REST_Service.SERVICE_RESULT_STATUS);
            if (resultCode == REST_Service.STATUS_ERROR) {
                String resultString = extras.getString(REST_Service.SERVICE_RESULT_STRING);
                Snackbar.showError(MatchActivity.this, resultString, null);
                return;
            }

            String resultExtra = extras.getString(REST_Service.SERVICE_RESULT_EXTRA);

//            S.L(TAGR + intentAction);
            switch (intentAction) {
                case API.REQUEST_MATCH:
                    mMatch = DB.getMatch(MatchActivity.this, mMatchId);
                    mMatchParticipantList = DB.getMatchParticipants(MatchActivity.this, mMatchId);
                    showMatchSummary();
                    showMatchParticipants();
                    break;
                case API.REQUEST_SUMMONER_DATA_BY_ID:
                    updateParticipantNames(resultExtra);
                    break;
            }
        }
    }

}
