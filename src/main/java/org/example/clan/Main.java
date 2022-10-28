package org.example.clan;

import spark.Spark;

public class Main {
    public static void main(String[] args) throws Exception {
        Spark.staticFileLocation("public");
        ServiceLocator.init();
        ServiceLocator.getDbInitializer().createTables();
    }
}