package tsingularity.lolexplorer.Model.DTO.StaticData.Mastery;

public enum MasteryListData {
    ALL("all"),
    IMAGE("image"),
    PREREQ("prereq"),
    RANKS("ranks"),
    SANITIZED_DESCRIPTION("sanitizedDescription"),
    TREE("tree");

    public final String value;

    MasteryListData(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
