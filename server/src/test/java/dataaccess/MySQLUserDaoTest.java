package dataaccess;

import dataaccess.sql.DatabaseManager;
import dataaccess.sql.MySQLUserDao;
import exception.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserDaoTest
{
    UserDao SQLUserDao;

    @BeforeEach
    void setUp()
    {
        try
        {
            DatabaseManager.createDatabase();
            this.SQLUserDao = new MySQLUserDao();
        }catch (DataAccessException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws DataAccessException
    {
        SQLUserDao.clearUsers();
    }

    @Test
    void createUserGood()
    {
        assertDoesNotThrow(() -> SQLUserDao.createUser(new UserData("test","password","email")));
    }

    @Test
    void createUserBad()
    {
        assertThrows(DataAccessException.class, () -> SQLUserDao.createUser(new UserData(null,"password","email")));
    }

    @Test
    void getUser() throws DataAccessException
    {
        String username = "test";
        String password = BCrypt.hashpw("ABC123",BCrypt.gensalt());
        String email = "email";

        SQLUserDao.createUser(new UserData(username,password,email));

        assertDoesNotThrow(() -> SQLUserDao.getUser(username));
        UserData user = SQLUserDao.getUser(username);
        assertNotNull(user);
        assertEquals(username, user.username());
        assertTrue(BCrypt.checkpw("ABC123",user.password()));
        assertEquals(email, user.email());
        System.out.printf("User %s, %s, %s was successfully added and returned from db", username, user.password(), email);
    }

    @Test
    void clearAuth()
    {
        assertDoesNotThrow(() -> SQLUserDao.clearUsers());
    }
}
