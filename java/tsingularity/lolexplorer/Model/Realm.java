package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.DTO.StaticData.Realm.RealmDTO;

public class Realm {
    public static final String CHAMPION_DATA    = "champion";
    public static final String PROFILEICON_DATA = "profileicon";
    public static final String ITEM_DATA        = "item";
    public static final String SUMMONER_DATA    = "summoner";

    @Index
    public int    id;
    public String cdn;
    public String language;
    public String championVer;
    public String profileiconVer;
    public String itemsVer;
    public String summonerSpellsVer;
    public String version;

    public static Realm fromDTO(RealmDTO realmDTO) {

        Realm realm = new Realm();

        realm.id = 0;
        realm.cdn = realmDTO.cdn;
        realm.language = realmDTO.l;
        realm.version = realmDTO.v;
        realm.championVer = realmDTO.n.get(CHAMPION_DATA);
        realm.profileiconVer = realmDTO.n.get(PROFILEICON_DATA);
        realm.itemsVer = realmDTO.n.get(ITEM_DATA);
        realm.summonerSpellsVer = realmDTO.n.get(SUMMONER_DATA);

        return realm;
    }
}
