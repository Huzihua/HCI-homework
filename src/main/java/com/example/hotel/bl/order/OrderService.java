package com.example.hotel.bl.order;

import com.example.hotel.po.Order;
import com.example.hotel.vo.CommentVO;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
public interface OrderService {

    /**
     * 预订酒店
     * @param orderVO
     * @return
     */
    ResponseVO addOrder(OrderVO orderVO);

    /**
     * 获得所有订单信息
     * @return
     */
    List<Order> getAllOrders();

    /**
     * 获得指定用户的所有订单信息
     * @param userid
     * @return
     */
    List<Order> getUserOrders(int userid);

    /**
     * 撤销订单
     * @param orderid
     * @return
     */
    ResponseVO annulOrder(int orderid);

    /**
     * 获得异常状态的订单信息
     * @return
     */
    List<Order> getAbnormalOrders();

    /**
     * 执行订单
     * @param orderid
     * @return
     */
    ResponseVO executeOrder(int orderid);

    /**
     * 更新退房信息
     * @param orderid
     * @return
     */
    ResponseVO checkOutOrder(int orderid);

    /**
     * 获得待入住状态的订单信息
     * @return
     */
    List<Order> getCurrentOrders();


    /**
     * 网站营销人员取消异常订单
     * @param orderid
     * @param userid
     * @return
     */
    ResponseVO annulAbnormalOrder(int orderid, int userid);

    /**
     * 根据orderid获取订单
     * @param orderid
     * @return
     */
    OrderVO getOrderById(int orderid);

    /**
     * 查看酒店的所有订单
     * @param hotelId
     * @return
     */
    List<Order> getHotelOrders(Integer hotelId);   //从hotelservce中移过来

    /**
     * 添加订单评论
     * @param orderid
     * @param comment
     * @param orderRate
     * @return
     */
    ResponseVO updateComment(int orderid, String comment, double orderRate);

    /**
     * 查看酒店的所有评价
     * @param hotelId
     * @return
     */
    List<CommentVO> getHotelComments(int hotelId);   //这里是vo的吗

    /**
     * 设置订单多种状态
     * @param orderid
     * @param orderState
     * @return
     */
    ResponseVO setOrderState(int orderid, String orderState);

    /**
     * 更新订单的实际入住和退房时间
     * @param orderid
     * @param actualInTime
     * @param avtualOutTime
     * @return
     */
    ResponseVO updateActualTime(int orderid,String actualInTime,String avtualOutTime);

    /**
     * 将超时订单置为异常，并扣除信用值
     * @param
     * @return
     */
    ResponseVO checkAllLateOrders();

}
