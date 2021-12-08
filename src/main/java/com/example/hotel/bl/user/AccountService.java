package com.example.hotel.bl.user;

import com.example.hotel.po.User;
import com.example.hotel.vo.*;
import com.example.hotel.po.CreditRecord;
import java.util.List;
/**
 * @author huwen
 * @date 2019/3/23
 */
public interface AccountService {

    /**
     * 注册账号
     *
     * @return
     */
    ResponseVO registerAccount(UserVO userVO);

    /**
     * 用户登录，登录成功会将用户信息保存再session中
     *
     * @return
     */
    ResponseVO login(UserForm userForm);

    /**
     * 获取用户个人信息
     * @param id
     * @return
     */
    User getUserInfo(int id);

    /**
     * 获取用户信息，方法重载
     * @param email
     * @return
     */
    User getUserInfo(String email);

    /**
     * 更新用户个人信息
     *
     * @param id
     * @param password
     * @param username
     * @param phonenumber
     * @return
     */
    ResponseVO updateUserInfo(int id, String password, String username, String phonenumber,String email,String birth_date);

    /**
     * 网站营销人员撤销异常订单，恢复用户信用值
     *
     * @param id
     * @param amount
     * @return
     */
    ResponseVO addCreditById(int id, double amount);

    /**
     * 网站营销人员为用户充值信用值
     *
     * @param userEmail
     * @param amount
     * @return
     */
    ResponseVO addCreditForUser(String userEmail, double amount);

       /**
     * 添加信用记录
     * @param creditRecord
     * @return
     */
    ResponseVO addCreditRecord(CreditRecord creditRecord);

    /**
     * 获得指定用户的所有信用记录
     * @param userid
     * @return
     */
    List<CreditRecord> getUserCreditRecords(int userid);

    /**
     * 校验验证码是否正确
     * @param userForm
     * @return
     */
    ResponseVO checkoutVarifyCode(UserForm userForm);

    /**
     * 用户重置密码
     * @param userForm
     * @return
     */
    ResponseVO changePasswd(UserForm userForm);
}