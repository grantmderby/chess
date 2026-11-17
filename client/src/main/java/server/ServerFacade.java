package server;

import com.google.gson.Gson;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import exception.ResponseException;
import model.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.ListResult;

public class ServerFacade
{
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url)
    {
        serverUrl = url;
    }

    public AuthData registerUser(String username, String password, String email) throws ResponseException
    {
        var userData = new UserData(username, password, email);
        var request = buildRequest("POST","/user",userData,null);
        var response = sendRequest(request);
        return handleResponse(response,AuthData.class);   //may through an error as username and authToken are returned
    }

    public AuthData loginUser(String username, String password) throws ResponseException
    {
        var userData = new UserData(username,password,null);
        var request = buildRequest("POST","/session",userData,null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logoutUser(String authToken) throws ResponseException
    {
        var request = buildRequest("DELETE","/session",null,authToken);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public ListResult listGames(String authToken) throws ResponseException
    {
        var request = buildRequest("GET","/game",null,authToken);
        var response = sendRequest(request);
        return handleResponse(response,ListResult.class);
    }

    public String createGame(String authToken, String gameName) throws ResponseException
    {
        var createGameRequest = new CreateGameRequest(gameName);
        var request = buildRequest("POST","/game",createGameRequest,authToken);
        var response = sendRequest(request);
        return handleResponse(response,String.class);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException
    {
        var joinGameRequest = new JoinGameRequest(playerColor,gameID);
        var request = buildRequest("PUT","/game",joinGameRequest,authToken);
        var response = sendRequest(request);
        handleResponse(response,JoinGameRequest.class);
    }

    public void clearDatabase() throws ResponseException
    {
        var request = buildRequest("DELETE","/db",null,null);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    //observeGame

    private HttpRequest buildRequest(String method, String path, Object body, String authToken)
    {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null)
        {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null)
        {
            request.setHeader("authorization",authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request)
    {
        if (request != null)
        {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else
        {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException
    {
        try
        {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex)
        {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException
    {
        var status = response.statusCode();
        if (!isSuccessful(status))
        {
            var body = response.body();
            if (body != null)
            {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null)
        {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status)
    {
        return status / 100 == 2;
    }
}
