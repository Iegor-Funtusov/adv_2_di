package ua.com.alevel.dao;

import ua.com.alevel.config.annotation.BeanClass;
import ua.com.alevel.config.annotation.DbProps;
import ua.com.alevel.entity.User;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@BeanClass
public class UserDaoImpl implements UserDao {

    private Connection connection;
    private Statement statement;

    @DbProps("db.driver")
    private String driver;

    @DbProps("db.url")
    private String url;

    @DbProps("db.user")
    private String user;

    @DbProps("db.password")
    private String password;

    private static final String CREATE_USER = "insert into users values(default,?,?)";
    private static final String UPDATE_BY_ID_QUERY = "update users set name = ?, age = ? where id = ?";
    private static final String FIND_ALL_USER_QUERY = "select * from users";
    private static final String FIND_USER_BY_ID_QUERY = "select * from users where id = ";
    private static final String DELETE_USER_BY_ID_QUERY = "delete from users where id = ";

    public UserDaoImpl() {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/adv_2?useSSL=false&serverTimezone=UTC", "root", "root");
//            this.statement = connection.createStatement();
//        } catch (SQLException | ClassNotFoundException e) {
//            System.out.println("problem: = " + e.getMessage());
//        }
    }

    @PostConstruct
    private void init() {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, user, password);
            this.statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("problem: = " + e.getMessage());
        }
    }

    @Override
    public void create(User user) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(CREATE_USER);
            preparedStatement.setInt(1, user.getAge());
            preparedStatement.setString(2, user.getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("problem: = " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection
                    .prepareStatement(UPDATE_BY_ID_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("problem: = " + e.getMessage());
        }
    }

    @Override
    public void delete(long id) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection
                    .prepareStatement(DELETE_USER_BY_ID_QUERY + id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("problem: = " + e.getMessage());
        }
    }

    @Override
    public User find(long id) {
        try {
            ResultSet resultSet = this.statement.executeQuery(FIND_USER_BY_ID_QUERY + id);
            while (resultSet.next()) {
                return initUserByResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("problem: = " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> find() {
        List<User> users = new ArrayList<>();
        try {
            ResultSet resultSet = this.statement.executeQuery(FIND_ALL_USER_QUERY);
            while (resultSet.next()) {
                users.add(initUserByResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("problem: = " + e.getMessage());
        }
        return users;
    }

    private User initUserByResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        long id = resultSet.getLong("id");
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        return user;
    }
}
