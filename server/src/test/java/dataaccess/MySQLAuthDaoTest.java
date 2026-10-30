package dataaccess;

import dataaccess.sql.DatabaseManager;
import dataaccess.sql.MySQLAuthDao;
import exception.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySQLAuthDaoTest
{

    AuthDao sqlAuthDao;

    @BeforeEach
    void setUp()
    {
        try
        {
            DatabaseManager.createDatabase();
            this.sqlAuthDao = new MySQLAuthDao();
        } catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws DataAccessException
    {
        sqlAuthDao.clearAuths();
    }

    @Test
    void createAuthGood()
    {
        assertDoesNotThrow(() -> sqlAuthDao.createAuth(new AuthData("Bob", "AT ABC")));
    }

    @Test
    void createAuthBad()
    {
        assertThrows(DataAccessException.class, () -> sqlAuthDao.createAuth(new AuthData(null, "AT ABC")));
    }

    @Test
    void getAuth() throws DataAccessException
    {
        String username = "Bob";
        String authToken = "ABC123";

        sqlAuthDao.createAuth(new AuthData(username, authToken));

        assertDoesNotThrow(() -> sqlAuthDao.getAuth(authToken));
        AuthData auth = sqlAuthDao.getAuth(authToken);
        assertNotNull(auth);
        assertEquals(authToken, auth.authToken());
        assertEquals(username, auth.username());
        System.out.printf("%s and %s were successfully added and returned from db%n", username, authToken);
    }

    @Test
    void deleteAuth() throws DataAccessException
    {
        String username = "Bob";
        String authToken = "ABC123";

        sqlAuthDao.createAuth(new AuthData(username, authToken));
        AuthData auth = sqlAuthDao.getAuth(authToken);
        assertNotNull(auth);

        sqlAuthDao.deleteAuth(auth.authToken());
        auth = sqlAuthDao.getAuth(authToken);
        assertNull(auth);

    }

    @Test
    void clearAuth()
    {
        assertDoesNotThrow(() -> sqlAuthDao.clearAuths());
    }

}