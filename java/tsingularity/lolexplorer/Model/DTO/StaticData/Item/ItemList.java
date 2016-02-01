package tsingularity.lolexplorer.Model.DTO.StaticData.Item;

import java.util.List;
import java.util.Map;

import tsingularity.lolexplorer.Model.DTO.StaticData.BasicData;
import tsingularity.lolexplorer.Model.DTO.StaticData.Group;

public class ItemList {

    public BasicData         basic;
    public Map<String, Item> data;
    public List<Group>       groups;
    public List<ItemTree>    tree;
    public String            type;
    public String            version;

}
