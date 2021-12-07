package com.example.hotel.controller.user;

import com.example.hotel.bl.mail.EmailService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.po.User;
import com.example.hotel.vo.UserForm;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserInfoVO;
import com.example.hotel.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController()
@RequestMapping("/api/user")
public class AccountController {
    private final static String ACCOUNT_INFO_ERROR = "用户名或密码错误";
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserForm userForm) {
        System.out.println(userForm.getPassword());
        System.out.println(userForm.getLoginVarifyCode());
        return accountService.login(userForm);

    }

    @PostMapping("/loginWithCode")
    public ResponseVO login(@RequestBody UserVO uservo) {
        User user = accountService.loginWithCode(uservo);
        if (user == null) {
            return ResponseVO.buildFailure("用户不存在或验证码错误");
        }
        return ResponseVO.buildSuccess(user);

    }

    @PostMapping("/register")
    public ResponseVO registerAccount(@RequestBody UserVO userVO) {
        userVO.setBirth_date("未填写");
        userVO.setPhoneNumber("未填写");
        return accountService.registerAccount(userVO);
    }

//    发送验证码到邮箱
    @PostMapping("/sendMail")
    public ResponseVO sendMail(@RequestBody String recever){
        recever=recever.replace("%40","@");
        recever=recever.substring(0,recever.length()-1);
        System.out.println(recever);
        return emailService.send(recever);
    }


    @GetMapping("/{id}/getUserInfo")
    public ResponseVO getUserInfo(@PathVariable int id) {
        User user = accountService.getUserInfo(id);
        if(user==null){
            return ResponseVO.buildFailure(ACCOUNT_INFO_ERROR);
        }
        return ResponseVO.buildSuccess(user);
    }

    @PostMapping("/{id}/userInfo/update")
    public ResponseVO updateInfo(@RequestBody UserInfoVO userInfoVO,@PathVariable int id){
        return accountService.updateUserInfo(id,userInfoVO.getPassword(),userInfoVO.getUserName(),userInfoVO.getPhoneNumber(),userInfoVO.getEmail(),userInfoVO.getBirth_date());

    }
    @PostMapping("/{userEmail}/{amount}/userInfo/addCredit")
    public ResponseVO addCreditForUser(@PathVariable String userEmail, @PathVariable double amount) {
        return accountService.addCreditForUser(userEmail,amount);
    }
    @GetMapping("/{userId}/getUserCreditRecords")
    public ResponseVO retrieveUserCreditRecords(@PathVariable int userId) {
        return ResponseVO.buildSuccess(accountService.getUserCreditRecords(userId));
    }
}
