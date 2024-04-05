package org.example;

import java.sql.*;

public class ForBatch {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver"); //название драйвера
        try(Connection conn = DriverManager.getConnection(url, userName, password);
            Statement statement = conn.createStatement()){ //для SQL
            conn.setAutoCommit(false); //в batchах всё выполняется 1 пачкой
            statement.addBatch("drop table WWWorkers"); //execute() для select и т.д.; для внесения изменений с помощью SQL - executeUpdate()
            statement.addBatch("create table IF NOT EXISTS WWWorkers (id int not null auto_increment, name VARCHAR(30) not null, PRIMARY KEY(id))");
            statement.addBatch("insert into WWWorkers (name) values ('Bob')");
            statement.addBatch("insert into WWWorkers set name = 'Goobka'");
            statement.executeBatch();// вернёт интовый массив, чья длина равна количеству запросов
            conn.commit();//выполнится совместно
        }
    }
}
