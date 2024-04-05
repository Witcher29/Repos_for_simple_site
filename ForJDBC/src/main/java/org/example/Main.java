package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver"); //название драйвера
        try(Connection conn = DriverManager.getConnection(url, userName, password);
        Statement statement = conn.createStatement()){ //для SQL
            System.out.println("We are connected");
            conn.setAutoCommit(false); // пока ничего не выполнится
            statement.execute("drop table WWWorkers"); //execute() для select и т.д.; для внесения изменений с помощью SQL - executeUpdate()
            statement.executeUpdate("create table IF NOT EXISTS WWWorkers (id int not null auto_increment, name VARCHAR(30) not null, PRIMARY KEY(id))");
            statement.executeUpdate("insert into WWWorkers (name) values ('Bob')");
            Savepoint savepoint = conn.setSavepoint();
            statement.executeUpdate("insert into WWWorkers set name = 'Goobka'");

            conn.rollback(savepoint); // выполнится только то что до savepoint
            conn.commit();//выполнится совместно

            ResultSet resultSet = statement.executeQuery("select* from WWWorkers");
            while(resultSet.next()){// Курсор в ResultSet указывает на некоторую строку. Когда курсор перемещается, то метод перемещения курсора возвращает true
                System.out.println(resultSet.getInt("id"));//либо циферкой 1
                System.out.println(resultSet.getString(2));//либо названием поля - name
            }
        }
    }
}