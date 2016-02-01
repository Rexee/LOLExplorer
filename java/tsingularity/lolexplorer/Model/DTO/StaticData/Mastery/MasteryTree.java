package tsingularity.lolexplorer.Model.DTO.StaticData.Mastery;

import java.util.List;

public class MasteryTree {

    public List<MasteryTreeList> defense;
    public List<MasteryTreeList> offense;
    public List<MasteryTreeList> utility;

    public List<MasteryTreeList> getDefense() {
        return defense;
    }

    public List<MasteryTreeList> getOffense() {
        return offense;
    }

    public List<MasteryTreeList> getUtility() {
        return utility;
    }
}
