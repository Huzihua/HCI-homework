package com.example.hotel.bl.mail;

import com.example.hotel.vo.ResponseVO;

public interface EmailService {
    ResponseVO send(String recever);
}
