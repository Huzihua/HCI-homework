package com.example.hotel.blImpl.mail;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailServiceImplTest extends TestCase {
    @Autowired
    JavaMailSenderImpl mailSender;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void testsend() {
        String code="";
        Random random=new Random();
        for(int i=0;i<6;i++){
            int temp=random.nextInt(10);
            code+=temp;
        }
        stringRedisTemplate.opsForValue().set("2567712208@qq.com",code,5, TimeUnit.MINUTES);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("“住哪儿”验证码");
        mailMessage.setText("亲爱的\"住哪儿\"用户,您好:\n" +
                "       本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n" +
                "如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");
        mailMessage.setFrom("2567712208@qq.com");
        mailMessage.setTo("181250047@smail.nju.edu.cn");
        mailSender.send(mailMessage);
        System.out.println(code);
        System.out.println(stringRedisTemplate.opsForValue().get("2567712208@qq.com"));
        Assert.assertEquals(code,stringRedisTemplate.opsForValue().get("2567712208@qq.com"));
    }
}