package com.bin.action.dao;

import com.bin.action.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectLogin(@Param(value = "username") String username, @Param(value = "password") String password);

    String selectQuestion(String username);

    int forgetCheckAnswer(@Param(value = "username") String username,@Param(value = "question") String question,@Param(value = "answer") String answer);

    int updatePasswordByUsername(@Param(value = "username") String username,@Param(value = "password") String password);

    int findByPasswordAndUserId(@Param(value = "password") String password,@Param(value = "id") int userId);

    int checkEmailByUserId(@Param(value = "email") String email,@Param(value = "id") int id);
}