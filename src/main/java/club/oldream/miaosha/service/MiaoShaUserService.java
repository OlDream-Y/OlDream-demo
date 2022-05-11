package club.oldream.miaosha.service;

import club.oldream.miaosha.dao.MiaoshaUserDao;
import club.oldream.miaosha.domain.MiaoshaUser;
import club.oldream.miaosha.exception.GlobalException;
import club.oldream.miaosha.redis.MiaoshaUserKey;
import club.oldream.miaosha.redis.RedisService;
import club.oldream.miaosha.result.CodeMsg;
import club.oldream.miaosha.util.MD5Util;
import club.oldream.miaosha.util.UUIDUtil;
import club.oldream.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserService {

    public static final String COOKI_NAME_TOKEN = "token";
    @Autowired
    private MiaoshaUserDao miaoshaUserDao;
    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(Long id){
        return miaoshaUserDao.getById(id);
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token	 = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
