package tsingularity.lolexplorer.Model.DTO.StaticData.SummonerSpell;

import java.util.List;

import tsingularity.lolexplorer.Model.DTO.StaticData.Image;
import tsingularity.lolexplorer.Model.DTO.StaticData.LevelTip;
import tsingularity.lolexplorer.Model.DTO.StaticData.SpellVars;

public class SummonerSpell {

    public List<Double>       cooldown;
    public String             cooldownBurn;
    public List<Integer>      cost;
    public String             costBurn;
    public String             costType;
    public String             description;
    public List<List<Double>> effect;
    public List<String>       effectBurn;
    public int                id;
    public Image              image;
    public String             key;
    public LevelTip           leveltip;
    public int                maxrank;
    public List<String>       modes;
    public String             name;
    public Object             range;
    public String             rangeBurn;
    public String             resource;
    public String             sanitizedDescription;
    public String             sanitizedTooltip;
    public int                summonerLevel;
    public String             tooltip;
    public List<SpellVars>    vars;

    public List<Double> getCooldown() {
        return cooldown;
    }

    public String getCooldownBurn() {
        return cooldownBurn;
    }

    public List<Integer> getCost() {
        return cost;
    }

    public String getCostBurn() {
        return costBurn;
    }

    public String getCostType() {
        return costType;
    }

    public String getDescription() {
        return description;
    }

    public List<List<Double>> getEffect() {
        return effect;
    }

    public List<String> getEffectBurn() {
        return effectBurn;
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public LevelTip getLeveltip() {
        return leveltip;
    }

    public int getMaxrank() {
        return maxrank;
    }

    public List<String> getModes() {
        return modes;
    }

    public String getName() {
        return name;
    }

    /**
     * @return either a {@link java.util.List} of {@link java.lang.Integer}, or a {@link java.lang.String} containing
     * 'self' for spells that target one's own champion
     */
    public Object getRange() {
        return range;
    }

    public String getRangeBurn() {
        return rangeBurn;
    }

    public String getResource() {
        return resource;
    }

    public String getSanitizedDescription() {
        return sanitizedDescription;
    }

    public String getSanitizedTooltip() {
        return sanitizedTooltip;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public String getTooltip() {
        return tooltip;
    }

    public List<SpellVars> getVars() {
        return vars;
    }
}
