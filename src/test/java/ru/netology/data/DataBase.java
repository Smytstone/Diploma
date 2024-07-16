package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DataBase {

    private static final String DB_URL = "spring.datasource.url";

    private static final String USER = "spring.datasource.username";

    private static final String PASS = "spring.datasource.password";


    private static Properties properties = new Properties();

    @SneakyThrows
    public static void clearTables() {
        QueryRunner runner = new QueryRunner();
        String deleteCredit = "DELETE FROM credit_request_entity";
        String deleteOrder = "DELETE FROM order_entity";
        String deletePayment = "DELETE FROM payment_entity";

        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        runner.update(connection, deleteCredit);
        runner.update(connection, deleteOrder);
        runner.update(connection, deletePayment);
    }

    @SneakyThrows
    public static String getStatus(String statusSQL) {
        QueryRunner runner = new QueryRunner();

        try {
            InputStream intStream = new FileInputStream("src/test/resources/application.properties");
            properties.load(intStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String url = properties.getProperty(DB_URL);
        String user = properties.getProperty(USER);
        String pass = properties.getProperty(PASS);
        Connection connection = DriverManager.getConnection(url,user,pass);
        return runner.query(connection, statusSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getStatusPayment() {
        String statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getStatus(statusSQL);
    }

    @SneakyThrows
    public static String getStatusCredit() {
        String statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return getStatus(statusSQL);
    }
}
