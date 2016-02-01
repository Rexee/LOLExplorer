package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.DTO.StaticData.Item.Item;

public class ItemsStatic {
    @Index
    public int    id;
    public String full;

    public static ItemsStatic fromDTO(Item item) {

        ItemsStatic itemsStatic = new ItemsStatic();
        itemsStatic.id = item.id;
        itemsStatic.full = item.image.full;

        return itemsStatic;

    }
}
