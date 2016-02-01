package tsingularity.lolexplorer.Model.DTO.StaticData.Champion;

import java.util.List;

import tsingularity.lolexplorer.Model.DTO.StaticData.Image;
import tsingularity.lolexplorer.Model.DTO.StaticData.Info;
import tsingularity.lolexplorer.Model.DTO.StaticData.Passive;
import tsingularity.lolexplorer.Model.DTO.StaticData.Recommended;
import tsingularity.lolexplorer.Model.DTO.StaticData.Skin;
import tsingularity.lolexplorer.Model.DTO.StaticData.Stats;

public class Champion {

    public List<String>        allytips;
    public String              blurb;
    public List<String>        enemytips;
    public int                 id;
    public Image               image;
    public Info                info;
    public String              key;
    public String              lore;
    public String              name;
    public String              partype;
    public Passive             passive;
    public List<Recommended>   recommended;
    public List<Skin>          skins;
    public List<ChampionSpell> spells;
    public Stats               stats;
    public List<String>        tags;
    public String              title;


}
