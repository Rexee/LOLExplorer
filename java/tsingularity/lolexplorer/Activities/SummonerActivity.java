package tsingularity.lolexplorer.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

import java.util.ArrayList;
import java.util.List;

import Material.FloatingActionButton;
import Material.Snackbar;
import tsingularity.lolexplorer.API.API;
import tsingularity.lolexplorer.API.StaticData;
import tsingularity.lolexplorer.DB;
import tsingularity.lolexplorer.Model.Constants.LeagueTier;
import tsingularity.lolexplorer.Model.Constants.Leagues;
import tsingularity.lolexplorer.Model.League;
import tsingularity.lolexplorer.Model.MatchHistory;
import tsingularity.lolexplorer.Model.MatchHistoryDisplay;
import tsingularity.lolexplorer.Model.StatsRanked;
import tsingularity.lolexplorer.Model.Summoner;
import tsingularity.lolexplorer.R;
import tsingularity.lolexplorer.REST_Response;
import tsingularity.lolexplorer.REST_Service;
import tsingularity.lolexplorer.REST_ServiceManager;
import tsingularity.lolexplorer.REST_ServiceManager.SummonerCallbackInterface;
import tsingularity.lolexplorer.REST_ServiceManager.SummonerDetailCallbackInterface;
import tsingularity.lolexplorer.Settings;
import tsingularity.lolexplorer.SummonerSummaryAdapter;
import tsingularity.lolexplorer.Util.S;
import tsingularity.lolexplorer.Util.SB;
import tsingularity.lolexplorer.Util.StatusBar;
import tsingularity.lolexplorer.Util.Util;


public class SummonerActivity extends Activity implements SummonerDetailCallbackInterface, SummonerCallbackInterface {

    private static final String TAG            = "SummonerActivity ";
    private static final float  PARALLAX_SPEED = 1.4F;
    private static final float  ALPHA_SPEED    = 1F;

    private static final int MAX_PANEL_MINI_HEIGHT_DP = 80;
    private static final int MAX_PANEL_HEIGHT_DP      = 252;
    private static final int MAX_PANEL_LEN_DP         = MAX_PANEL_HEIGHT_DP - MAX_PANEL_MINI_HEIGHT_DP;

    //icon
    private static final int ICON_START_SIZE_DP      = 128;
    private static final int ICON_END_SIZE_DP        = 48;
    private static final int ICON_X_START_PADDING_DP = 10;
    private static final int ICON_X_END_PADDING_DP   = 80;
    private static final int ICON_Y_START_PADDING_DP = 38;
    private static final int ICON_Y_END_PADDING_DP   = 28;

    private static final int ICON_SIZE_LEN_DP = ICON_START_SIZE_DP - ICON_END_SIZE_DP;
    private static final int ICON_X_LEN_DP    = ICON_X_START_PADDING_DP - ICON_X_END_PADDING_DP;
    private static final int ICON_Y_LEN_DP    = ICON_Y_START_PADDING_DP - ICON_Y_END_PADDING_DP;

    private static final float ICON_SIZE_STEP = (float) MAX_PANEL_LEN_DP / ICON_SIZE_LEN_DP;
    private static final float ICON_X_STEP    = (float) MAX_PANEL_LEN_DP / ICON_X_LEN_DP;
    private static final float ICON_Y_STEP    = (float) MAX_PANEL_LEN_DP / ICON_Y_LEN_DP;

    //border-icon
    private static final int ICON_BG_START_SIZE_DP      = 134;
    private static final int ICON_BG_END_SIZE_DP        = 50;
    private static final int ICON_BG_X_START_PADDING_DP = 7;
    private static final int ICON_BG_X_END_PADDING_DP   = 78;
    private static final int ICON_BG_Y_START_PADDING_DP = 35;
    private static final int ICON_BG_Y_END_PADDING_DP   = 26;

