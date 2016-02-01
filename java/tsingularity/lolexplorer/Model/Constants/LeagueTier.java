package tsingularity.lolexplorer.Model.Constants;

public enum LeagueTier {

    CHALLENGER("Challenger", 7),
    MASTER("Master", 6),
    DIAMOND("Diamond", 5),
    PLATINUM("Platinum", 4),
    GOLD("Gold", 3),
    SILVER("Silver", 2),
    BRONZE("Bronze", 1),
    UNRANKED("Unranked", 0),
    WOOD("Wood", -1);

    public int    value;
    public String name;

    LeagueTier(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
//        for(LeagueTier v : values())
//            if(v.name.equalsIgnoreCase(name)) return v.value;
//        return 0;
    }
}
