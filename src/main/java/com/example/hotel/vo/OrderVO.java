package com.example.hotel.vo;

import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * @Author: chenyizong
 * @Date: 2020-02-29
 */
public class OrderVO {
    private Integer id;
    private Integer userId;
    private Integer hotelId;
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private Integer roomNum;
    private Integer peopleNum;
    private boolean haveChild;
    private String createDate;
    private Double price;
    private String clientName;
    private String phoneNumber;
    private String orderState;
    private String comment;      //新增属性
    private Double orderRate;
    private String checkInDeadline;//新增：最晚入住时间
    private String checkOutDeadline;//新增：最晚退房时间
    private String actualInTime;//新增，实际入住时间
    private String actualOutTime; //新增，实际退房时间

    public String getActualOutTime() {
        return actualOutTime;
    }

    public void setActualOutTime(String actualOutTime) {
        this.actualOutTime = actualOutTime;
    }

    public String getActualInTime() {
        return actualInTime;
    }

    public void setActualInTime(String actualInTime) {
        this.actualInTime = actualInTime;
    }

    public String getCheckInDeadline() {
        return checkInDeadline;
    }

    public void setCheckInDeadline(String checkInDeadline) {
        this.checkInDeadline = checkInDeadline;
    }


    public String getCheckOutDeadline() {
        return checkOutDeadline;
    }

    public void setCheckOutDeadline(String checkOutDeadline) {
        this.checkOutDeadline = checkOutDeadline;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }
    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public boolean isHaveChild() {
        return haveChild;
    }

    public void setHaveChild(boolean haveChild) {
        this.haveChild = haveChild;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public Double getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(Double orderRate) {
        this.orderRate = orderRate;
    }
}
