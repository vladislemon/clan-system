package org.example.clan;

import spark.Spark;

public class Main {
    public static void main(String[] args) throws Exception {
        ServiceLocator.init();
        ServiceLocator.getDbInitializer().createTables();
        Spark.staticFileLocation("public");
        Spark.get("/hello", (request, response) -> "Hello");
    }
}