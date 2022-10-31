package org.example.clan.user;

import org.example.clan.transaction.gold.GoldTransactionService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GoldTransactionService goldTransactionService;

    public UserServiceImpl(UserRepository userRepository, GoldTransactionService goldTransactionService) {
        this.userRepository = userRepository;
        this.goldTransactionService = goldTransactionService;
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
    public void createUser(String userName, int gold) throws InterruptedException {
        User user = new User(0, userName, 0);
        userRepository.createUser(user);
        user = findUserByName(userName).orElseThrow(() -> new IllegalStateException("User not found after creation"));
        goldTransactionService.addGoldToUser(user.getId(), gold, "Initial gold");
    }
}
