package tsingularity.lolexplorer.Model.DTO.Server;

import java.util.List;

public class Incident {
    public boolean       active;
    public String        created_at;
    public long          id;
    public List<Message> updates;
}
