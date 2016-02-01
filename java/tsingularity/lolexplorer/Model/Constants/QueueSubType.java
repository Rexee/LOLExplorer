package tsingularity.lolexplorer.Model.Constants;

public enum QueueSubType {

    NONE("Custom"),
    NORMAL("Normal 5x5"),
    BOT("Coop vs AI"),
    RANKED_SOLO_5x5("Ranked Solo"),
    RANKED_PREMADE_3x3("Ranked Premade 3x3"),
    RANKED_PREMADE_5x5("Ranked Premade 5x5"),
    ODIN_UNRANKED("Dominion"),
    RANKED_TEAM_3x3("Ranked Team 3x3"),
    RANKED_TEAM_5x5("Ranked Team 5x5"),
    NORMAL_3x3("Normal 3x3"),
    BOT_3x3("Coop vs AI 3x3"),
    CAP_5x5("Normal Team Builder"),
    ARAM_UNRANKED_5x5("ARAM"),
    ONEFORALL_5x5("One for All"),
    FIRSTBLOOD_1x1("Snowdown Showdown 1v1"),
    FIRSTBLOOD_2x2("Snowdown Showdown 2v2"),
    SR_6x6("Hexakill"),
    URF("Ultra Rapid Fire"),
    URF_BOT("Ultra Rapid Fire Coop vs AI"),
    NIGHTMARE_BOT("Doom Coop vs AI"),
    ASCENSION("Ascension"),
    HEXAKILL("Hexakill"),
    KING_PORO("King Poro"),
    COUNTER_PICK("Nemesis Draft");

    private String name;

    QueueSubType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
