package tsingularity.lolexplorer.Model.DTO.StaticData;

import java.util.List;

public class SpellVars {
    public List<Double> coeff;
    public String       dyn;
    public String       key;
    public String       link;
    public String       ranksWith;

    public List<Double> getCoeff() {
        return coeff;
    }

    public String getDyn() {
        return dyn;
    }

    public String getKey() {
        return key;
    }

    public String getLink() {
        return link;
    }

    public String getRanksWith() {
        return ranksWith;
    }
}
