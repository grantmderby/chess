package dataaccess;

import exception.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySQLAuthDaoTest
{

    //MySQLAuthDao SQLAuthDao;
    AuthDao SQLAuthDao;

    @BeforeEach
    void setUp()
    {
        try
        {
            DatabaseManager.createDatabase();
            //SQLAuthDao = new MySQLAuthDao();
            this.SQLAuthDao = new MySQLAuthDao();
        }catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws DataAccessException
    {
        SQLAuthDao.clearAuths();
    }

    @Test
    void createAuth()
    {
        assertDoesNotThrow(() -> SQLAuthDao.createAuth(new AuthData("Bob","AT ABC")));
    }

    @Test
    void getAuth() throws DataAccessException
    {
        String username = "Bob";
        String authToken = "ABC123";

        SQLAuthDao.createAuth(new AuthData(username,authToken));

        assertDoesNotThrow(() -> SQLAuthDao.getAuth(authToken));
        AuthData auth = SQLAuthDao.getAuth(authToken);
        assertNotNull(auth);
        assertEquals(authToken, auth.authToken());
        assertEquals(username, auth.username());
        System.out.printf("%s and %s were successfully added and returned from db%n",username, authToken);
    }

    @Test
    void deleteAuth() throws DataAccessException
    {
        String username = "Bob";
        String authToken = "ABC123";

        SQLAuthDao.createAuth(new AuthData(username,authToken));
        AuthData auth = SQLAuthDao.getAuth(authToken);
        assertNotNull(auth);

        SQLAuthDao.deleteAuth(auth.authToken());
        auth = SQLAuthDao.getAuth(authToken);
        assertNull(auth);

    }

    @Test
    void clearAuth()
    {
        assertDoesNotThrow(() -> SQLAuthDao.clearAuths());
    }

    void getAndDeleteHelper() throws DataAccessException
    {



    }
}