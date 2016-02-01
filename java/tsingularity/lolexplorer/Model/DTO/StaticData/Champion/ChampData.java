package tsingularity.lolexplorer.Model.DTO.StaticData.Champion;

public enum ChampData {
    ALL("all"),
    ALLYTIPS("allytips"),
    ALTIMAGES("altimages"),
    BLURB("blurb"),
    ENEMYTIPS("enemytips"),
    IMAGE("image"),
    INFO("info"),
    LORE("lore"),
    PARTYPE("partype"),
    PASSIVE("passive"),
    RECOMMENDED("recommended"),
    SKINS("skins"),
    SPELLS("spells"),
    STATS("stats"),
    TAGS("tags");

    public final String value;

    ChampData(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
