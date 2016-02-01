package tsingularity.lolexplorer.Model;

import cupboard.annotation.Index;
import tsingularity.lolexplorer.Model.DTO.StaticData.ProfileIcons.ProfileIconsData;


public class ProfileIcons {

    @Index
    public int    id;
    public String full;

    public static ProfileIcons fromDTO(ProfileIconsData profileIconsData) {

        ProfileIcons profileIcon = new ProfileIcons();
        profileIcon.id = profileIconsData.id;
        profileIcon.full = profileIconsData.image.full;

        return profileIcon;

    }
}
