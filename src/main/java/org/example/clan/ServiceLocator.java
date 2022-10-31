package org.example.clan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.clan.clan.*;
import org.example.clan.task.*;
import org.example.clan.transaction.gold.*;
import org.example.clan.user.*;
import org.example.clan.util.ConnectionManager;

public class ServiceLocator {
    private static ServiceLocator INSTANCE;
    private final ObjectMapper objectMapper;
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
    private final UserMapper userMapper;
    private final ClanMapper clanMapper;
    private final TaskMapper taskMapper;
    private final GoldTransactionMapper goldTransactionMapper;
    private final UserController userController;
    private final ClanController clanController;
    private final TaskController taskController;
    private final GoldTransactionController goldTransactionController;

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

    public static ObjectMapper getObjectMapper() {
        return getInstance().objectMapper;
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

    public static UserMapper getUserMapper() {
        return getInstance().userMapper;
    }

    public static ClanMapper getClanMapper() {
        return getInstance().clanMapper;
    }

    public static TaskMapper getTaskMapper() {
        return getInstance().taskMapper;
    }

    public static GoldTransactionMapper getGoldTransactionMapper() {
        return getInstance().goldTransactionMapper;
    }

    public static UserController getUserController() {
        return getInstance().userController;
    }

    public static ClanController getClanController() {
        return getInstance().clanController;
    }

    public static TaskController getTaskController() {
        return getInstance().taskController;
    }

    public static GoldTransactionController getGoldTransactionController() {
        return getInstance().goldTransactionController;
    }

    private ServiceLocator() throws Exception {
        objectMapper = new ObjectMapper();
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
        taskService = new TaskServiceImpl(taskRepository);
        goldTransactionService = new GoldTransactionServiceImpl(userRepository, clanRepository, taskRepository, goldTransactionRepository);
        userService = new UserServiceImpl(userRepository, goldTransactionService);
        clanService = new ClanServiceImpl(clanRepository, goldTransactionService);
        userMapper = new UserMapper();
        clanMapper = new ClanMapper();
        taskMapper = new TaskMapper();
        goldTransactionMapper = new GoldTransactionMapper();
        userController = new UserController(userService, userMapper, objectMapper);
        clanController = new ClanController(clanService, clanMapper, objectMapper);
        taskController = new TaskController(taskService, taskMapper, objectMapper);
        goldTransactionController = new GoldTransactionController(goldTransactionService, goldTransactionMapper, objectMapper);
    }

    private static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ServiceLocator hasn't been initialized");
        }
        return INSTANCE;
    }
}
