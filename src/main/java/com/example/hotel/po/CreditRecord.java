package com.example.hotel.po;

public class CreditRecord {
    private int id;
    private String date;
    private int orderId;
    private int userId;
    private String type;
    private double changeCredit;
    private double finalCredit;

    public void setId(int id){this.id=id;}
    public int getId(){return id;}
    public void setOrderId(int orderId){this.orderId=orderId;}
    public int getOrderId(){return orderId;}
    public void setUserId(int userId){this.userId=userId;}
    public int getUserId(){return userId;}
    public void setType(String type){this.type=type;}
    public String getType(){return type;}
    public void setChangeCredit(double changeCredit){this.changeCredit=changeCredit;}
    public double getChangeCredit(){return changeCredit;}
    public void setFinalCredit(double finalCredit){this.finalCredit=finalCredit;}
    public double getFinalCredit(){return finalCredit;}
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}