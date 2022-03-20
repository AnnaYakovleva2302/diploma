package ru.netology.tour.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import lombok.SneakyThrows;

public class DataBaseManager {
    private Connection connection;

    @SneakyThrows
    public DataBaseManager() {
        Properties properties = new Properties();
        File configurationFile = new File("application.properties");
        InputStream input = new FileInputStream(configurationFile);
        properties.load(input);
        connection = DriverManager.getConnection(
            properties.getProperty("spring.datasource.url"), 
            properties.getProperty("spring.datasource.username"), 
            properties.getProperty("spring.datasource.password")
        );
    }

    @SneakyThrows
    public int getOrdersCount() {
        String SQL = "SELECT COUNT(id) FROM order_entity;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getInt(1);
    }

    @SneakyThrows
    public int getLastPaymentAmount() {
        String SQL = "SELECT amount FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getInt("amount");
    }

    @SneakyThrows
    public String getLastPaymentStatus() {
        String SQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getString("status");
    }

    @SneakyThrows
    public String getLastCreditStatus() {
        String SQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getString("status");
    }

    @SneakyThrows
    public String getLastPaymentID() {
        String SQL = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getString("transaction_id");
    }

    @SneakyThrows
    public String getLastCreditID() {
        String SQL = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getString("bank_id");
    }

    @SneakyThrows
    public String getLastTransactionID() {
        String SQL = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1;";
        var state = connection.createStatement();
        var rs = state.executeQuery(SQL);
        rs.next();
        return rs.getString("payment_id");
    }
}