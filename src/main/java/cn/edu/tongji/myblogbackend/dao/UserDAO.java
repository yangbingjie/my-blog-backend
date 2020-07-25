package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    UserEntity getByUsernameAndPassword(String username, String password);

    UserEntity getByUserId(String user_id);
}
