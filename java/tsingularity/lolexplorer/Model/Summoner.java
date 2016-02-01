package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.Constants.Servers.Server;
import tsingularity.lolexplorer.Model.DTO.Summoner.SummonerDTO;
import tsingularity.lolexplorer.Util.Util;

public class Summoner {
//    public Long   _id;

    @Index
    public long summonerId;

    public String name;
    public String nameSimple;
    public Server server;
    public int    profileIconId;
    public long   revisionDate;
    public long   summonerLevel;

    public static Summoner fromDTO(SummonerDTO summonerDTO, String host) {
        Summoner summoner = new Summoner();

        summoner.summonerId = summonerDTO.id;
        summoner.name = summonerDTO.name;
        summoner.server = Server.getServerByHost(host);
        summoner.nameSimple = Util.normalize(summonerDTO.name);
        summoner.profileIconId = summonerDTO.profileIconId;
        summoner.revisionDate = summonerDTO.revisionDate;
        summoner.summonerLevel = summonerDTO.summonerLevel;

        return summoner;
    }

}
