package dataaccess;

import exception.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MySQLAuthDaoTest
{

    MySQLAuthDao SQLAuthDao;

    @BeforeEach
    void setUp()
    {
        try
        {
            DatabaseManager.createDatabase();
            SQLAuthDao = new MySQLAuthDao();
        }catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown()
    {

    }

    @Test
    void createAuth()
    {
        assertDoesNotThrow(() -> SQLAuthDao.createAuth(new AuthData("Bob","AT ABC")));
    }

    @Test
    void getAuth() throws DataAccessException
    {
        assertDoesNotThrow(() -> SQLAuthDao.getAuth("AT ABC"));
        AuthData auth = SQLAuthDao.getAuth("AT ABC");
        assertNotNull(auth);
        assertEquals("AT ABC", auth.authToken());
        assertEquals("Bob", auth.username());
    }
}