    private static final int ICON_BG_SIZE_LEN_DP = ICON_BG_START_SIZE_DP - ICON_BG_END_SIZE_DP;
    private static final int ICON_BG_X_LEN_DP    = ICON_BG_X_START_PADDING_DP - ICON_BG_X_END_PADDING_DP;
    private static final int ICON_BG_Y_LEN_DP    = ICON_BG_Y_START_PADDING_DP - ICON_BG_Y_END_PADDING_DP;

    private static final float ICON_BG_SIZE_STEP = (float) MAX_PANEL_LEN_DP / ICON_BG_SIZE_LEN_DP;
    private static final float ICON_BG_X_STEP    = (float) MAX_PANEL_LEN_DP / ICON_BG_X_LEN_DP;
    private static final float ICON_BG_Y_STEP    = (float) MAX_PANEL_LEN_DP / ICON_BG_Y_LEN_DP;

    public static float mDensity = 1;

    private static int MAX_PANEL_HEIGHT_PX;

    private static int ICON_START_SIZE_PX;
    private static int ICON_X_START_PADDING_PX;
    private static int ICON_Y_START_PADDING_PX;

    private static int ICON_BG_START_SIZE_PX;
    private static int ICON_BG_X_START_PADDING_PX;
    private static int ICON_BG_Y_START_PADDING_PX;

    private static int ICON_END_SIZE_PX;
    private static int ICON_X_END_PADDING_PX;
    private static int ICON_Y_END_PADDING_PX;

    private static int ICON_BG_END_SIZE_PX;
    private static int ICON_BG_X_END_PADDING_PX;
    private static int ICON_BG_Y_END_PADDING_PX;


    AQuery                    aq;
    REST_ServiceManager       mRESTServiceManager;
    StaticData                mStaticData;
    String                    mSummonerId;
    Summoner                  mSummoner;
    List<MatchHistoryDisplay> mMatchHistory;
    SummonerSummaryAdapter    mSummonerSummaryAdapter;
    BroadcastReceiver         mReceiver;
    ListView                  mListView;

    FrameLayout  mSummonerSummaryViewFrame;
    View         mSummonerSummaryHeaderShadow;
    LinearLayout mMostPlayedListIcons;
    ImageView    mSummonerIcon;
    ImageView    mSummonerIconBorder;
    ImageView    mSummonerIconBackground;
    TextView     mSummonerName;
    TextView     mSummonerRanked;
    TextView     mSummonerServer;
    TextView     mSummonerLastSeen;
    TextView     mMostPlayed;

    FloatingActionButton mFab;

    int mSummaryViewFrameMiniHeight;
    int mSummaryViewFrameHeight;

    boolean mIsViewMinimized     = false;
    boolean mIsCurrentGameActive = false;

