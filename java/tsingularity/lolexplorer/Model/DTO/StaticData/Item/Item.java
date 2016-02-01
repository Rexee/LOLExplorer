package tsingularity.lolexplorer.Model.DTO.StaticData.Item;


import java.util.List;
import java.util.Map;

import tsingularity.lolexplorer.Model.DTO.StaticData.BasicDataStats;
import tsingularity.lolexplorer.Model.DTO.StaticData.Gold;
import tsingularity.lolexplorer.Model.DTO.StaticData.Image;
import tsingularity.lolexplorer.Model.DTO.StaticData.MetaData;

public class Item {

    public String               colloq;
    public boolean              consumeOnFull;
    public boolean              consumed;
    public int                  depth;
    public String               description;
    public List<String>         from;
    public Gold                 gold;
    public String               group;
    public boolean              hideFromAll;
    public int                  id;
    public Image                image;
    public boolean              inStore;
    public List<String>         into;
    public Map<String, Boolean> maps;
    public String               name;
    public String               plaintext;
    public String               requiredChampion;
    public MetaData             rune;
    public String               sanitizedDescription;
    public int                  specialRecipe;
    public int                  stacks;
    public BasicDataStats       stats;
    public List<String>         tags;

    public String getColloq() {
        return colloq;
    }

    public boolean isConsumeOnFull() {
        return consumeOnFull;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public int getDepth() {
        return depth;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFrom() {
        return from;
    }

    public Gold getGold() {
        return gold;
    }

    public String getGroup() {
        return group;
    }

    public boolean isHideFromAll() {
        return hideFromAll;
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public boolean isInStore() {
        return inStore;
    }

    public List<String> getInto() {
        return into;
    }

    public Map<String, Boolean> getMaps() {
        return maps;
    }

    public String getName() {
        return name;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public String getRequiredChampion() {
        return requiredChampion;
    }

    public MetaData getRune() {
        return rune;
    }

    public String getSanitizedDescription() {
        return sanitizedDescription;
    }

    public int getSpecialRecipe() {
        return specialRecipe;
    }

    public int getStacks() {
        return stacks;
    }

    public BasicDataStats getStats() {
        return stats;
    }

    public List<String> getTags() {
        return tags;
    }
}
