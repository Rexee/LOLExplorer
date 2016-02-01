package tsingularity.lolexplorer.Model.DTO.StaticData.Champion;

import java.util.List;

import tsingularity.lolexplorer.Model.DTO.StaticData.Image;
import tsingularity.lolexplorer.Model.DTO.StaticData.LevelTip;
import tsingularity.lolexplorer.Model.DTO.StaticData.SpellVars;


public class ChampionSpell {

    public List<Image>     altimages;
    public List<Double>    cooldown;
    public String          cooldownBurn;
    public List<Integer>   cost;
    public String          costBurn;
    public String          costType;
    public String          description;
    public List<Object>    effect;
    public List<String>    effectBurn;
    public Image           image;
    public String          key;
    public LevelTip        leveltip;
    public int             maxrank;
    public String          name;
    public Object          range;
    public String          rangeBurn;
    public String          resource;
    public String          sanitizedDescription;
    public String          sanitizedTooltip;
    public String          tooltip;
    public List<SpellVars> vars;

}
