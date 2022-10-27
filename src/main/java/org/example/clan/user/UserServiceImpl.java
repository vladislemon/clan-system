package org.example.clan.user;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(long userId) {
        return userRepository.getUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Optional<User> findUserByName(String userName) {
        return userRepository.findUserByName(userName);
    }

    @Override
    public void createUser(String userName, int gold) {
        User user = new User(0, userName, gold);
        userRepository.createUser(user);
    }

    @Override
    public void setUserGold(long userId, int gold) {
        User user = getUser(userId);
        user.setGold(gold);
        userRepository.updateUser(user);
    }
}
