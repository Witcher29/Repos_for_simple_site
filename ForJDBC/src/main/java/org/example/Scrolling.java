package org.example;

import java.sql.*;

public class Scrolling {
    public static void main(String[] args) throws ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, userName, password);
            Statement stat = connection.createStatement()){
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//insensitive - можем бегать вверх вниз, но без учёта изменений
//            PreparedStatement preparedStatement = connection.prepareStatement("", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery("select * from books");
            while (resultSet.next())
                System.out.println(resultSet.getString("name"));
            while (resultSet.previous())//в обратную сторону
                System.out.println(resultSet.getString("name"));
            System.out.println("--------------------");

            if(resultSet.relative(2)) //на 2 колонки вперёд, выведет вторую запись в таблице
                System.out.println(resultSet.getString("name"));
            if(resultSet.relative(-1)) // на 1 назад
                System.out.println(resultSet.getString("name"));
            if(resultSet.absolute(2)) // смещение относительно самого начала таблицы, а не относительно текущего положения
                System.out.println(resultSet.getString("name"));

            if(resultSet.first())
                System.out.println(resultSet.getString("name"));
            if(resultSet.last())
                System.out.println(resultSet.getString("name"));
            System.out.println("--------------------");

            Statement statement1 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet1 = statement1.executeQuery("select * from books");
            resultSet1.last();
            resultSet1.updateString("name", "new hell");
            resultSet1.updateRow();//чтобы изменения вступили в силу

            resultSet1.moveToInsertRow();//переходим к позиции для вставки новой строки
            resultSet1.updateString("name", "inserted row");
            resultSet1.insertRow();

            resultSet1.moveToInsertRow();//переходим к позиции для вставки новой строки
            resultSet1.updateString("name", "inserted row2");
            resultSet1.insertRow();
            resultSet1.absolute(4);
            resultSet1.deleteRow();

            resultSet1.beforeFirst();//для того чтобы применить next с первого же элемента
            while (resultSet1.next()){
                System.out.println(resultSet1.getString("name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
