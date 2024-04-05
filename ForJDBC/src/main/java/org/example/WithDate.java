package org.example;

import java.sql.*;

public class WithDate {
    public static void main(String[] args) throws ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement()){
            statement.execute("drop table if EXISTS workers");
            statement.executeUpdate("create table if NOT EXISTS workers (id int NOT NULL AUTO_INCREMENT, dt DATE, PRIMARY KEY(id) )");

            PreparedStatement preparedStatement = connection.prepareStatement("insert into workers (dt) values (?)");
            preparedStatement.setDate(1, new Date(1710072000L));
            preparedStatement.execute();
            System.out.println(preparedStatement);//покажет дату в нормальном виде

            statement.executeUpdate("insert into workers (dt) values ('1980-01-20')");
            ResultSet resultSet = preparedStatement.executeQuery("select * from workers");
            while (resultSet.next()){
                System.out.println(resultSet.getDate("dt"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
