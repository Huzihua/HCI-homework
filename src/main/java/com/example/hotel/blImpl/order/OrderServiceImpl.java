package com.example.hotel.blImpl.order;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.vo.CommentVO;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final static String RESERVE_ERROR = "预订失败";
    private final static String ROOMNUM_LACK = "预订房间数量剩余不足";
    private final static String ANNUL_ERROR = "删除失败";
    private final static String EXECUTE_ERROR = "最早入住时间未到";
    private final static String CHECKOUT_ERROR = "退房失败";
    private final static String CREDIT_LACK = "信用值不足";
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    HotelService hotelService;
    @Autowired
    AccountService accountService;

    @Autowired
    private OrderService orderService;  //新导入

    @Override
    public ResponseVO addOrder(OrderVO orderVO) {
        int reserveRoomNum = orderVO.getRoomNum();
        int curNum = hotelService.getRoomCurNum(orderVO.getHotelId(),orderVO.getRoomType());
        if(reserveRoomNum>curNum){
            return ResponseVO.buildFailure(ROOMNUM_LACK);
        }
        else if(accountService.getUserInfo(orderVO.getUserId()).getCredit()<=0){
            return ResponseVO.buildFailure(CREDIT_LACK);
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String curdate = sf.format(date);
            orderVO.setCreateDate(curdate);
            orderVO.setOrderState("已预订");
            User user = accountService.getUserInfo(orderVO.getUserId());

            orderVO.setClientName(user.getUserName());
            orderVO.setPhoneNumber(user.getPhoneNumber());
            orderVO.setCheckInDeadline(orderVO.getCheckInDate()+" 22-00-00");
            orderVO.setCheckOutDeadline(orderVO.getCheckOutDate()+" 12-00-00");
            Order order = new Order();
            BeanUtils.copyProperties(orderVO,order);
            orderMapper.addOrder(order);
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(RESERVE_ERROR);
        }
       return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public List<Order> getUserOrders(int userid) {
        return orderMapper.getUserOrders(userid);
    }

    @Override
    public ResponseVO annulOrder(int orderid) {

            //取消订单逻辑的具体实现（注意可能有和别的业务类之间的交互）
            Order ordertoannul=orderMapper.getOrderById(orderid);
            Order order = orderMapper.getOrderById(orderid);
            String checkInDdl = order.getCheckInDeadline();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            LocalDateTime orderCheckInDdl = LocalDateTime.parse(checkInDdl,df);
            LocalDateTime now=LocalDateTime.now();
            if (now.isAfter(orderCheckInDdl)) {
                double amount=-order.getPrice()*0.5;
                accountService.addCreditById(order.getUserId(),amount);
                //信用记录
                CreditRecord record=new CreditRecord();
                record.setDate(df.format(now));
                record.setOrderId(orderid);
                record.setUserId(order.getUserId());
                record.setType("超时撤销订单");
                record.setChangeCredit(amount);
                User user=accountService.getUserInfo(order.getUserId());
                accountService.addCreditById(user.getId(),amount);
                record.setFinalCredit(user.getCredit()+amount);
                accountService.addCreditRecord(record);
                //
            }
            hotelService.updateRoomInfo(ordertoannul.getHotelId(), ordertoannul.getRoomType(), -ordertoannul.getRoomNum());//更新酒店房间
            orderMapper.annulOrder(orderid);
//            orderMapper.setOrderState(orderid, "已撤销");
            return ResponseVO.buildSuccess(true);
        }

    @Override
    public ResponseVO executeOrder(int orderid) {
        //执行订单逻辑的具体实现（注意可能有和别的业务类之间的交互）
        Order ordertoexecute=orderMapper.getOrderById(orderid);
//        ordertoexecute.setOrderState("已入住");
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime checkInDate=LocalDateTime.parse(ordertoexecute.getCheckInDate()+" 00-00-00",df);
        if (now.isBefore(checkInDate)){
            return ResponseVO.buildFailure(EXECUTE_ERROR);
        }
        double amount=ordertoexecute.getPrice();
        //信用记录
        CreditRecord record=new CreditRecord();
        record.setDate(df.format(now));
        record.setOrderId(orderid);
        record.setUserId(ordertoexecute.getUserId());
        record.setType("订单正常执行");
        record.setChangeCredit(amount);
        User user=accountService.getUserInfo(ordertoexecute.getUserId());
        accountService.addCreditById(user.getId(),amount);
        record.setFinalCredit(user.getCredit()+amount);
        accountService.addCreditRecord(record);
        //
        orderMapper.setOrderState(orderid,"已入住");
        orderService.updateActualTime(orderid,df.format(now),"");
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO checkOutOrder(int orderid){
        Order order = orderMapper.getOrderById(orderid);
        hotelService.updateRoomInfo(order.getHotelId(), order.getRoomType(), -order.getRoomNum());
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String checkOutDate = df.format(now);
        try {
            orderService.updateActualTime(orderid,order.getActualInTime(),checkOutDate);
            orderMapper.setOrderState(orderid,"待点评");
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(CHECKOUT_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getCurrentOrders() {
        return orderMapper.getCurrentOrders();
    }

    @Override
    public List<Order> getHotelOrders(Integer hotelId) {   //从hotelservceimpl移过来
        List<Order> orders = orderService.getAllOrders();
        return orders.stream().filter(order -> order.getHotelId().equals(hotelId)).collect(Collectors.toList());
    }

    @Override
    public List<Order> getAbnormalOrders() {
        return orderMapper.getAbnormalOrders();
    }

    @Override
    public ResponseVO annulAbnormalOrder(int orderid, int userid) {
        Order order = orderMapper.getOrderById(orderid);
        try {
            orderMapper.annulOrder(orderid);
            double amount = order.getPrice();
            accountService.addCreditById(userid, amount);
            //信用记录
            CreditRecord record=new CreditRecord();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            LocalDateTime now=LocalDateTime.now();
            record.setDate(df.format(now));
            record.setOrderId(orderid);
            record.setUserId(order.getUserId());
            record.setType("撤销异常订单");
            record.setChangeCredit(amount);
            User user=accountService.getUserInfo(order.getUserId());
            record.setFinalCredit(user.getCredit());
            accountService.addCreditRecord(record);
            //
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ANNUL_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }



    @Override
    public OrderVO getOrderById(int orderid) {
        Order order = orderMapper.getOrderById(orderid);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }

    /**
     * 添加订单评论
     * @param orderid
     * @param comment
     * @param orderRate
     * @return
     */
    @Override
    public ResponseVO updateComment(int orderid, String comment, double orderRate) {
        orderMapper.updateComment(orderid, comment, orderRate);
        orderMapper.setOrderState(orderid,"已评价");
        Order order = orderMapper.getOrderById(orderid);
        double avg = orderMapper.getHotelAvgRate(order.getHotelId());
        hotelService.setNewRate(order.getHotelId(), avg);
        return ResponseVO.buildSuccess(true);
    }

    /**
     * 查看酒店的所有评价
     * @param hotelId
     * @return
     */
    @Override
    public List<CommentVO> getHotelComments(int hotelId) {

        List<Order> orders = orderMapper.getHotelComments(hotelId);
        List<CommentVO> comments = new ArrayList<>();

       // orders.stream().filter(order -> order.getComment()!=null);
        for (Order o: orders){
            if(o.getComment()!=null){
            CommentVO comment = new CommentVO();
            comment.setHotelId(o.getHotelId());
            comment.setUserName(accountService.getUserInfo(o.getUserId()).getUserName());
            comment.setContext(o.getComment());
            comments.add(comment);
            }
        }
        Collections.reverse(comments);
        return comments;
    }

    /**
     * 设置订单多种状态
     * @param orderid
     * @param orderState
     * @return
     */
    @Override
    public ResponseVO setOrderState(int orderid, String orderState){      //设置订单状态
        orderMapper.setOrderState(orderid,orderState);
        return ResponseVO.buildSuccess(true);
    }

    /**
     * 更新订单实际入住和退房时间
     * @param orderid
     * @param actualInTime
     * @param actualOutTime
     * @return
     */
    @Override
    public ResponseVO updateActualTime(int orderid, String actualInTime,String actualOutTime){
        orderMapper.updateActualTime(orderid,actualInTime,actualOutTime);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO checkAllLateOrders(){
        List<Order> allOrder = orderMapper.getAllOrders();
        Date current = new Date(System.currentTimeMillis());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        for(Order order:allOrder){
            if(!order.getOrderState().equals("已预订"))continue;
            Date latestCheckInTime;
            try{
                latestCheckInTime = sf.parse(order.getCheckInDeadline());
            } catch (Exception e){
                System.out.println(e.getMessage());
                continue;
            }
            if(latestCheckInTime.getTime()<current.getTime()){
                double beforeCredit = accountService.getUserInfo(order.getUserId()).getCredit();
                double amount = -order.getPrice();
                Integer orderid=order.getUserId();
                accountService.addCreditById(order.getUserId(),amount);
                orderMapper.setOrderState(order.getId(),"异常");
                //信用记录
                CreditRecord record=new CreditRecord();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
                LocalDateTime now=LocalDateTime.now();
                record.setDate(df.format(now));
                record.setOrderId(orderid);
                record.setUserId(order.getUserId());
                record.setType("置为异常订单");
                record.setChangeCredit(amount);
                record.setFinalCredit(beforeCredit+amount);
                accountService.addCreditRecord(record);
                //
            }
        }
        return ResponseVO.buildSuccess(true);
    }


}

