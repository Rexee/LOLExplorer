package tsingularity.lolexplorer.Model.DTO.Summoner;

import java.util.List;
import java.util.Set;

public class MasteryPages {

    public Set<MasteryPage> pages;
    public long             summonerId;

    public class MasteryPage {
        public boolean       current;
        public long          id;
        public List<Mastery> masteries;
        public String        name;
    }

    public class Mastery {
        public int id;
        public int rank;
    }
}
