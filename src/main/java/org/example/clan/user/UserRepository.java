package org.example.clan.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User getUser(long userId);

    List<User> getAllUsers();

    Optional<User> findUserByName(String userName);

    void createUser(User user);

    void updateUser(User user);

    void setUserGold(long userId, int gold);

}
