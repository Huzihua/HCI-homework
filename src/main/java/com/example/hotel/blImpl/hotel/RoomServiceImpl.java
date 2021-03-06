package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.data.hotel.RoomMapper;
import com.example.hotel.po.HotelRoom;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<HotelRoom> retrieveHotelRoomInfo(Integer hotelId) {
        List<HotelRoom> hotelRooms = roomMapper.selectRoomsByHotelId(hotelId);
        for(HotelRoom room : hotelRooms){
            room.setPeopleNum();
            room.setBedType();
        }
        return hotelRooms;
    }

    @Override
    public void insertRoomInfo(HotelRoom hotelRoom) {
        try{
            int curNum = roomMapper.getRoomCurNum(hotelRoom.getHotelId(), hotelRoom.getRoomType().name());
            roomMapper.addExistedRoom(hotelRoom.getHotelId(), hotelRoom.getRoomType().name(), hotelRoom.getTotal());
        }
        catch (BindingException e){
            roomMapper.insertRoom(hotelRoom);
        }
    }

    @Override
    public void updateRoomInfo(Integer hotelId, String roomType, Integer rooms) {
        roomMapper.updateRoomInfo(hotelId,roomType,rooms);
    }

    @Override
    public int getRoomCurNum(Integer hotelId, String roomType) {
        return roomMapper.getRoomCurNum(hotelId,roomType);
    }


}
