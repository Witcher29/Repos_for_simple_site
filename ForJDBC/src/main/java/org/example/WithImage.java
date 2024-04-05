package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class WithImage {
    public static void main(String[] args) throws ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement()){
            statement.execute("drop table if EXISTS books");
            statement.executeUpdate("create table if NOT EXISTS books (id int NOT NULL AUTO_INCREMENT, name VARCHAR(30) NOT NULL, img BLOB, PRIMARY KEY(id) )");

            BufferedImage image = ImageIO.read(new File("src/main/Something.jpg"));
            Blob blob = connection.createBlob();
            try(OutputStream outputStream = blob.setBinaryStream(1)) {
                ImageIO.write(image, "jpg", outputStream);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("insert into books (name, img) values (?,?)");
            preparedStatement.setString(1, "inferno");
            preparedStatement.setBlob(2, blob);
            preparedStatement.execute();

            ResultSet resultSet = statement.executeQuery("select * from books");
            while (resultSet.next()){
                BufferedImage image2 = ImageIO.read(blob.getBinaryStream());
                File outputFile = new File("saved.jpg");
                ImageIO.write(image2, "jpg", outputFile);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

