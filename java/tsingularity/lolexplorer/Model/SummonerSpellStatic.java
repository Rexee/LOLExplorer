package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.DTO.StaticData.SummonerSpell.SummonerSpell;

public class SummonerSpellStatic {
    @Index
    public int    id;
    public String full;

    public static SummonerSpellStatic fromDTO(SummonerSpell summonerSpell) {

        SummonerSpellStatic summonerSpellStatic = new SummonerSpellStatic();
        summonerSpellStatic.id = summonerSpell.id;
        summonerSpellStatic.full = summonerSpell.image.full;

        return summonerSpellStatic;

    }
}
