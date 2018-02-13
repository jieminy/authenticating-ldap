package com.Service;

import com.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService {
    void regist(User user) throws Exception;
    List<User> getAllUsers();
}
