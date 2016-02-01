package tsingularity.lolexplorer.Model.DTO.StaticData.Rune;

public enum RuneListData {
    ALL("all"),
    COLLOQ("colloq"),
    CONSUME_ON_FULL("consumeOnFull"),
    CONSUMED("consumed"),
    DEPTH("depth"),
    FROM("from"),
    GOLD("gold"),
    GROUPS("groups"),
    HIDE_FROM_ALL("hideFromAll"),
    IMAGE("image"),
    IN_STORE("inStore"),
    INTO("into"),
    MAPS("maps"),
    REQUIRED_CHAMPION("requiredChampion"),
    SANITIZED_DESCRIPTION("sanitizedChampion"),
    SPECIAL_RECIPE("specialRecipe"),
    STACKS("stacks"),
    STATS("stats"),
    TAGS("tags");

    public final String value;

    RuneListData(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
