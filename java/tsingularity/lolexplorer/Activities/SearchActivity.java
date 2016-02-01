package tsingularity.lolexplorer.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Material.Snackbar;
import tsingularity.lolexplorer.API.API;
import tsingularity.lolexplorer.API.StaticData;
import tsingularity.lolexplorer.DB;
import tsingularity.lolexplorer.Model.Constants.Servers;
import tsingularity.lolexplorer.Model.Constants.Servers.Server;
import tsingularity.lolexplorer.Model.SearchHistory;
import tsingularity.lolexplorer.Model.Summoner;
import tsingularity.lolexplorer.R;
import tsingularity.lolexplorer.REST_Service;
import tsingularity.lolexplorer.REST_ServiceManager;
import tsingularity.lolexplorer.Settings;
import tsingularity.lolexplorer.Util.S;

public class SearchActivity extends Activity {

    REST_ServiceManager mRESTServiceManager;
    StaticData          mStaticData;
    AQuery              aq;

    ArrayList<Map<String, Object>> mSearchHistory;

    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_summoner);

        initObjects();
        showSearchHistory();
    }

    @Override
    protected void onResume() {
        registerRecievers();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRESTServiceManager.stopRequests(this);
        unregisterReceivers();
        super.onPause();
    }

    private void initObjects() {
        aq = new AQuery(this);

        registerRecievers();

        mRESTServiceManager = REST_ServiceManager.getInstance(this);
        mRESTServiceManager.setCallbackContext(this);

        mStaticData = StaticData.getInstance(this);

        updateStaticData();

        bindInterfaceEvents();
    }

    private void updateStaticData() {
        if (mStaticData.mBaseCDN.isEmpty()) mRESTServiceManager.getStaticData();
        else mRESTServiceManager.updateStaticData();
    }

    private void bindInterfaceEvents() {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Servers.getServersList());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aq.id(R.id.searchServer).adapter(spinnerArrayAdapter);
        aq.id(R.id.searchServer).setSelection(spinnerArrayAdapter.getPosition(Settings.DEFAULT_SERVER.displayName));

        Button mSearchButton = (Button) findViewById(R.id.searchButton);

        EditText summonerNameEdit = (EditText) findViewById(R.id.searchSummoner);
        summonerNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(v);
                    return true;
                }
                return false;
            }
        });

        mSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch(v);
            }
        });
    }

    private void doSearch(View v) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String summonerName = aq.id(R.id.searchSummoner).getText().toString();
        String host = Server.getHostByName((String) aq.id(R.id.searchServer).getSelectedItem());

        if (summonerName.isEmpty()) return;

        mRESTServiceManager.setHost(host);
        mRESTServiceManager.getSummonerDataByName(summonerName);
    }

    private void registerRecievers() {
        if (mReceiver != null) return;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(API.REQUEST_SUMMONER_DATA_BY_NAME);
        intentFilter.addAction(API.REQUEST_UPDATE_STATIC_DATA);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_CDN);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_CHAMPIONS);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_ITEMS);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_PROFILE_ICONS);
        intentFilter.addAction(API.REQUEST_STATIC_DATA_SUMMONERS);
        intentFilter.addAction(Settings.STOP_PROGRESSBAR);
        intentFilter.addAction(Settings.START_PROGRESSBAR);

        mReceiver = new API_ResultReciever();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void unregisterReceivers() {
        if (mReceiver != null) LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    private void startSummonerActivity(String summonerId) {
        Intent intentActivity = new Intent();
        intentActivity.setClass(SearchActivity.this, SummonerActivity.class);
        intentActivity.putExtra("summonerId", summonerId);
        intentActivity.putExtra("summonerSearched", true);
        startActivity(intentActivity);
    }

    public void loadStaticData() {
        mRESTServiceManager.getStaticDataProfileIcons(mStaticData.mBaseCDN, mStaticData.mProfileIconVer, mStaticData.mBaseLanguage);
        mRESTServiceManager.getStaticDataChampions(mStaticData.mChampionVer);
        mRESTServiceManager.getStaticDataItems(mStaticData.mItemsVer);
        mRESTServiceManager.getStaticDataSummonerSpells(mStaticData.mSummonerSpellsVer);
    }

    private void showSearchHistory() {
        List<SearchHistory> searchHistory = DB.getSearchHistory(this);

        aq.id(R.id.searchHistory).adapter(new SearchAdapter(this, searchHistory));
        aq.id(R.id.searchHistory).itemClicked(this, "historyClicked");

    }

    public void historyClicked(AdapterView<?> parent, View v, int pos, long id) {

        SearchHistory item = (SearchHistory) parent.getItemAtPosition(pos);
        if (item == null) return;

        mRESTServiceManager.setHost(item.server.getHost());
        startSummonerActivity(Long.toString(item.summonerId));
    }

    private void addToSearchHistory(String summonerId) {
        Summoner summoner = DB.getSummoner(this, summonerId);
        if (summoner == null) return;

        DB.putSearchHistory(this, SearchHistory.fromSummoner(summoner, System.currentTimeMillis()));
    }

    public class API_ResultReciever extends BroadcastReceiver {
        public static final String TAGR = "SearchActivity. StatusReceiver: ";

        @Override
        public void onReceive(Context context, Intent intent) {

            if (SearchActivity.this.isFinishing()) return;

            String intentAction = intent.getAction();

            S.L(TAGR + intentAction);

            if (intentAction.equals(Settings.START_PROGRESSBAR)) {
//                aq.id(R.id.progressBar).visible();
                return;
            }
            if (intentAction.equals(Settings.STOP_PROGRESSBAR)) {
//                aq.id(R.id.progressBar).invisible();
                return;
            }

            Bundle extras = intent.getExtras();

            int resultStatus = extras.getInt(REST_Service.SERVICE_RESULT_STATUS);
            if (resultStatus == REST_Service.STATUS_ERROR) {

                String resultString = extras.getString(REST_Service.SERVICE_RESULT_STRING);
                Snackbar.showError(SearchActivity.this, resultString, null);

                return;
            }

            String resultExtra = extras.getString(REST_Service.SERVICE_RESULT_EXTRA);

            switch (intentAction) {
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
//                    updateProfileIcon();
                    break;
                case API.REQUEST_STATIC_DATA_SUMMONERS:
                    mStaticData.initSummonerSpellsIconsList();
                    break;
                case API.REQUEST_SUMMONER_DATA_BY_NAME:

                    String summonerId = resultExtra;
                    if (summonerId == null || summonerId.isEmpty()) return;

                    addToSearchHistory(summonerId);
                    startSummonerActivity(summonerId);
                    break;
            }
        }
    }

    class SearchAdapter extends BaseAdapter {
        public LayoutInflater      mInflater;
        public List<SearchHistory> mSearchHistory;
        public AQuery              aq;

        public SearchAdapter(Context context, List<SearchHistory> searchHistory) {
            this.mSearchHistory = searchHistory;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aq = new AQuery(context);
        }

        @Override
        public int getCount() {
            return mSearchHistory.size();
        }

        @Override
        public Object getItem(int position) {
            return mSearchHistory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View v = convertView;

            if (v == null) {
                v = mInflater.inflate(R.layout.search_history_item, parent, false);

                holder = new ViewHolder();
                holder.summonerName = (TextView) v.findViewById(R.id.search_item_name);
                holder.description = (TextView) v.findViewById(R.id.search_item_descr);
                holder.icon = (ImageView) v.findViewById(R.id.search_item_image);


                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            SearchHistory currentSummoner = mSearchHistory.get(position);

            aq.id(holder.summonerName).text(currentSummoner.name);
            aq.id(holder.description).text(currentSummoner.server.toString());
            aq.id(holder.icon).image(mStaticData.getProfileIconURL(currentSummoner.profileIconId));


            return v;
        }

        class ViewHolder {
            public TextView  summonerName;
            public TextView  description;
            public ImageView icon;
        }
    }

}
