package com.example.chatroom.Dao;


import com.example.chatroom.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {
    int addSysUser(User user);

    int countByUserList(Map map);
}
