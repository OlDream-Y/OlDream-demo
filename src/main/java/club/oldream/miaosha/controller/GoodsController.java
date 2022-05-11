package club.oldream.miaosha.controller;

import club.oldream.miaosha.domain.MiaoshaUser;
import club.oldream.miaosha.redis.GoodsKey;
import club.oldream.miaosha.redis.RedisService;
import club.oldream.miaosha.result.Result;
import club.oldream.miaosha.service.GoodsService;
import club.oldream.miaosha.service.MiaoShaUserService;
import club.oldream.miaosha.vo.GoodsDetailVo;
import club.oldream.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoShaUserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        //取缓存
//    	String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
//    	if(!StringUtils.isEmpty(html)) {
//    		return html;
//    	}
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
//    	 return "goods_list";
        IWebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        //手动渲染
        String html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }




    @RequestMapping(value="/to_detail2/{goodsId}",produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user,
                          @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        //手动渲染
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";

        IWebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

//    @RequestMapping("/to_detail1/{goodsId}")
//    public String listDetail1(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId){
//        model.addAttribute("user",user);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        model.addAttribute("goods",goods);
//
//        long startAt = goods.getStartDate().getTime();
//        long endAt = goods.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//        SimpleDateFormat smt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        int miaoshaStatus = 0;
//        int remainSeconds = 0;
//        if(now < startAt ) {//秒杀还没开始，倒计时
//            miaoshaStatus = 0;
//            remainSeconds = (int)((startAt - now )/1000);
//        }else  if(now > endAt){//秒杀已经结束
//            miaoshaStatus = 2;
//            remainSeconds = -1;
//        }else {//秒杀进行中
//            miaoshaStatus = 1;
//            remainSeconds = 0;
//        }
//        model.addAttribute("miaoshaStatus", miaoshaStatus);
//        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";
//    }

    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
