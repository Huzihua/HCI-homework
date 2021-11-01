package com.example.hotel.bl.hotel;

import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.Order;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.CommentVO;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.HotelVO;

import java.util.List;

public interface HotelService {

    /**
     * 添加酒店
     * @param hotelVO
     * @throws
     */
    void addHotel(HotelVO hotelVO) throws ServiceException;

    /**
     * 修改酒店信息
     * @param hotelVO
     * @throws
     */
    void updateHotel(HotelVO hotelVO)throws ServiceException;

    /**
     * 删除酒店信息
     * @param hotelVO
     * @throws
     */
    void deleteHotel(HotelVO hotelVO)throws ServiceException;
    /**
     * 预订酒店修改剩余客房信息
     * @param hotelId
     * @param roomType
     * @param rooms
     */
    void updateRoomInfo(Integer hotelId, String roomType,Integer rooms);

    /**
     * 列表获取酒店信息
     * @return
     */
    List<HotelVO> retrieveHotels();

    /**
     * 获取某家酒店详细信息
     * @param hotelId
     * @return
     */
    HotelVO retrieveHotelDetails(Integer hotelId);

    /**
     * 获取酒店工作人员对应管理的酒店
     * @param id
     * @return
     */
    List<HotelVO> getHotelByManagerId(Integer id);

    /**
     * 查看酒店剩余某种房间数量
     * @param hotelId
     * @param roomType
     * @return
     */
    int getRoomCurNum(Integer hotelId,String roomType);

    /**
     * 设置新评分
     * @param hotelId
     * @param avg
     */
    void setNewRate(Integer hotelId, double avg);

}
