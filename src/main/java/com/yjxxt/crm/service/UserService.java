package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.Model.UserModel;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;

    public UserModel userLogin(String userName,String userPwd){
        //判断用户和密码是否存在
        checkUserLoginParam(userName,userPwd);

        //用户是否存在
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp ==null,"用户不存在");
        //密码是否正确
        checkUserPwd(userPwd,temp.getUserPwd());
        //创建对象 然后返回
        return builderUserInfo(temp);



    }

    private void checkUserLoginParam(String userName, String userPwd) {
        //用户非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户不能为空");
        //密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }


    /**
     * 构建返回的目标对象
     * @param temp
     * @return
     */
    private UserModel builderUserInfo(User temp) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(temp.getId()));
        userModel.setUserName(temp.getUserName());
        userModel.setTrueName(temp.getTrueName());


        return userModel;
    }
    //返回目标对象
    private void checkUserPwd(String userPwd, String userPwd1) {
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码不正确");
    }
    @Transactional
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPwd){
        //根据用户的Id查询用户信息
        User temp = userMapper.selectByPrimaryKey(userId);
        //参数验证
        checkUserPassword(temp,oldPassword,newPassword,confirmPwd);
        //设定密码加密
        temp.setUserPwd(Md5Util.encode(newPassword));
        //更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(temp)<1,"用户密码更新失败了");
    }


    /**
     * 用户名密码验证
     * @param temp
     * @param oldPassword
     * @param newPassword
     * @param confirmPwd
     */
    private void checkUserPassword(User temp, String oldPassword, String newPassword, String confirmPwd) {
        //当前用户不能3为空
        AssertUtil.isTrue(temp==null,"用户未登录或者不存在");
        //原始密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"用户密码不能为空");
        //原始密码和数据中的密文密码一致
        AssertUtil.isTrue(!(temp.getUserPwd().equals(Md5Util.encode(oldPassword))),"用户原始密码错误");
        //新密码非空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        //新密码和原始密码不能一致
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码和原始密码不能一致");
        //确认密码非空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能非空");
        //确认密码和新密码要一致
        AssertUtil.isTrue(!(confirmPwd.equals(newPassword)),"确认密码和新密码不一致");
    }


    //查询所有的销售人员
    public List<Map<String,Object>> querySales(){
        return userMapper.selectSales();
    }

//------------------------------------------------------------------------------------
    //设置分页查询

    /**
     * 用户模块的列表查询
     * 条件
     * @param userQuery 查询条件
     * @return
     */
    public Map<String,Object> findUserByParams(UserQuery userQuery){
        //实例化map
        Map<String,Object> map = new HashMap<String,Object>();
        //初始化分页单位
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //开始分页
        PageInfo<User> pageInfo = new PageInfo<>(selectByParams(userQuery));
        //存储数据
        map.put("code",0);
        map.put("msg","success");
        map.put("result",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }


    /**
     * 用户页面中 弹出页面需 添加的业务代码
     * 一、验证
     *  用户名 唯一且非空
     *  手机号  非空 格式正确
     *  邮箱  非空
     *  二、 默认值
     *  三、判断是否添加成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //验证
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
        //用户名唯一
        User temp = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(temp != null,"该用户已存在");
        //默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //密码加密
        user.setUserPwd(Md5Util.encode("123456"));
        //三、判断是否添加成功
        //在此处需使用insertHasKey添加，重新将sql语句生成，使之能够获得roleids
       // AssertUtil.isTrue(insertSelective(user)<1,"添加失败了！！！");
        AssertUtil.isTrue(insertHasKey(user)<1,"添加失败了！！！");

        relaionUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 操作中间表 将两个表 user userRole进行关联
     * @param id  用户id
     * @param roleIds  角色id
     */
    private void relaionUserRole(Integer id, String roleIds) {
        //user id ， role id
        //判断角色信息是否取到
        AssertUtil.isTrue();
        //创建集合存储对象
    }


    /**
     *验证
     *   用户名 唯一且非空
     *   手机号  非空 格式正确
     *    邮箱  非空
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUser(String userName, String email, String phone) {
       AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
       AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
       AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
       AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"请输入合法的手机号");
    }
    /**
     * 用户页面修改的业务代码
     * 一、验证
     * 判断用户id是否已经存在
     *  用户名 唯一且非空
     *  手机号  非空 格式正确
     *  邮箱  非空
     *  二、 默认值
     *  update时间
     *  三、判断是否添加成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeUser(User user){
        //获取用户id
        User temp = userMapper.selectByPrimaryKey(user.getId());
        //验证是否存在
        AssertUtil.isTrue(temp == null,"待修改的记录不存在");
        //验证非空问题
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
        //用户名唯一
        User temp2 = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(temp2 != null && (temp2.getId().equals(user.getId())),"该用户名称已经存在");
        //设定默认值
        user.setUpdateDate(new Date());
        //判断是否添加成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"添加失败了！！！");

        relaionUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 删除数据
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeUserIds(Integer[] ids){
        //验证
        AssertUtil.isTrue(ids == null || ids.length == 0 ,"请选择要删除的数据");
        //判断是否删除成功
        AssertUtil.isTrue( userMapper.deleteBatch(ids)<1,"删除数据失败了");

    }

}
