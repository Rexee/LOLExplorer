package tsingularity.lolexplorer.Model.DTO.Server;

import java.util.List;

public class Message {
    public String            author;
    public String            content;
    public String            created_at;
    public long              id;
    public String            severity;
    public List<Translation> translations;
    public String            updated_at;
}
