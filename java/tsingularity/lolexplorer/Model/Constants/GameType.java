package tsingularity.lolexplorer.Model.Constants;

//StatsSummary
public enum GameType {

    AramUnranked5x5("AramUnranked5x5", 1),
    CoopVsAI("CoopVsAI", 2),
    CoopVsAI3x3("CoopVsAI3x3", 3),
    OdinUnranked("OdinUnranked", 4),
    RankedPremade3x3("RankedPremade3x3", 5),
    RankedPremade5x5("RankedPremade5x5", 6),
    RankedSolo5x5("RankedSolo5x5", 7),
    RankedTeam3x3("RankedTeam3x3", 8),
    RankedTeam5x5("RankedTeam5x5", 9),
    Unranked("Unranked", 10),
    Unranked3x3("Unranked3x3", 11),
    OneForAll5x5("OneForAll5x5", 12),
    FirstBlood1x1("FirstBlood1x1", 13),
    FirstBlood2x2("FirstBlood2x2", 14),
    SummonersRift6x6("SummonersRift6x6", 15),
    CAP5x5("CAP5x5", 16),
    URF("URF", 17),
    URFBots("URFBots", 18),
    NightmareBot("NightmareBot", 19),
    Ascension("Ascension", 20),
    Hexakill("Hexakill", 21),
    KingPoro("KingPoro", 22);

    public String name;
    public int    value;

    GameType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static int getValue(String name) {
        for (GameType v : values())
            if (v.name.equalsIgnoreCase(name)) return v.value;
        return 0;
    }
}