    String sKilo;
    String sLP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summoner_summary);

        initObjects();
        bindInterfaceEvents();

        loadInputParams();
        showSummonerInit(mSummonerId);
    }

    @Override
    protected void onResume() {
        registerRecievers();
        checkIfCurrentGameActive();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRESTServiceManager.stopRequests(this);
        unregisterReceivers();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadInputParams() {

        Bundle args = getIntent().getExtras();
        if (args == null) return;

        boolean summonerSearched = args.getBoolean("summonerSearched", false);
        if (summonerSearched) {
            showSummonerInit(args.getString("summonerId", ""));
            return;
        }

        mSummonerId = args.getString("summonerId", "");
        if (!mSummonerId.isEmpty()) {
            getSummonerDataById(mSummonerId);
        }
    }

    private void getSummonerDataById(String summonerId) {
        mRESTServiceManager.getSummonerDataById(summonerId);
    }

    private void unregisterReceivers() {
        if (mReceiver != null) LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    public void initObjects() {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        mDensity = dm.density;

        MAX_PANEL_HEIGHT_PX = Util.dpToPix(MAX_PANEL_HEIGHT_DP, mDensity);

        ICON_START_SIZE_PX = Util.dpToPix(ICON_START_SIZE_DP, mDensity);
        ICON_X_START_PADDING_PX = Util.dpToPix(ICON_X_START_PADDING_DP, mDensity);
        ICON_Y_START_PADDING_PX = Util.dpToPix(ICON_Y_START_PADDING_DP, mDensity);

        ICON_BG_START_SIZE_PX = Util.dpToPix(ICON_BG_START_SIZE_DP, mDensity);
        ICON_BG_X_START_PADDING_PX = Util.dpToPix(ICON_BG_X_START_PADDING_DP, mDensity);
        ICON_BG_Y_START_PADDING_PX = Util.dpToPix(ICON_BG_Y_START_PADDING_DP, mDensity);

        ICON_END_SIZE_PX = Util.dpToPix(ICON_END_SIZE_DP, mDensity);
        ICON_X_END_PADDING_PX = Util.dpToPix(ICON_X_END_PADDING_DP, mDensity);
        ICON_Y_END_PADDING_PX = Util.dpToPix(ICON_Y_END_PADDING_DP, mDensity);

        ICON_BG_END_SIZE_PX = Util.dpToPix(ICON_BG_END_SIZE_DP, mDensity);
        ICON_BG_X_END_PADDING_PX = Util.dpToPix(ICON_BG_X_END_PADDING_DP, mDensity);
        ICON_BG_Y_END_PADDING_PX = Util.dpToPix(ICON_BG_Y_END_PADDING_DP, mDensity);

        aq = new AQuery(this);

        registerRecievers();

        mRESTServiceManager = REST_ServiceManager.getInstance(this);
        mRESTServiceManager.setCallbackContext(this);

        mStaticData = StaticData.getInstance(this);
        mRESTServiceManager.updateStaticData();

        sKilo = getString(R.string.thousand);
        sLP = getString(R.string.lp);

        mSummonerSummaryViewFrame = (FrameLayout) findViewById(R.id.summonerSummaryViewFrame);
        mMostPlayedListIcons = (LinearLayout) findViewById(R.id.mostPlayedListIcons);

        mSummonerIcon = (ImageView) findViewById(R.id.summonerIcon);
        mSummonerIconBackground = (ImageView) findViewById(R.id.summonerIconBackground);
        mSummonerIconBorder = (ImageView) findViewById(R.id.summonerIconPlaceholder);

        mListView = (ListView) findViewById(R.id.matchHistory);
        View mSummonerHeaderLayout = getLayoutInflater().inflate(R.layout.summoner_summary_header, null);
        mListView.addHeaderView(mSummonerHeaderLayout);

        mFab = (FloatingActionButton) findViewById(R.id.fab_Summary);

        mSummonerName = (TextView) findViewById(R.id.summonerName);
        mSummonerRanked = (TextView) findViewById(R.id.summonerRankedSolo5v5);
        mSummonerServer = (TextView) findViewById(R.id.summonerServer);
        mSummonerLastSeen = (TextView) findViewById(R.id.summonerLastTime);
        mMostPlayed = (TextView) findViewById(R.id.summonerMostPlayedText);

        mSummonerSummaryHeaderShadow = findViewById(R.id.summonerDataHeaderShadow);

        mSummaryViewFrameMiniHeight = Util.dpToPix(MAX_PANEL_MINI_HEIGHT_DP, mDensity);

        StatusBar.setAlpha(this, 0);

    }

    public void bindInterfaceEvents() {

        mFab.show();

        mFab.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                S.L(hasFocus);
            }
        });

        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSearchActivity();

