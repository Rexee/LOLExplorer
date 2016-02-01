package tsingularity.lolexplorer.Model.Constants;

//Match, Current-game
public enum QueueType {

    CUSTOM("Custom", 0),
    NORMAL_5x5_BLIND("Normal 5v5 Blind Pick", 2),
    BOT_5x5("Historical Summoner's Rift Coop vs AI", 7),
    BOT_5x5_INTRO("Summoner's Rift Coop vs AI Intro Bot", 31),
    BOT_5x5_BEGINNER("Summoner's Rift Coop vs AI Beginner Bot", 32),
    BOT_5x5_INTERMEDIATE("Historical Summoner's Rift Coop vs AI Intermediate Bot", 33),
    NORMAL_3x3("Normal 3v3", 8),
    NORMAL_5x5_DRAFT("Normal 5v5 Draft Pick", 14),
    ODIN_5x5_BLIND("Dominion 5v5 Blind Pick", 16),
    ODIN_5x5_DRAFT("Dominion 5v5 Draft Pick", 17),
    BOT_ODIN_5x5("Dominion Coop vs AI", 25),
    RANKED_SOLO_5x5("Ranked Solo 5v5", 4),
    RANKED_PREMADE_3x3("Ranked Premade 3v3", 9),
    RANKED_PREMADE_5x5("Ranked Premade 5v5", 6),
    RANKED_TEAM_3x3("Ranked Team 3v3", 41),
    RANKED_TEAM_5x5("Ranked Team 5v5", 42),
    BOT_TT_3x3("Twisted Treeline Coop vs AI", 52),
    GROUP_FINDER_5x5("Team Builder", 61),
    ARAM_5x5("ARAM", 65),
    ONEFORALL_5x5("One for All", 70),
    ONEFORALL_MIRRORMODE_5x5("One for All: Mirror Mode", 70),
    FIRSTBLOOD_1x1("Snowdown Showdown 1v1", 72),
    FIRSTBLOOD_2x2("Snowdown Showdown 2v2", 73),
    SR_6x6("Hexakill", 75),
    URF_5x5("Ultra Rapid Fire", 76),
    BOT_URF_5x5("Ultra Rapid Fire games played against AI", 83),
    NIGHTMARE_BOT_5x5_RANK1("Doom Bots Rank 1", 91),
    NIGHTMARE_BOT_5x5_RANK2("Doom Bots Rank 2", 92),
    NIGHTMARE_BOT_5x5_RANK5("Doom Bots Rank 5", 93),
    ASCENSION_5x5("Ascension", 96),
    HEXAKILL("Twisted Treeline 6x6 Hexakill", 98),
    KING_PORO_5x5("King Poro", 300),
    COUNTER_PICK("Nemesis Draft", 301);

    private String name;
    private int    gameQueueConfigId;

    QueueType(String name, int gameQueueConfigId) {
        this.name = name;
        this.gameQueueConfigId = gameQueueConfigId;
    }

    public static String getQueueNameByConfigId(int gameQueueConfigId) {
        QueueType[] queueTypes = QueueType.values();
        for (QueueType queueType : queueTypes) {
            if (queueType.getGameQueueConfigId() == gameQueueConfigId) {
                return queueType.getName();
            }
        }
        return "";
    }

    public static QueueType getQueueTypeByConfigId(int gameQueueConfigId) {
        QueueType[] queueTypes = QueueType.values();
        for (QueueType queueType : queueTypes) {
            if (queueType.getGameQueueConfigId() == gameQueueConfigId) {
                return queueType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getGameQueueConfigId() {
        return gameQueueConfigId;
    }

}
