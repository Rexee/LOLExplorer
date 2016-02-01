package tsingularity.lolexplorer.Model.Constants;


//    public static final String RANKED_SOLO = "RANKED_SOLO_5x5";
//    public static final String RANKED_TEAM5 = "RANKED_TEAM_3x3";
//    public static final String RANKED_TEAM3 = "RANKED_TEAM_5x5";

public enum Leagues {
    RANKED_SOLO_5x5("RANKED_SOLO_5x5", 1),
    RANKED_TEAM_3x3("RANKED_TEAM_3x3", 2),
    RANKED_TEAM_5x5("RANKED_TEAM_5x5", 3);

    public String name;
    public int    value;

    Leagues(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static Leagues getLeague(int val) {
        for (Leagues v : values())
            if (v.value == val) return v;
        return null;
    }

    public static int getValue(String name) {
        for (Leagues v : values())
            if (v.name.equalsIgnoreCase(name)) return v.value;
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }


}
