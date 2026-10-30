package dataaccess.sql;

import exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdditionalFunctions
{
    public AdditionalFunctions(){}

    void configureDatabase(String[] createStatements) throws DataAccessException
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

    void clearHelper(String sqlStatement) throws DataAccessException
    {
        try(Connection databaseConnection = DatabaseManager.getConnection())
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
}
