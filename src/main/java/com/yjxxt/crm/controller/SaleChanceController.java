package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;

import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;


    @RequestMapping("/index")
    public String index() {
        return "saleChance/sale_chance";
    }

    @RequestMapping("addOrUpdateDialog")
    public String addOrUpdata(Integer id , Model model) {
        //判断id是否为空
        if(id != null){
            //查询用户信息
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance",saleChance);

        }
        return "saleChance/add_update";
    }

    //条件查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> sayList(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = saleChanceService.querySaleChanceByParams(saleChanceQuery);

        return map;
    }

    @RequestMapping("/save")
    @ResponseBody
    //此页面为销售机会的弹出页面  添加信息
    public ResultInfo save(HttpServletRequest req, SaleChance saleChance) {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();

        //创建人信息
        saleChance.setCreateMan(trueName);

        //添加操作
        saleChanceService.saleChanceService(saleChance);

        return success("添加成功了");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){

        //更新操作
        saleChanceService.changeService(saleChance);
        return success("修改成功了！！！");
    }

    //批量删除
    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids){

        //更新操作
        saleChanceService.removeSaleChanaceIds(ids);
        return success("恭喜批量删除成功了！！！");
    }
}
