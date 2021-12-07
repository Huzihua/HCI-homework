package com.example.hotel.blImpl.mail;

import com.example.hotel.bl.mail.EmailService;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @program: hotel
 * @description: 发送邮件的实现类
 * @author: Huzihua
 * @create: 2021-12-05 22:25
 **/
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseVO send(String recever) {
        try {
            String code = "";
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                int temp = random.nextInt(10);
                code += temp;
            }
//            存入Redis 而且时间有效性为5分钟
            stringRedisTemplate.opsForValue().set(recever,code,5, TimeUnit.MINUTES);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("“住哪儿”验证码");
            mailMessage.setText("亲爱的\"住哪儿\"用户,您好:\n" +
                    "       本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n" +
                    "如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");
            mailMessage.setFrom("2567712208@qq.com");
            mailMessage.setTo(recever);
            mailSender.send(mailMessage);
            return ResponseVO.buildSuccess(true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildSuccess(false);
        }

    }
}
