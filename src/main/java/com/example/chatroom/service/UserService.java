package com.example.chatroom.service;

import com.example.chatroom.Dao.UserDao;
import com.example.chatroom.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public int insterUser(User user) {
        return userDao.addSysUser(user);
    }

    public int countByUserList(Map map) {
        return userDao.countByUserList(map);
    }
}
