package com.springboot.jwt_study.repository;

import com.springboot.jwt_study.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
    int save(User user);
    User findById(int id);
    User findByUsername(String username);
}
