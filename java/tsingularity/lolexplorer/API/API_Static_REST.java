package tsingularity.lolexplorer.API;

import retrofit.RestAdapter;

public class API_Static_REST {

    public API_Static_Retrofit mService;
    public String              mServer;

    public API_Static_REST(String server) {
        this.mServer = server;

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(server)
//                .setLogLevel(LogLevel.FULL)
                .build();
        mService = restAdapter.create(API_Static_Retrofit.class);
    }


}
