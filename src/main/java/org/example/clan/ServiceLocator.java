package org.example.clan;

import org.example.clan.clan.ClanRepository;
import org.example.clan.clan.ClanRepositoryImpl;
import org.example.clan.clan.ClanService;
import org.example.clan.clan.ClanServiceImpl;
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
    private final UserService userService;
    private final ClanService clanService;

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

    public static UserService getUserService() {
        return getInstance().userService;
    }

    public static ClanService getClanService() {
        return getInstance().clanService;
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
        userService = new UserServiceImpl(userRepository);
        clanService = new ClanServiceImpl(clanRepository);
    }

    private static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ServiceLocator hasn't been initialized");
        }
        return INSTANCE;
    }
}
