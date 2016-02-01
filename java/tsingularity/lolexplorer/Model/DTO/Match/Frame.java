package tsingularity.lolexplorer.Model.DTO.Match;

import java.util.List;
import java.util.Map;

public class Frame {
    public List<Event>                   events;
    public Map<String, ParticipantFrame> participantFrames;
    public long                          timestamp;
}
