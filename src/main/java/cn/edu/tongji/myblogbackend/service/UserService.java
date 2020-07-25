package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.dao.UserDAO;
import cn.edu.tongji.myblogbackend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public boolean isExist(String username){
        UserEntity user = getByName(username);
        return null != user;
    }
    public UserEntity getByUserId(String user_id){
        return userDAO.getByUserId(user_id);
    }
    public UserEntity getByName(String username){
        return userDAO.findByUsername(username);
    }
    public UserEntity get(String username, String password){
        return userDAO.getByUsernameAndPassword(username, password);
    }
    public void add(UserEntity user){
        userDAO.save(user);
    }
}
