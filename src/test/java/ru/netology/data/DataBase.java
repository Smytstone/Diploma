package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBase {

    @SneakyThrows
    public static void clearTables() {
        QueryRunner runner = new QueryRunner();
        Connection connection = DriverManager.getConnection(System.getProperty("DB_URL"), "app", "pass");
        String deleteCredit = "DELETE FROM credit_request_entity";
        String deleteOrder = "DELETE FROM order_entity";
        String deletePayment = "DELETE FROM payment_entity";

        runner.update(connection, deleteCredit);
        runner.update(connection, deleteOrder);
        runner.update(connection, deletePayment);
    }

    @SneakyThrows
    public static String getStatus(String statusSQL) {
        QueryRunner runner = new QueryRunner();
        Connection connection = DriverManager.getConnection(System.getProperty("DB_URL"), "app", "pass");
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
