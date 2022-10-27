package org.example.clan.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User getUser(long userId);

    List<User> getAllUsers();

    Optional<User> findUserByName(String userName);

    void createUser(String userName, int gold);

    void setUserGold(long userId, int gold);

}
