package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import exception.DataAccessException;
import model.*;


public class MySQLAuthDao implements AuthDao
{
    private final Map<String, AuthData> authMap;

    public MySQLAuthDao() throws DataAccessException
    {
        this.authMap = new HashMap<>();
        configureDatabase();
    }

    public void createAuth(AuthData auth) throws DataAccessException
    {
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?,?)";
        executeUpdate(statement, auth);
    }

   public AuthData getAuth(String authToken) throws DataAccessException
   {
        var statement = "SELECT * FROM AuthData WHERE authToken = ?";
        return getAuthHelper(statement,authToken);
    }

    public boolean verifyAuth(String authToken) throws DataAccessException
    {
        return !authMap.containsKey(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException
    {
        authMap.remove(authToken);
    }

    public void clearAuths() throws DataAccessException
    {
        authMap.clear();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
               authToken VARCHAR(100) PRIMARY KEY,
               username VARCHAR(100) NOT NULL
             );
            """
    };

    private AuthData getAuthHelper(String sqlStatement, String authToken) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
        {
            preparedStatement.setString(1,authToken);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    String authTokenFromDB = resultSet.getString(1);
                    String usernameFromDB = resultSet.getString(2);

                    return new AuthData(usernameFromDB,authTokenFromDB);
                }
            }
        } catch(SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }

        return null;
    }

    private void executeUpdate(String sqlStatement, AuthData auth) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection())
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.setString(1,auth.authToken());
                preparedStatement.setString(2,auth.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private void configureDatabase() throws DataAccessException
    {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection())
        {
            for (String statement : createStatements)
            {
                try (var preparedStatement = conn.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException sqlError)
        {
            throw new DataAccessException(String.format("Unable to configure database: %s", sqlError.getMessage()));
        }
    }

}
