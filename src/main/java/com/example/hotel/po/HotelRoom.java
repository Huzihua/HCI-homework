package com.example.hotel.po;

import com.example.hotel.enums.RoomType;

public class HotelRoom {
    private Integer id;
    private RoomType roomType;
    private Integer hotelId;
    private double price;
    /**
     * 当前剩余可预定房间数
     */
    private int curNum;
    /**
     * 某类型房间总数
     */
    private int total;

    /**
     * 建议入住人数
     */
    private Integer peopleNum;

    /**
     * 床型
     */
    private String bedType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum() {
        if(roomType==null){
            this.peopleNum = 0;
        }else if(roomType==RoomType.Family){
            this.peopleNum = 4;
        }else{
            this.peopleNum = 2;
        }
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType() {
        if(roomType==null){
            this.bedType = "";
        }else if(roomType==RoomType.BigBed){
            this.bedType = "大床1.8m";
        }else if(roomType==RoomType.DoubleBed){
            this.bedType = "双床1.5m";
        }else{
            this.bedType = "大床2m，小床1.5m";
        }
    }
}
