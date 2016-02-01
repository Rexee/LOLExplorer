package tsingularity.lolexplorer;

import tsingularity.lolexplorer.Model.Constants.Servers.Server;

public class Settings {
    public static final String API_KEY        = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; //Enter here your dev. API key
    public static final Server DEFAULT_SERVER = Server.EUNE;

    public static final int COLOR_TOP_HEADER = 0xFF303F9F;

    public static final int COLOR_WINNER = 0xFF81c784;
    public static final int COLOR_LOOSER = 0xFFff8a80;

    public static final int COLOR_WINNER_TEXT = 0xff1b5e20;
    public static final int COLOR_LOOSER_TEXT = 0xffb71c1c;

    public static final int COLOR_UP_TEAM_HEADER     = 0xFF6a1b9a;
    public static final int COLOR_BOTTOM_TEAM_HEADER = 0xFF0d47a1;

    public static final boolean DEBUG = false;

    public static final int ACTIVITY_STOP_ALL    = -1;
    public static final int ACTIVITY_UNKNOWN     = 0;
    public static final int ACTIVITY_SUMMONER    = 1;
    public static final int ACTIVITY_MATCH       = 2;
    public static final int ACTIVITY_CURRENTGAME = 3;
    public static final int ACTIVITY_SEARCH      = 4;

    public static final String STOP_PROGRESSBAR  = "STOP_PROGRESSBAR";
    public static final String START_PROGRESSBAR = "START_PROGRESSBAR";

    public static String SETTINGS_SUMMONER_ID     = "SETTINGS_SUMMONER_ID";
    public static String SETTINGS_SUMMONER_SERVER = "SETTINGS_SUMMONER_SERVER";
}
