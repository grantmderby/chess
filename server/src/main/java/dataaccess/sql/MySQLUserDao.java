package dataaccess.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataaccess.UserDao;
import exception.DataAccessException;
import model.*;

public class MySQLUserDao implements UserDao
{
    AdditionalFunctions addFunc = new AdditionalFunctions();

    public MySQLUserDao()
    {
        try
        {


            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS UserData (
               username VARCHAR(100) PRIMARY KEY,
               password VARCHAR(100) NOT NULL,
               email VARCHAR(100)
             );
            """
            };

            addFunc.configureDatabase(createStatements);
        }catch (DataAccessException ex)
        {
            throw new RuntimeException(String.format("Error: UserTable failed to be added, cannot continue. Error: %s",ex.getMessage()));
        }

    }

    public void createUser(UserData user) throws DataAccessException
    {
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?,?,?)";
        createUserHelper(statement, user);
    }


    public UserData getUser(String username) throws DataAccessException
    {
        var statement = "SELECT * FROM UserData WHERE username = ?";
        return getUserHelper(statement, username);
    }


    public void clearUsers() throws DataAccessException
    {
        String statement = "TRUNCATE TABLE UserData";
        addFunc.clearHelper(statement);
    }

    private void createUserHelper(String sqlStatement, UserData user) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection())
        {
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
            {
                preparedStatement.setString(1,user.username());
                preparedStatement.setString(2,user.password());
                preparedStatement.setString(3,user.email());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }
    }

    private UserData getUserHelper(String sqlStatement, String username) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlStatement))
        {
            preparedStatement.setString(1,username);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    String usernameFromDB = resultSet.getString(1);
                    String passwordFromDB = resultSet.getString(2);
                    String emailFromDB = resultSet.getString(3);


                    return new UserData(usernameFromDB,passwordFromDB,emailFromDB);
                }
            }
        } catch(SQLException sqlError)
        {
            String errorMessage = String.format("Unable to update database: %s, %s",sqlStatement,sqlError.getMessage());
            throw new DataAccessException(errorMessage);
        }

        return null;
    }

}
