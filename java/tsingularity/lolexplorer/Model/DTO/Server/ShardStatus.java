package tsingularity.lolexplorer.Model.DTO.Server;

import java.util.List;

public class ShardStatus {
    public String        hostname;
    public List<String>  locales;
    public String        name;
    public String        region_tag;
    public List<Service> services;
    public String        slug;
}
