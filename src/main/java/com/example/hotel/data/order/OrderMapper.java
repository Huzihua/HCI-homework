package com.example.hotel.data.order;

import com.example.hotel.po.Order;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Mapper
@Repository
public interface OrderMapper {

    int addOrder(Order order);

    List<Order> getAllOrders();

    List<Order> getUserOrders(@Param("userid") int userid);

    int annulOrder(@Param("orderid") int orderid);

    int executeOrder(@Param("orderid") int orderid);

    Order getOrderById(@Param("orderid") int orderid);

    List<Order> getAbnormalOrders();

    List<Order> getCurrentOrders();

    int updateComment(@Param("orderid") int orderid, @Param("comment") String comment, @Param("orderRate") Double orderRate);
   // List<Order> getHotelOrders(@Param("hotelId") Integer hotelId);

//    List<String> getHotelComments(@Param("hotelid") int hotelid);
    List<Order> getHotelComments(@Param("hotelid") int hotelid);

    int setOrderState (@Param("orderid") int orderid, @Param("orderState") String orderState);

    double getHotelAvgRate(@Param("hotelId") int hotelId);

    //更新最新的订单实际入住时间和实际退房时间

    /**
     * huzihua 2020.07.03 01:10
     * @param orderid
     * @param actualInTime
     * @param actualOutTime
     * @return
     */
    int updateActualTime(@Param("orderid") int orderid,@Param("actualInTime") String actualInTime,@Param("actualOutTime") String actualOutTime);


}

