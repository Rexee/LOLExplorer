package tsingularity.lolexplorer.Model.DTO.StaticData.Rune;

import java.util.Map;

import tsingularity.lolexplorer.Model.DTO.StaticData.BasicData;

public class RuneList {

    public BasicData         basic;
    public Map<String, Rune> data;
    public String            type;
    public String            version;

    public BasicData getBasic() {
        return basic;
    }

    public Map<String, Rune> getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }
}
