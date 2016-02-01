package tsingularity.lolexplorer.Model.DTO.StaticData.Mastery;

import java.util.List;

import tsingularity.lolexplorer.Model.DTO.StaticData.Image;

public class Mastery {

    public List<String> description;
    public int          id;
    public Image        image;
    public String       name;
    public String       prereq;
    public int          ranks;
    public List<String> sanitizedDescription;

    public List<String> getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPrereq() {
        return prereq;
    }

    public int getRanks() {
        return ranks;
    }

    public List<String> getSanitizedDescription() {
        return sanitizedDescription;
    }
}
