package com.Service.impl;

import com.Service.IUserService;
import com.dao.IUserDao;
import com.dao.impl.UserDao;
import com.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService{
    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private IUserDao userDao;
    @Override
    public void regist(User user) throws Exception{
        userDao.addUser(user);
    }

    @Override
    public List<User> getAllUsers(){
        try{
            List<User> listUsers = userDao.getAllUsers();
            return listUsers;
        }catch (Exception e){
            logger.error("UserService.getAllUsers()| "+e.getMessage());
            return new ArrayList<User>();
        }
    }
}
