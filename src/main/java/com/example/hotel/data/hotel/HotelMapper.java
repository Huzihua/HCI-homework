package com.example.hotel.data.hotel;

import com.example.hotel.po.Hotel;
import com.example.hotel.vo.HotelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HotelMapper {

    int insertHotel(Hotel hotel);

    List<HotelVO> selectAllHotel();

    HotelVO selectById(@Param("id") Integer id);

    List<HotelVO> selectHotelByManagerId(@Param("managerId") Integer id);

    int updateHotel(Hotel hotel);

    int deleteHotel(Integer id);

    int setNewRate(@Param("id") Integer id, @Param("rate") Double rate);
}
