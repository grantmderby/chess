package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import exception.DataAccessException;
import model.*;


public class MySQLAuthDao implements AuthDao
{

    public MySQLAuthDao()
    {
        try
        {
            configureDatabase();
        }catch (DataAccessException ex)
        {
            throw new RuntimeException(String.format("Error: Table failed to be added, cannot continue. Error: %s",ex.getMessage()));
        }

    }

    public void createAuth(AuthData auth) throws DataAccessException
    {
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?,?)";
        createAuthHelper(statement, auth);
    }

   public AuthData getAuth(String authToken) throws DataAccessException
   {
        var statement = "SELECT * FROM AuthData WHERE authToken = ?";
        return getAuthHelper(statement,authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException
    {
        var statement = "DELETE FROM AuthData WHERE authToken = ?";
        deleteAuthHelper(statement, authToken);
    }

    public void clearAuths() throws DataAccessException
    {
        clearAuthHelper();
    }

    private void createAuthHelper(String sqlStatement, AuthData auth) throws DataAccessException
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

    private void deleteAuthHelper(String sqlStatement, String authToken) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection())
        {

            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.setString(1,authToken);
                preparedStatement.executeUpdate();
            }
        } catch(SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private void clearAuthHelper() throws DataAccessException
    {
        String sqlStatement = "TRUNCATE TABLE AuthData";
        try(Connection databaseConnection = DatabaseManager.getConnection();)
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException | DataAccessException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
               authToken VARCHAR(100) PRIMARY KEY,
               username VARCHAR(100) NOT NULL
             );
            """
    };

    private void configureDatabase() throws DataAccessException
    {
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
