package tsingularity.lolexplorer.API;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import tsingularity.lolexplorer.DB;
import tsingularity.lolexplorer.Model.ChampionIconStatic;
import tsingularity.lolexplorer.Model.ItemsStatic;
import tsingularity.lolexplorer.Model.ProfileIcons;
import tsingularity.lolexplorer.Model.Realm;
import tsingularity.lolexplorer.Model.SummonerSpellStatic;

public class StaticData {

    public static final String IMG_PROFILEICON = "img/profileicon";
    public static final String IMG_CHAMPION    = "img/champion";
    public static final String IMG_ITEM        = "img/item";
    public static final String IMG_SUMMONER    = "img/spell";
    public static volatile StaticData               instance;
    public                 String                   mBaseCDN;
    public                 String                   mBaseVersion;
    public                 String                   mBaseLanguage;
    public                 String                   mChampionsIconsBaseURL;
    public                 String                   mProfileIconsBaseURL;
    public                 String                   mProfileIconVer;
    public                 String                   mChampionVer;
    public                 String                   mItemsVer;
    public                 String                   mSummonerSpellsVer;
    public                 String                   mItemsBaseURL;
    public                 String                   mSummonerSpellsBaseURL;
    public                 HashMap<Integer, String> mProfileIcons;
    public                 HashMap<Integer, String> mChampionsIcons;
    public                 HashMap<Integer, String> mItemsIcons;

    // img
//    http://ddragon.leagueoflegends.com/cdn/5.2.1/img/profileicon/588.png
//    http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/Aatrox.png
//    http://ddragon.leagueoflegends.com/cdn/5.2.1/img/spell/SummonerFlash.png
//    http://ddragon.leagueoflegends.com/cdn/5.2.1/img/item/1001.png
// data
//    http://ddragon.leagueoflegends.com/cdn/5.2.1/data/en_US/profileicon.json
//    http://ddragon.leagueoflegends.com/cdn/5.2.1/data/en_US/champion.json
    public HashMap<Integer, String> mSpellsIcons;
    public Context                  mContext;


    public StaticData(Context context) {
        this.mContext = context.getApplicationContext();

        initStaticData();
        initProfileIconsList();
        initChampionIconsList();
        initItemsIconsList();
        initSummonerSpellsIconsList();
    }

    public static StaticData getInstance(Context context) {

        StaticData localInstance = instance;
        if (instance == null) {
            synchronized (StaticData.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StaticData(context);
                }
            }
        }
        return localInstance;
    }

    public void initChampionIconsList() {
        List<ChampionIconStatic> championIconStaticList = DB.getChampionIconList(mContext);
        int sizeList = championIconStaticList.size();

        mChampionsIcons = new HashMap<>(sizeList);
        for (int i = 0; i < sizeList; i++) {
            ChampionIconStatic championIconStatic = championIconStaticList.get(i);
            mChampionsIcons.put(championIconStatic.id, championIconStatic.full);
        }
    }

    public void initProfileIconsList() {
        List<ProfileIcons> profileIconsList = DB.getProfileIconList(mContext);
        int sizeList = profileIconsList.size();

        mProfileIcons = new HashMap<>(sizeList);
        for (int i = 0; i < sizeList; i++) {
            ProfileIcons profileIcons = profileIconsList.get(i);
            mProfileIcons.put(profileIcons.id, profileIcons.full);
        }
    }

    public void initItemsIconsList() {
        List<ItemsStatic> itemsIconsList = DB.getItemsIconList(mContext);
        int sizeList = itemsIconsList.size();

        mItemsIcons = new HashMap<>(sizeList);
        for (int i = 0; i < sizeList; i++) {
            ItemsStatic itemIcon = itemsIconsList.get(i);
            mItemsIcons.put(itemIcon.id, itemIcon.full);
        }
    }

    public void initSummonerSpellsIconsList() {
        List<SummonerSpellStatic> summonerSpellIconsList = DB.getSummonerSpellsIconList(mContext);
        int sizeList = summonerSpellIconsList.size();

        mSpellsIcons = new HashMap<>(sizeList);
        for (int i = 0; i < sizeList; i++) {
            SummonerSpellStatic summonerSpell = summonerSpellIconsList.get(i);
            mSpellsIcons.put(summonerSpell.id, summonerSpell.full);
        }
    }

    public void initStaticData() {

        Realm realm = DB.getRealm(mContext);
        if (realm == null) {
            mBaseCDN = "";
            return;
        }
        //TODO: error handling

        mBaseCDN = realm.cdn;
        mBaseVersion = realm.version;
        mBaseLanguage = realm.language;
        mProfileIconVer = realm.profileiconVer;
        mChampionVer = realm.championVer;
        mItemsVer = realm.itemsVer;
        mSummonerSpellsVer = realm.summonerSpellsVer;

        mProfileIconsBaseURL = mBaseCDN + "/" + realm.profileiconVer + "/" + IMG_PROFILEICON + "/";
        mChampionsIconsBaseURL = mBaseCDN + "/" + realm.championVer + "/" + IMG_CHAMPION + "/";
        mItemsBaseURL = mBaseCDN + "/" + realm.itemsVer + "/" + IMG_ITEM + "/";
        mSummonerSpellsBaseURL = mBaseCDN + "/" + realm.summonerSpellsVer + "/" + IMG_SUMMONER + "/";

    }

    public String getProfileIconURL(int profileIconId) {
        if (mBaseCDN == null || mBaseCDN.isEmpty()) return "";

        String curIcon;
        if (profileIconId == 30) curIcon = mProfileIcons.get(0);
        else curIcon = mProfileIcons.get(profileIconId);

        if (curIcon.isEmpty()) return "";

        return mProfileIconsBaseURL + curIcon;
    }

    public String getChampionIconURL(int championId) {
        if (mBaseCDN == null || mBaseCDN.isEmpty()) return "";

        String curIcon = mChampionsIcons.get(championId);
        if (curIcon == null) return "";

        return mChampionsIconsBaseURL + curIcon;
    }

    public String getItemIconURL(int itemId) {
        if (mBaseCDN == null || mBaseCDN.isEmpty()) return "";

        String curIcon = mItemsIcons.get(itemId);
        if (curIcon == null) return "";

        return mItemsBaseURL + curIcon;
    }

    public String getSummonerSpellIconURL(int spellId) {
        if (mBaseCDN == null || mBaseCDN.isEmpty()) return "";

        String curIcon = mSpellsIcons.get(spellId);
        if (curIcon == null) return "";

        return mSummonerSpellsBaseURL + curIcon;
    }
}
