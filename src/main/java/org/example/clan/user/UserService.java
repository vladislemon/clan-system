package org.example.clan.user;

import java.util.List;

public interface UserService {

    User getUser(long userId);

    List<User> getAllUsers();

    void createUser(String userName);

}
