package com.example.hotel.data.user;

import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AccountMapper {

    /**
     * 创建一个新的账号
     *
     * @return
     */
     int createNewAccount(User user);

    /**
     * 根据用户名查找账号
     * @param email
     * @return
     */
     User getAccountByName(@Param("email") String email);

     User getAccountById(@Param("id") int id);

    /**
     * 更新用户信息
     * @param id
     * @param password
     * @param username
     * @param phonenumber
     * @return
     */
     int updateAccount(@Param("id") int id, @Param("password") String password,@Param("userName") String username, @Param("phoneNumber") String phonenumber,@Param("email") String email,@Param("birth_date") String birth_date);


    int addCreditById(@Param("id") int id, @Param("amount") double amount);

    int addCreditByEmail(@Param("email") String userEmail, @Param("amount") double amount);

    int addCreditRecord(CreditRecord creditRecord);

    List<CreditRecord> getUserCreditRecords(@Param("userid") int userid);

    int updateVipLevel(@Param("id") int id,@Param("vip_level")String vip_level);
}
