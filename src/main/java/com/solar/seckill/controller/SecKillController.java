package com.solar.seckill.controller;

import com.solar.seckill.business.SecKillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author hushaoge
 * @date 2024/11/12 9:15
 * @description
 */

@Controller
public class SecKillController {

    @Resource
    private SecKillService secKillService;

    // http://127.0.0.1:8080/init?stock=10
    @RequestMapping("/init")
    @ResponseBody
    public String init(@RequestParam("stock") int stock) {
        String productId = "XJ001";
        secKillService.initStock(productId, stock);
        return "设置成功";
    }

    // http://127.0.0.1:8080/seckill?userId=lisi
    @RequestMapping("/seckill")
    @ResponseBody
    public String seckill(@RequestParam("userId") String userId) {
        String productId = "XJ001";
        boolean result = secKillService.secKill(productId, userId);
        return "秒杀结果：" + result;
    }

}
