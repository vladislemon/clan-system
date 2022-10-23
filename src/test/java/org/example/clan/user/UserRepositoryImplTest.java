package org.example.clan.user;

import org.example.clan.DbInitializer;
import org.example.clan.TestUtils;
import org.example.clan.util.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryImplTest {
    static ConnectionManager connectionManager;
    UserRepositoryImpl repository = new UserRepositoryImpl(connectionManager);

    @BeforeAll
    static void beforeAll() throws SQLException {
        connectionManager = new ConnectionManager("jdbc:h2:mem:test", "", "");
        new DbInitializer(connectionManager).createUsersTable();
    }

    @Test
    void testCreateAndGetUsers() {
        User expectedUser1 = new User(1, "user1", 0);
        User expectedUser2 = new User(2, "user2", 100);
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser1);
        expectedUsers.add(expectedUser2);
        repository.createUser(expectedUser1);
        repository.createUser(expectedUser2);
        User actualUser1 = repository.getUser(expectedUser1.getId());
        assertEquals(expectedUser1, actualUser1);
        List<User> actualUsers = repository.getAllUsers();
        TestUtils.assertContainsEqualElements(expectedUsers, actualUsers);
    }
}