package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.DTO.StaticData.Champion.Champion;

public class ChampionIconStatic {

    @Index
    public int    id;
    public String full;

    public static ChampionIconStatic fromDTO(Champion champion) {

        ChampionIconStatic championIconStatic = new ChampionIconStatic();
        championIconStatic.id = champion.id;
        championIconStatic.full = champion.image.full;

        return championIconStatic;

    }
}
