package org.example.clan.user;

import java.util.List;

public interface UserRepository {

    User getUser(long userId);

    List<User> getAllUsers();

    void createUser(User user);

}
