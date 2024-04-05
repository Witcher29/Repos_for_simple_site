package org.example;

import java.sql.*;

public class ForMetaData {
    public static void main(String[] args) throws ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver"); //название драйвера
        try(Connection conn = DriverManager.getConnection(url, userName, password);
            Statement statement = conn.createStatement()){ //для SQL

            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, null);
            while (resultSet.next()){
                System.out.println(resultSet.getString(3));// тут будет имя таблицы
            }

            ResultSet resultSet1 = statement.executeQuery("select * from books");
            ResultSetMetaData resultSetMetaData = resultSet1.getMetaData();
            for (int i=1; i<resultSetMetaData.getColumnCount(); i++){//начинаем с 1 !!!
                System.out.println(resultSetMetaData.getColumnLabel(i));
                System.out.println(resultSetMetaData.getColumnType(i));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
