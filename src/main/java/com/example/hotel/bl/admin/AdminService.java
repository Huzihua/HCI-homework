package com.example.hotel.bl.admin;

import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
public interface AdminService {

    /**
     * 添加酒店管理人员账号
     * @param userForm
     * @return
     */
    ResponseVO addManager(UserForm userForm);

    /**
     * 获得所有酒店管理人员信息
     * @return
     */
    List<User> getAllManagers();

    /**
     * 添加网站营销人员
     * @param userForm
     * @return
     */
    ResponseVO addMarketer(UserForm userForm);

    /**
     * 获取网站营销人员
     * @return
     */
    List<User> getAllMarketers();

    /**
     * 获取所有客户信息
     * @return
     */
    List<User> getAllClients();
}
