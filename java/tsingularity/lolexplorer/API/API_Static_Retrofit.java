package tsingularity.lolexplorer.API;

import retrofit.http.GET;
import retrofit.http.Path;
import tsingularity.lolexplorer.Model.DTO.StaticData.ProfileIcons.ProfileIconsDTO;

public interface API_Static_Retrofit {

    @GET(API_Static.PROFILE_ICON)
    String getProfileIcon(@Path("version") String version, @Path("fullName") String fullName);

    @GET(API_Static.CHAMPION_ICON)
    String getChampionIcon(@Path("version") String version, @Path("fullName") String fullName);

    @GET(API_Static.PROFILE_ICON_JSON)
    ProfileIconsDTO getProfileIconJSON(@Path("version") String version, @Path("locale") String locale);

    @GET(API_Static.CHAMPIONS_JSON)
    String getChampionJSON(@Path("version") String version, @Path("locale") String locale);

}
