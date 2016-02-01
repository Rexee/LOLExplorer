package tsingularity.lolexplorer;

import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;
import retrofit.client.Response;
import tsingularity.lolexplorer.API.API;

public class REST_Response {

    public static final int BAD_REQUEST         = 400;
    public static final int UNAUTHORIZED        = 401;
    public static final int FORBIDDEN           = 403;
    public static final int NO_DATA_FOUND       = 404;
    public static final int DATA_TOO_OLD        = 422;
    public static final int RATE_LIMIT_EXCEEDED = 429;
    public static final int INTERNAL_SERVER     = 500;
    public static final int SERVICE_UNAVAILABLE = 503;

    public String description;
    public int    code;

    public static REST_Response getResponseString(RetrofitError error, String requestType) {

        REST_Response rest_response = new REST_Response();

        Kind errorKind = error.getKind();
        switch (errorKind) {
            case HTTP:
                Response response = error.getResponse();
                if (response != null) {
                    rest_response.code = response.getStatus();
                    rest_response.description = getDescription(error, rest_response.code, requestType);
                }
                break;
            case CONVERSION:
                rest_response.description = "Serialization error: " + error.getMessage();
                break;
            case NETWORK:
                rest_response.description = "Nerwork error: " + error.getMessage();
                break;
            case UNEXPECTED:
                rest_response.description = "Unexpected error: " + error.getMessage();
                break;
        }

        return rest_response;
    }

    private static String getDescription(RetrofitError error, int code, String requestType) {

        String res;

        switch (code) {
            case NO_DATA_FOUND:
                switch (requestType) {
                    case API.REQUEST_SUMMONER_DATA_BY_NAME:
                        res = "Summoner not found";
                        break;
                    case API.REQUEST_MATCH_HISTORY_RANKED:
                    case API.REQUEST_MATCH_HISTORY_ALL:
                        res = "Matches not found";
                        break;
                    case API.REQUEST_MATCH:
                        res = "Match not found";
                        break;
                    case API.REQUEST_LEAGUE:
                        res = "League not found";
                        break;
                    case API.REQUEST_MOST_PLAYED_RANKED:
                        res = "Ranked data not found";
                        break;
                    default:
                        res = "Data not found";
                }
                break;
            case RATE_LIMIT_EXCEEDED:
                res = "Requests rate limit exceed";
                break;
            case FORBIDDEN:
                res = "Services unavailable";
                break;
            case DATA_TOO_OLD:
                res = "No actual data found";
                break;
            case BAD_REQUEST:
                res = "Bad request";
                break;
            case UNAUTHORIZED:
                res = "Authorization error";
                break;
            case INTERNAL_SERVER:
                res = "Server error";
                break;
            case SERVICE_UNAVAILABLE:
                res = "Services unavailable";
                break;
            default:
                res = error.getMessage();
        }

        return res;
    }
}
