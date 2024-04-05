package org.example;

import java.sql.*;

public class SecretData {
    public static void main(String[] args) throws ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement()){
            statement.execute("drop table if EXISTS workers");
            statement.executeUpdate("create table if NOT EXISTS workers (id int NOT NULL AUTO_INCREMENT, name VARCHAR(30) NOT NULL, password CHAR(3) not null, PRIMARY KEY(id) )");
            statement.executeUpdate("insert into workers (name, password) values ('max', '123')");
            statement.executeUpdate("insert into workers SET name = 'Vasa', password = '321'");

            String userId = "1";
//            ResultSet resultSet = statement.executeQuery("select * from workers where id ='" + userId + "'");

            // защищает от sql инъекций
            PreparedStatement preparedStatement = connection.prepareStatement("select * from workers where id = ?");// можно вписать where id = ? and name = ?
            preparedStatement.setString(1, userId);//(это индекс вопросика) установили значение первого параметра, т.е. id, в select выше
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println("UserName: " + resultSet.getString("name"));
                System.out.println("Password: " + resultSet.getString("password"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
