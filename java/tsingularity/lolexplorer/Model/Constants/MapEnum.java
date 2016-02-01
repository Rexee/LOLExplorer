package tsingularity.lolexplorer.Model.Constants;

public enum MapEnum {
    SRO(1, "Summoner's Rift. Summer"),
    SROA(2, "Summoner's Rift. Autumn"),
    PG(3, "Tutorial. The Proving Grounds"),
    TTO(4, "Twisted Treeline Original"),
    CS(8, "The Crystal Scar"),
    TT(10, "Twisted Treeline"),
    SR(11, "Summoner's Rift"),
    ARAM(12, "Howling Abyss");

    public String name;
    public int    value;

    MapEnum(int i, String s) {
        this.name = s;
        this.value = i;
    }

    public static String getMap(int i) {
        for (MapEnum v : values())
            if (v.value == i) return v.name;
        return "Unknown";
    }

}