//                if (!mFab.mSearchTextVisible) showSearchView();
//                else doSearch();
//            }
//
//            private void doSearch() {
//                View view = getCurrentFocus();
//                if (view == null) return;
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//                String summonerNew = mSearchEditView.getText().toString();
//                if (!summonerNew.isEmpty()) getSummonerDataByName(summonerNew);
//
//                mSearchEditView.animate().setInterpolator(mFab.mInterpolatorAccell).setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mFab.mSearchTextVisible = false;
//                        mSearchEditView.setVisibility(View.INVISIBLE);
//                    }
//                });
//            }
//
//            private void showSearchView() {
//                mFab.mSearchTextVisible = true;
//                mSearchEditView.setVisibility(View.VISIBLE);
//                mSearchEditView.animate().setInterpolator(mFab.mInterpolatorAccell).setDuration(200).alpha(1).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputManager.showSoftInput(mSearchEditView, InputMethodManager.SHOW_IMPLICIT);
//                    }
//                });
            }
        });
    }

    private void StartSearchActivity() {
        Intent intent = new Intent();
        intent.setClass(SummonerActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void registerRecievers() {
        if (mReceiver != null) return;

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
        intentFilter.addAction(Settings.STOP_PROGRESSBAR);
        intentFilter.addAction(Settings.START_PROGRESSBAR);

        mReceiver = new API_ResultReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void showSummonerInit(String summonerId) {
        if (summonerId == null || summonerId.isEmpty()) return;

        mSummonerId = summonerId;

        mSummoner = DB.getSummoner(SummonerActivity.this, mSummonerId);
        mMatchHistory = null;
        mSummonerSummaryAdapter = null;

        if (!mIsCurrentGameActive) checkIfCurrentGameActive();

        showSummoner();
        loadSummonerData();
    }

    private void updateStaticData() {
        if (mStaticData.mBaseCDN.isEmpty()) mRESTServiceManager.getStaticData();
        else mRESTServiceManager.updateStaticData();
    }

    private void showCurrentGame(final String currentGameId) {

        Snackbar.showAction(this, "Game in progress", "Show", mFab, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SummonerActivity.this, CurrentGameActivity.class);
                intent.putExtra("summonerId", mSummonerId);
                intent.putExtra("currentGameId", currentGameId);

                startActivity(intent);
            }
        });
    }

    private void updateProfileIcon() {

        if (mSummoner == null) return;

        String profileIcon = mStaticData.getProfileIconURL(mSummoner.profileIconId);
        if (profileIcon.isEmpty()) {
            updateStaticData();
            return;
        }

        aq.id(R.id.summonerIcon).image(profileIcon, true, true, 0, 0, new BitmapAjaxCallback() {
            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                if (bm == null) return;

                bm = Util.crop(bm, mSummoner.profileIconId);

                aq.id(R.id.summonerIcon).image(bm);
                aq.id(R.id.summonerIconBackground).image(Util.blur(SummonerActivity.this, bm));
            }
        });

    }

    public void loadStaticData() {
        mRESTServiceManager.getStaticDataProfileIcons(mStaticData.mBaseCDN, mStaticData.mProfileIconVer, mStaticData.mBaseLanguage);
        mRESTServiceManager.getStaticDataChampions(mStaticData.mChampionVer);
        mRESTServiceManager.getStaticDataItems(mStaticData.mItemsVer);
        mRESTServiceManager.getStaticDataSummonerSpells(mStaticData.mSummonerSpellsVer);
    }

    public void loadSummonerData() {
        if (mRESTServiceManager == null) return;

        mRESTServiceManager.getMatchHistory_All(mSummonerId);
        mRESTServiceManager.getLeagues(mSummonerId);
        mRESTServiceManager.getMostPlayed(mSummonerId);
    }

    void checkIfCurrentGameActive() {
        if (mRESTServiceManager == null || mSummonerId == null || mSummonerId.isEmpty()) return;
        mRESTServiceManager.checkIsCurrentGameActive(mSummonerId);
    }

    void getSummonerDataByName(String summonerName) {
        mRESTServiceManager.getSummonerDataByName(summonerName);
    }

    public void showMatchHistory() {
        List<MatchHistory> matchHistory = DB.getMatchHistory(SummonerActivity.this, mSummonerId);
        showMatchHistory(matchHistory);
    }

    public void showMatchHistory(List<MatchHistory> matchHistory) {

        if (mMatchHistory == null) {
            mMatchHistory = new ArrayList<>(matchHistory != null ? matchHistory.size() : 0);
        }

        MatchHistoryDisplay.getMatchHistory(mStaticData, mMatchHistory, matchHistory, sKilo);

        if (mSummonerSummaryAdapter != null) {
            mSummonerSummaryAdapter.notifyDataSetChanged();

        } else {

            mSummonerSummaryAdapter = new SummonerSummaryAdapter(this, mMatchHistory);

            aq.id(R.id.matchHistory).adapter(mSummonerSummaryAdapter);
            aq.id(R.id.matchHistory).itemClicked(this, "matchClicked");
            aq.id(R.id.matchHistory).scrolled(new OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (mListView.getChildCount() == 0) return;
                    scrollEffects(mListView.getChildAt(0), firstVisibleItem);
                }

                private void scrollEffects(View ChildAt0, int firstVisibleItem) {

                    if (firstVisibleItem > 0) {
                        showSummonerMini();
                        return;
                    }

                    if (mSummaryViewFrameHeight == 0) mSummaryViewFrameHeight = mSummonerSummaryViewFrame.getHeight();

                    int top = ChildAt0.getTop();
                    int currentSizePanel = mSummaryViewFrameHeight + top;
                    if (currentSizePanel <= mSummaryViewFrameMiniHeight) {
                        showSummonerMini();
                        return;
                    }

                    mIsViewMinimized = false;

                    float alpha = (top >= 0) ? 1 : 1 + ALPHA_SPEED * top / (mSummaryViewFrameHeight - mSummaryViewFrameMiniHeight);
                    if (alpha < 0) alpha = 0;

                    float translationParallaxed = (float) top / PARALLAX_SPEED;

                    mSummonerSummaryViewFrame.setTranslationY(top);
                    mSummonerSummaryHeaderShadow.setTranslationY(top);
                    mSummonerIconBackground.setTranslationY(-translationParallaxed);
                    mSummonerLastSeen.setAlpha(alpha);
                    mMostPlayed.setAlpha(alpha);
                    mSummonerServer.setAlpha(alpha);
                    mSummonerRanked.setAlpha(alpha);
                    mMostPlayedListIcons.setAlpha(alpha);

                    float cur_increment_px = MAX_PANEL_HEIGHT_PX - currentSizePanel;

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSummonerIcon.getLayoutParams();
                    int iconSize_px = (int) (ICON_START_SIZE_PX - cur_increment_px / ICON_SIZE_STEP);
                    int iconX_px = (int) (ICON_X_START_PADDING_PX - cur_increment_px / ICON_X_STEP);
                    int iconY_px = (int) (ICON_Y_START_PADDING_PX - cur_increment_px / ICON_Y_STEP);
                    layoutParams.width = iconSize_px;
                    layoutParams.height = iconSize_px;
                    layoutParams.leftMargin = iconX_px;
                    layoutParams.topMargin = iconY_px;
                    mSummonerIcon.setLayoutParams(layoutParams);

                    layoutParams = (RelativeLayout.LayoutParams) mSummonerIconBorder.getLayoutParams();
                    iconSize_px = (int) (ICON_BG_START_SIZE_PX - cur_increment_px / ICON_BG_SIZE_STEP);
                    iconX_px = (int) (ICON_BG_X_START_PADDING_PX - cur_increment_px / ICON_BG_X_STEP);
                    iconY_px = (int) (ICON_BG_Y_START_PADDING_PX - cur_increment_px / ICON_BG_Y_STEP);
                    layoutParams.width = iconSize_px;
                    layoutParams.height = iconSize_px;
                    layoutParams.leftMargin = iconX_px;
                    layoutParams.topMargin = iconY_px;
                    mSummonerIconBorder.setLayoutParams(layoutParams);

                }

                private void showSummonerMini() {
                    if (mIsViewMinimized) return;
                    mIsViewMinimized = true;

                    mSummonerSummaryViewFrame.setTranslationY(mSummaryViewFrameMiniHeight - mSummaryViewFrameHeight);
                    mSummonerSummaryHeaderShadow.setTranslationY(mSummaryViewFrameMiniHeight - mSummaryViewFrameHeight);
                    mSummonerIconBackground.setTranslationY(-(float) (mSummaryViewFrameMiniHeight - mSummaryViewFrameHeight) / PARALLAX_SPEED);
                    mSummonerLastSeen.setAlpha(0);
                    mSummonerRanked.setAlpha(0);
                    mSummonerServer.setAlpha(0);
                    mMostPlayedListIcons.setAlpha(0);
                    mMostPlayed.setAlpha(0);

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSummonerIcon.getLayoutParams();
                    layoutParams.width = ICON_END_SIZE_PX;
                    layoutParams.height = ICON_END_SIZE_PX;
                    layoutParams.leftMargin = ICON_X_END_PADDING_PX;
                    layoutParams.topMargin = ICON_Y_END_PADDING_PX;
                    mSummonerIcon.setLayoutParams(layoutParams);

                    layoutParams = (RelativeLayout.LayoutParams) mSummonerIconBorder.getLayoutParams();
                    layoutParams.width = ICON_BG_END_SIZE_PX;
                    layoutParams.height = ICON_BG_END_SIZE_PX;
                    layoutParams.leftMargin = ICON_BG_X_END_PADDING_PX;
                    layoutParams.topMargin = ICON_BG_Y_END_PADDING_PX;
                    mSummonerIconBorder.setLayoutParams(layoutParams);

                }
            });

        }
    }

    public void matchClicked(AdapterView<?> parent, View v, int pos, long id) {

        if (id < 0) return;

        MatchHistoryDisplay matchHistoryDisplayItem = (MatchHistoryDisplay) mSummonerSummaryAdapter.getItem((int) id);
        if (matchHistoryDisplayItem == null) return;

        long matchId = matchHistoryDisplayItem.matchId;
        if (matchId == 0) return;

        Intent intent = new Intent();
        intent.setClass(this, MatchActivity.class);
        intent.putExtra("matchId", matchId);
        intent.putExtra("summonerId", mSummonerId);

        startActivity(intent);
    }

    //LEAGUES
    @Override
    public void getLeaguesCallback(String summonerId, List<League> leagues) {
        showLeagues(leagues);
    }

    public void showLeagues(List<League> leagues) {

        String leagueText = LeagueTier.UNRANKED.getName();
        LeagueTier leagueTier = LeagueTier.UNRANKED;

        if (leagues != null) {
            for (League league : leagues) {
                if (league == null) continue;

                switch (Leagues.getLeague(league.queue)) {
                    case RANKED_SOLO_5x5:
                        leagueText = SB.getLeagueRepresentation(league, sLP);
                        leagueTier = league.tier;
                        break;
                }
            }
        }

        aq.id(R.id.summonerRankedSolo5v5).text(leagueText);
        switch (leagueTier) {
            case SILVER:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_silver);
                break;
            case GOLD:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_gold);
                break;
            case PLATINUM:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_plat);
                break;
            case DIAMOND:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_diamond);
                break;
            case MASTER:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_master);
                break;
            case CHALLENGER:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_challenger);
                break;
            default:
                aq.id(R.id.summonerIconPlaceholder).image(R.drawable.placeholder_unranked);
        }
    }

    public void showLeagues() {
        List<League> leagues = DB.getLeagues(SummonerActivity.this, mSummonerId);
        showLeagues(leagues);
    }

    private void showMostPlayed(List<StatsRanked> statsRanked) {
        if (statsRanked.isEmpty()) {
            aq.id(R.id.summonerMostPlayedText).invisible();
            aq.id(R.id.mostPlayed1).invisible();
            aq.id(R.id.mostPlayed2).invisible();
            aq.id(R.id.mostPlayed3).invisible();
            aq.id(R.id.mostPlayed4).invisible();
            aq.id(R.id.mostPlayed5).invisible();
            return;
        }

        aq.id(R.id.summonerMostPlayedText).visible();

        if (statsRanked.size() > 0) {
            aq.id(R.id.mostPlayedChamp1).image(mStaticData.getChampionIconURL(statsRanked.get(0).championId));
            aq.id(R.id.mostPlayedChampTxt1).text(SB.rankedStats(statsRanked.get(0)));
            aq.id(R.id.mostPlayed1).visible();
        }

        if (statsRanked.size() > 1) {
            aq.id(R.id.mostPlayedChamp2).image(mStaticData.getChampionIconURL(statsRanked.get(1).championId));
            aq.id(R.id.mostPlayedChampTxt2).text(SB.rankedStats(statsRanked.get(1)));
            aq.id(R.id.mostPlayed2).visible();
        }

        if (statsRanked.size() > 2) {
            aq.id(R.id.mostPlayedChamp3).image(mStaticData.getChampionIconURL(statsRanked.get(2).championId));
            aq.id(R.id.mostPlayedChampTxt3).text(SB.rankedStats(statsRanked.get(2)));
            aq.id(R.id.mostPlayed3).visible();
        }

        if (statsRanked.size() > 3) {
            aq.id(R.id.mostPlayedChamp4).image(mStaticData.getChampionIconURL(statsRanked.get(3).championId));
            aq.id(R.id.mostPlayedChampTxt4).text(SB.rankedStats(statsRanked.get(3)));
            aq.id(R.id.mostPlayed4).visible();
        }

        if (statsRanked.size() > 4) {
            aq.id(R.id.mostPlayedChamp5).image(mStaticData.getChampionIconURL(statsRanked.get(4).championId));
            aq.id(R.id.mostPlayedChampTxt5).text(SB.rankedStats(statsRanked.get(4)));
            aq.id(R.id.mostPlayed5).visible();
        }
    }

    public void showMostPlayed() {
        List<StatsRanked> statsRanked = DB.getRankedStats(SummonerActivity.this, mSummonerId);
        showMostPlayed(statsRanked);
    }

    //SHOW SUMMONER
    @Override
    public void getSummonerDataCallback(Summoner summoner) {
        updateSummoner(summoner);
    }

    //MATCH HISTORY
    @Override
    public void getRecentMatchesCallback(List<MatchHistory> recentMatches) {
        showMatchHistory(recentMatches);
    }

    //MOST PLAYED
    @Override
    public void getMostPlayedCallback(String summonerId, List<StatsRanked> statsRanked) {
        showMostPlayed(statsRanked);
    }

    private void updateSummoner(Summoner summoner) {
        if (summoner == null) return;

        if (mSummoner == null || !mSummoner.name.equals(summoner.name) || mSummoner.summonerId != summoner.summonerId) {
            mSummoner = summoner;
            mSummonerId = Long.toString(summoner.summonerId);
            showSummoner();
        } else {
            if (mSummoner.profileIconId != summoner.profileIconId) {
                mSummoner.profileIconId = summoner.profileIconId;
                updateProfileIcon();
            }
            if (mSummoner.revisionDate != summoner.revisionDate) {
                mSummoner.revisionDate = summoner.revisionDate;
                updateLastSeen();
            }
        }
    }

    public void showSummoner() {

        if (mSummoner == null) return;

        aq.id(R.id.summonerName).text(mSummoner.name);
        aq.id(R.id.summonerServer).text(getServerRepresentation());
        updateLastSeen();
        updateProfileIcon();

    }

    public String getServerRepresentation() {
        if (mSummoner.summonerLevel != 30) return mSummoner.server.toString() + " (Level: " + mSummoner.summonerLevel + ")";
        else return mSummoner.server.toString();
    }

    private void updateLastSeen() {
        aq.id(R.id.summonerLastTime).text("Last seen: " + Util.getUTimeStr(mSummoner.revisionDate));
    }

    public class API_ResultReciever extends BroadcastReceiver {
        public static final String TAGR = "SummonerActivity. StatusReceiver: ";

        @Override
        public void onReceive(Context context, Intent intent) {

            if (SummonerActivity.this.isFinishing()) return;

            String intentAction = intent.getAction();

            S.L(TAGR + intentAction);

            if (intentAction.equals(Settings.START_PROGRESSBAR)) {
                aq.id(R.id.progressBar).visible();
                return;
            }
            if (intentAction.equals(Settings.STOP_PROGRESSBAR)) {
                aq.id(R.id.progressBar).invisible();
                return;
            }

            Bundle extras = intent.getExtras();

            int resultStatus = extras.getInt(REST_Service.SERVICE_RESULT_STATUS);
            if (resultStatus == REST_Service.STATUS_ERROR) {
                int resultCode = extras.getInt(REST_Service.SERVICE_RESULT_CODE);

                //It's not errors:
                if (resultCode == REST_Response.NO_DATA_FOUND) {
                    switch (intentAction) {
                        case API.REQUEST_IS_CURRENT_GAME_ACTIVE:
                            mIsCurrentGameActive = false;
                            Snackbar.hideAll();
                            return;
                        case API.REQUEST_LEAGUE:
                            showLeagues();
                            return;
                        case API.REQUEST_MOST_PLAYED_RANKED:
                            return;
                    }
                }

                String resultString = extras.getString(REST_Service.SERVICE_RESULT_STRING);
                Snackbar.showError(SummonerActivity.this, resultString, mFab);

                return;
            }

            String resultExtra = extras.getString(REST_Service.SERVICE_RESULT_EXTRA);

            switch (intentAction) {
                case API.REQUEST_SUMMONER_DATA_BY_NAME:
                case API.REQUEST_SUMMONER_DATA_BY_ID:
                    showSummonerInit(resultExtra);
                    break;
                case API.REQUEST_LEAGUE:
                    showLeagues();
                    break;
                case API.REQUEST_MOST_PLAYED_RANKED:
                    showMostPlayed();
                    break;
                case API.REQUEST_MATCH_HISTORY_RANKED:
                case API.REQUEST_MATCH_HISTORY_ALL:
                    showMatchHistory();
                    break;
                case API.REQUEST_UPDATE_STATIC_DATA:
                    if (!resultExtra.equals(REST_Service.DATA_NOT_CHANGED)) {
                        mStaticData.initStaticData();
                    }
                    break;
                case API.REQUEST_STATIC_DATA_CDN:
                    mStaticData.initStaticData();
                    loadStaticData();
                    break;
                case API.REQUEST_STATIC_DATA_CHAMPIONS:
                    mStaticData.initChampionIconsList();
                    break;
                case API.REQUEST_STATIC_DATA_ITEMS:
                    mStaticData.initItemsIconsList();
                    break;
                case API.REQUEST_STATIC_DATA_PROFILE_ICONS:
                    mStaticData.initProfileIconsList();
                    updateProfileIcon();
                    break;
                case API.REQUEST_STATIC_DATA_SUMMONERS:
                    mStaticData.initSummonerSpellsIconsList();
                    break;
                case API.REQUEST_IS_CURRENT_GAME_ACTIVE:
                    mIsCurrentGameActive = true;
                    showCurrentGame(resultExtra);
                    break;
            }

        }

    }
}
