package tsingularity.lolexplorer.Model.DTO.Summoner;

import java.util.List;
import java.util.Set;

public class RunePages {

    public Set<RunePage> pages;
    public long          summonerId;

    public class RunePage {
        public Boolean        current;
        public Long           id;
        public String         name;
        public List<RuneSlot> slots;
    }

    public class RuneSlot {
        public int runeId;
        public int runeSlotId;
    }


}
