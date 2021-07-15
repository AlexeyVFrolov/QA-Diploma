package ru.netology.dbops;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseOperations {

    @SneakyThrows
    private static Connection getConnection() {
        String dbUrl = "jdbc:mysql://localhost:3306/app";
        if (System.getProperty("database").equals("postgresql")){
            dbUrl = "jdbc:postgresql://localhost:5432/app";
        }
        val conn = DriverManager.getConnection(dbUrl, "app", "pass");

        return conn;
    }

    public static boolean isRightDataBasePaymentOrderRecord(String status) {
        val getPaymentOrderPayment_IdSQL = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        val getPaymentStatusSQL = "SELECT status FROM payment_entity WHERE transaction_id = ? ORDER BY created DESC LIMIT 1";
        val runner = new QueryRunner();

        try {
            val paymentId = runner.query(getConnection(), getPaymentOrderPayment_IdSQL, new ScalarHandler<>());

            if (paymentId == null) {
                return false;
            } else {
                val paymentStatus = runner.query(getConnection(), getPaymentStatusSQL, new ScalarHandler<>(), paymentId);

                return paymentStatus.equals(status);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка доступа к базе данных");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean isNoDataBasePaymentOrderRecord() {
        val getPaymentOrderIdSQL = "SELECT id FROM order_entity ORDER BY created DESC LIMIT 1";
        val getPaymentIdSQL = "SELECT id FROM payment_entity ORDER BY created DESC LIMIT 1";
        val runner = new QueryRunner();
        val conn = getConnection();

        try {
            val orderId = runner.query(conn, getPaymentOrderIdSQL, new ScalarHandler<>());
            val paymentId = runner.query(conn, getPaymentIdSQL, new ScalarHandler<>());

            if ((orderId == null) && (paymentId == null)) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Ошибка доступа к базе данных");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean isRightDataBaseCreditOrderRecord(String status) {
        val getCreditOrderCredit_IdSQL = "SELECT credit_id FROM order_entity ORDER BY created DESC LIMIT 1";
        val getCreditStatusSQL = "SELECT status FROM credit_request_entity WHERE bank_id = ? ORDER BY created DESC LIMIT 1";
        val runner = new QueryRunner();
        val conn = getConnection();

        try {
            val creditId = runner.query(conn, getCreditOrderCredit_IdSQL, new ScalarHandler<>());

            if (creditId == null) {
                return false;
            } else {
                val paymentStatus = runner.query(conn, getCreditStatusSQL, new ScalarHandler<>(), creditId);

                return paymentStatus.equals(status);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка доступа к базе данных");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean isNoDataBaseCreditOrderRecord() {
        val getCreditOrderIdSQL = "SELECT id FROM order_entity ORDER BY created DESC LIMIT 1";
        val getCreditIdSQL = "SELECT id FROM credit_request_ ORDER BY created DESC LIMIT 1";
        val runner = new QueryRunner();
        val conn = getConnection();

        try {
            val orderId = runner.query(conn, getCreditOrderIdSQL, new ScalarHandler<>());
            val creditId = runner.query(conn, getCreditIdSQL, new ScalarHandler<>());

            if ((orderId == null) && (creditId == null)) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка доступа к базе данных");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void clearDataBase() {
        val clearPaymentEntityTable = "DELETE FROM payment_entity";
        val clearCreditEntityTable = "DELETE FROM credit_request_entity";
        val clearOrderEntityTable = "DELETE FROM order_entity";
        val runner = new QueryRunner();
        val conn = getConnection();

        try {
            runner.update(conn, clearPaymentEntityTable);
            runner.update(conn, clearCreditEntityTable);
            runner.update(conn, clearOrderEntityTable);
        } catch (SQLException e) {
            System.out.println("Ошибка доступа к базе данных");
            System.out.println(e.getMessage());
        }
    }
}
