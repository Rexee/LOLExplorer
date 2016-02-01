package tsingularity.lolexplorer.Model.DTO.StaticData.ProfileIcons;

import java.util.Map;

public class ProfileIconsDTO {

//    {
//        "type":"profileicon",
//            "version":"5.4.1",
//            "data":{
//                    "0":{
//                        "id":0,
//                                "image":{
//                                        "full":"0.png",
//                                        "sprite":"profileicon0.png",
//                                        "group":"profileicon",
//                                        "x":0,
//                                        "y":0,
//                                        "w":48,
//                                        "h":48
//                                        }
//                        },
//                    "1":{
//                        "id":1,
//                                "image":{
//                                        "full":"1.png",
//                                        "sprite":"profileicon0.png",
//                                        "group":"profileicon",
//                                        "x":48,
//                                        "y":0,
//                                        "w":48,
//                                        "h":48
//                                        }
//                        }
//                    }
//    }

    public String                        type;
    public String                        version;
    public Map<String, ProfileIconsData> data;

}
