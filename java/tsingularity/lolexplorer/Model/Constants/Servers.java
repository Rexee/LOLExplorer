package tsingularity.lolexplorer.Model.Constants;


import java.util.ArrayList;
import java.util.List;

import tsingularity.lolexplorer.Settings;

public class Servers {

    public static List<String> getServersList() {
//        ArrayList<Server> res = new ArrayList<Server>(Arrays.asList(Server.values()));

        ArrayList<String> res = new ArrayList<>(Server.values().length);

        for (Server server : Server.values()) {
            if (server == Server.GLOBAL) continue;
            res.add(server.displayName);
        }

        return res;
    }

    public enum Server {
        BR("BR", "br", "BR1"),
        EUNE("EUNE", "eune", "EUN1"),
        EUW("EUW", "euw", "EUW1"),
        KR("KR", "kr", "KR"),
        LAN("LAN", "lan", "LA1"),
        LAS("LAS", "las", "LA2"),
        NA("NA", "na", "NA1"),
        OCE("OCE", "oce", "OC1"),
        TR("TR", "tr", "TR1"),
        RU("RU", "ru", "RU"),
        PBE("PBE", "pbe", "PBE1"),
        GLOBAL("GLOBAL", "global", "global");

        public String displayName;
        public String host;
        public String platformId;

        Server(String region, String host, String platformId) {
            this.displayName = region;
            this.host = host;
            this.platformId = platformId;
        }

        public static String getHostByName(String name) {
            for (Server v : values())
                if (v.displayName.equals(name)) return v.host;

            return Settings.DEFAULT_SERVER.getHost();
        }

        public static Server getServerByHost(String host) {
            for (Server v : values())
                if (v.host.equals(host)) return v;

            return Settings.DEFAULT_SERVER;
        }

        public String getPlatformId() {
            return platformId;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public String getHost() {
            return host;
        }
    }
}
