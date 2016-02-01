package tsingularity.lolexplorer.Model.DTO.StaticData.Mastery;

import java.util.Map;

public class MasteryList {

    public Map<String, Mastery> data;
    public MasteryTree          tree;
    public String               type;
    public String               version;

    public Map<String, Mastery> getData() {
        return data;
    }

    public MasteryTree getTree() {
        return tree;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }
}
