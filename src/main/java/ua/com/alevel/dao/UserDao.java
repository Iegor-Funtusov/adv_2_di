package ua.com.alevel.dao;

import ua.com.alevel.entity.User;

import java.util.List;

public interface UserDao {

    void create(User user);
    void update(User user);
    void delete(long id);
    User find(long id);
    List<User> find();
}
