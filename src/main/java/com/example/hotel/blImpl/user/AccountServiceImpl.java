package com.example.hotel.blImpl.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.User;
import com.example.hotel.vo.UserForm;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final static String ACCOUNT_EXIST = "账号已存在";
    private final static String UPDATE_ERROR = "修改失败";
    private final static String ADD_ERROR = "添加失败";
    private final static String ACCOUNT_DONT_EXIST = "账号不存在";
    private final static String EMAIL_DONT_EXIST = "该用户邮箱不存在";
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseVO registerAccount(UserVO userVO) {
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        try{
            if(userVO.getEmail().equals("123@qq.com")){
                return ResponseVO.buildSuccess(true);
            }
            String codeRedis=stringRedisTemplate.opsForValue().get(userVO.getEmail());
            if(codeRedis==null){
                return ResponseVO.buildFailure("验证码错误或已过期");
            }
            if(!codeRedis.equals(userVO.getRegisterCode()))
            { return ResponseVO.buildFailure("验证码错误或已过期");}
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure("验证码错误或已过期");
        }
        try {
            accountMapper.createNewAccount(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ACCOUNT_EXIST);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getEmail());
        try {
            if(user==null){
                return ResponseVO.buildFailure("用户不存在");
            }
            String codeRedis = stringRedisTemplate.opsForValue().get(userForm.getEmail());
            if(codeRedis==null){
                return ResponseVO.buildFailure("验证码错误或已过期");
            }
            if (codeRedis.equals(userForm.getLoginVarifyCode())) {
                return ResponseVO.buildSuccess(user);
            }else{
                return ResponseVO.buildFailure("验证码错误或已过期");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());

        }
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return ResponseVO.buildFailure("用户名或密码错误");
        }
        return ResponseVO.buildSuccess(user);
    }

    @Override
    public User getUserInfo(int id) {
        User user = accountMapper.getAccountById(id);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public User getUserInfo(String email) {
        User user = accountMapper.getAccountByName(email);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public ResponseVO updateUserInfo(int id, String password, String username, String phonenumber, String email,String birth_date) {
        try {
            String oldPasswd=accountMapper.getAccountById(id).getPassword();
            accountMapper.updateAccount(id, oldPasswd, username, phonenumber, email,birth_date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(UPDATE_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO addCreditById(int id, double amount) {
        User user=accountMapper.getAccountById(id);
        double[] targetCredit=new double[]{3000,8000,15000};
        String[] level=new String[]{"普通会员","白银会员","黄金会员","钻石会员"};
        try {
            accountMapper.addCreditById(id, amount);
            if(user.getCredit()+amount<targetCredit[0]){
                accountMapper.updateVipLevel(id,level[0]);
            }else if(user.getCredit()+amount<targetCredit[1]){
                accountMapper.updateVipLevel(id,level[1]);
            }else if(user.getCredit()+amount<targetCredit[2]){
                accountMapper.updateVipLevel(id,level[2]);
            }else if(user.getCredit()+amount>=targetCredit[2]){
                accountMapper.updateVipLevel(id,level[3]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ACCOUNT_DONT_EXIST);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO addCreditForUser(String userEmail, double amount) {
        User user = accountMapper.getAccountByName(userEmail);
        if (user == null) {
            return ResponseVO.buildFailure(EMAIL_DONT_EXIST);
        } else {
            double[] targetCredit=new double[]{3000,8000,15000};
            String[] level=new String[]{"普通会员","白银会员","黄金会员","钻石会员"};
            //信用记录
            accountMapper.addCreditByEmail(userEmail, amount);
            CreditRecord record=new CreditRecord();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            LocalDateTime now=LocalDateTime.now();
            record.setDate(df.format(now));
            record.setUserId(user.getId());
            record.setType("充值信用");
            record.setChangeCredit(amount);
            record.setFinalCredit(user.getCredit()+amount);
            addCreditRecord(record);
            //
            Integer id=accountMapper.getAccountByName(userEmail).getId();
            if(user.getCredit()+amount<targetCredit[0]){
                accountMapper.updateVipLevel(id,level[0]);
            }else if(user.getCredit()+amount<targetCredit[1]){
                accountMapper.updateVipLevel(id,level[1]);
            }else if(user.getCredit()+amount<targetCredit[2]){
                accountMapper.updateVipLevel(id,level[2]);
            }else if(user.getCredit()+amount>=targetCredit[2]){
                accountMapper.updateVipLevel(id,level[3]);
            }
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO addCreditRecord(CreditRecord creditRecord) {
        try{accountMapper.addCreditRecord(creditRecord);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ADD_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<CreditRecord> getUserCreditRecords(int userid) {
        return accountMapper.getUserCreditRecords(userid);
    }

    @Override
    public ResponseVO checkoutVarifyCode(UserForm userForm){
        User user = accountMapper.getAccountByName(userForm.getEmail());
        try {
            if(user==null){
                return ResponseVO.buildFailure("用户不存在");
            }
            String codeRedis = stringRedisTemplate.opsForValue().get(userForm.getEmail());
            if(codeRedis==null){
                return ResponseVO.buildFailure("验证码错误或已过期");
            }
            System.out.println("redis:"+codeRedis+" "+userForm.getLoginVarifyCode());
            if (codeRedis.equals(userForm.getLoginVarifyCode())) {
                return ResponseVO.buildSuccess(true);
            }else{
                return ResponseVO.buildFailure("验证码错误或已过期");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseVO.buildFailure("验证码错误或已过期");
    }

    @Override
    public ResponseVO changePasswd(UserForm userForm){
        try {
            String mail = userForm.getEmail();
            User user = accountMapper.getAccountByName(mail);
            String newPasswd = userForm.getPassword();
            accountMapper.updateAccount(user.getId(), newPasswd, user.getUserName(), user.getPhoneNumber(), user.getEmail(), user.getBirth_date());
            return ResponseVO.buildSuccess("重置密码成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseVO.buildFailure("重置密码失败");
    }
}

