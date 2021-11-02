package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;


import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query){
        Map<String,Object> map = new HashMap<>();

        PageHelper.startPage(query.getPage(),query.getLimit());
        PageInfo<SaleChance> plist = new PageInfo<SaleChance>(selectByParams(query));

        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());

        return map;
    }


    //添加营销机会数据 页面弹出框部分
    @Transactional(propagation = Propagation.REQUIRED)
    public void saleChanceService(SaleChance saleChance){

        //判断联系电话是否正确，非空，11位手机号
        //验证输入信息是否正确
        checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //判断状态state为0， 未分配，  设置状态和开发结果为0
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
        }
        //判断状态为1， 已分配    设置状态和开发结果为1，切开发时间为当前时间
        if (!StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //设定默认值 state devResult（0 1 2 3 ）
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);

        //判断是否添加成功
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败了");
    }

    /**
     * 验证
     * //客户名非空
     * //联系人非空
     * //联系电话 非空，11位的合法的手机号
     * @param customerName 客户名称
     * @param linkMan 联系人
     * @param linkPhone 手机
     */
    private void checkSaleChanceParam(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户姓名");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人姓名");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系电话");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的电话号");
    }


    /**
     * 一:验证：
     *  1:当前对象的ID
     *  2.客户名非空
     *  3.联系人非空
     *  4.电话非空，11位合法
     * 二：
     *  设定默认值
     * 三
     *  修改是否成功
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeService(SaleChance saleChance) {
        //验证
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        //判断id是否为空
        AssertUtil.isTrue(temp == null, "待修改的记录不存在");
        checkSaleChanceParam(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        //原来未分配  state devresult 的改变过程
        if (StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //原来已经分配的
        if (StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())) {
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        //设定默认值
        saleChance.setUpdateDate(new Date());
        //判断修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance) < 1, "修改失败了！！！");
    }


    /**
     * 批量删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChanaceIds(Integer[] ids){
        //判断数据是否被选上
        AssertUtil.isTrue(ids == null || ids.length==0,"请选择要删除的数据");

        //判断是否删除成功
        AssertUtil.isTrue(deleteBatch(ids) != ids.length,"批量删除失败了！！！");
    }
}
