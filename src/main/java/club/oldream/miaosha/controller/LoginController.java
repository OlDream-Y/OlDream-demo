package club.oldream.miaosha.controller;


import club.oldream.miaosha.result.CodeMsg;
import club.oldream.miaosha.result.Result;
import club.oldream.miaosha.service.MiaoShaUserService;
import club.oldream.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Validated LoginVo loginVo){
        log.info(loginVo.toString());
        //登录
        String token = miaoShaUserService.login(response, loginVo);
        return Result.success(token);
    }
}
