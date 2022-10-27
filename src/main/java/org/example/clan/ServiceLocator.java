package org.example.clan;

import org.example.clan.clan.ClanRepository;
import org.example.clan.clan.ClanRepositoryImpl;
import org.example.clan.clan.ClanService;
import org.example.clan.clan.ClanServiceImpl;
import org.example.clan.task.TaskRepository;
import org.example.clan.task.TaskRepositoryImpl;
import org.example.clan.task.TaskService;
import org.example.clan.task.TaskServiceImpl;
import org.example.clan.transaction.gold.GoldTransactionRepository;
import org.example.clan.transaction.gold.GoldTransactionRepositoryImpl;
import org.example.clan.transaction.gold.GoldTransactionService;
import org.example.clan.transaction.gold.GoldTransactionServiceImpl;
import org.example.clan.user.UserRepository;
import org.example.clan.user.UserRepositoryImpl;
import org.example.clan.user.UserService;
import org.example.clan.user.UserServiceImpl;
import org.example.clan.util.ConnectionManager;

public class ServiceLocator {
    private static ServiceLocator INSTANCE;
    private final ConnectionManager connectionManager;
    private final DbInitializer dbInitializer;
    private final UserRepository userRepository;
    private final ClanRepository clanRepository;
    private final TaskRepository taskRepository;
    private final GoldTransactionRepository goldTransactionRepository;
    private final UserService userService;
    private final ClanService clanService;
    private final TaskService taskService;
    private final GoldTransactionService goldTransactionService;

    public static synchronized void init() {
        if (INSTANCE != null) {
            return;
        }
        try {
            INSTANCE = new ServiceLocator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionManager getConnectionManager() {
        return getInstance().connectionManager;
    }

    public static DbInitializer getDbInitializer() {
        return getInstance().dbInitializer;
    }

    public static UserRepository getUserRepository() {
        return getInstance().userRepository;
    }

    public static ClanRepository getClanRepository() {
        return getInstance().clanRepository;
    }

    public static TaskRepository getTaskRepository() {
        return getInstance().taskRepository;
    }

    public static GoldTransactionRepository getGoldTransactionRepository() {
        return getInstance().goldTransactionRepository;
    }

    public static UserService getUserService() {
        return getInstance().userService;
    }

    public static ClanService getClanService() {
        return getInstance().clanService;
    }

    public static TaskService getTaskService() {
        return getInstance().taskService;
    }

    public static GoldTransactionService getGoldTransactionService() {
        return getInstance().goldTransactionService;
    }

    private ServiceLocator() throws Exception {
        connectionManager = new ConnectionManager(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );
        dbInitializer = new DbInitializer(connectionManager);
        userRepository = new UserRepositoryImpl(connectionManager);
        clanRepository = new ClanRepositoryImpl(connectionManager);
        taskRepository = new TaskRepositoryImpl(connectionManager);
        goldTransactionRepository = new GoldTransactionRepositoryImpl(connectionManager);
        userService = new UserServiceImpl(userRepository);
        clanService = new ClanServiceImpl(clanRepository);
        taskService = new TaskServiceImpl(taskRepository);
        goldTransactionService = new GoldTransactionServiceImpl(goldTransactionRepository, userService, clanService, taskService);
    }

    private static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ServiceLocator hasn't been initialized");
        }
        return INSTANCE;
    }
}
