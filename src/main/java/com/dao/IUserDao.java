package com.dao;

import com.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface IUserDao {
    User getUserByUid(String uid);
    void addUser(User user) throws Exception;
    List<User> getAllUsers();
}
