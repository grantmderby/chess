package exception;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception
{



    public ResponseException(int code, String message)
    {
        super(message);
    }

    public ResponseException(String message)
    {
        super(message);
    }

    /*
    public String toJson()
    {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    } Temporary Test*/

    public static ResponseException fromJson(int status,String json)
    {
        var map = new Gson().fromJson(json, HashMap.class);
        String message = map.get("message").toString();
        return new ResponseException(status, message);
    }
}
