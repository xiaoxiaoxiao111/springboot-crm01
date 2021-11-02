package com.yjxxt.crm.controller;

import com.yjxxt.crm.Model.UserModel;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Resource
    private UserService userService;

    /**
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        //try{
            UserModel userModel = userService.userLogin(userName,userPwd);
            resultInfo.setResult(userModel);
       /* }catch (ParamsException ex){
            ex.printStackTrace();
            resultInfo.setCode(ex.getCode());
            resultInfo.setMsg(ex.getMsg());
        }catch (Exception e){
            //打印错误信息
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败");
        }*/
        return resultInfo;
    }


    @RequestMapping("/setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.updateByPrimaryKeySelective(user);
        //返回目标数据对象
        return resultInfo;
    }



    @RequestMapping("updateUserPwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req, String oldPassword, String newPassword, String confirmPwd){
        //实例化对象
        ResultInfo resultInfo=new ResultInfo();
        //根据Cookie获取用户的ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码操作
        //try{
            userService.updateUserPassword(userId,oldPassword,newPassword,confirmPwd);
        /*}catch (ParamsException pex){
            pex.printStackTrace();
            resultInfo.setCode(pex.getCode());
            resultInfo.setMsg(pex.getMsg());
        }catch (Exception ex){
            ex.printStackTrace();

            resultInfo.setMsg("操作失败");
            resultInfo.setCode(500);
        }*/
        //返回目标对象
        return resultInfo;
    }



    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        //转发到目标页面
        return "user/password";
    }

    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest request){
        //通过工具类，从cookie中获取userid
        Integer userId = LoginUserUtil.releaseUserIdFromCookie( request);
        //根据查询用户的信息
        User user = userService.selectByPrimaryKey(userId);
        //存储
        request.setAttribute("user",user);

        //转发到目标页面
        return "user/setting";
    }

    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String, Object>> findSales(){
        List<Map<String, Object>> list = userService.querySales();
        return list;
    }


//----------------------------------------------------------------------------------------------
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){

        return userService.findUserByParams(userQuery);
    }

    @RequestMapping("index")
    public String index(){
        //转发到目标页面
        return "user/user";
    }
    //用户管理的添加的弹出页面
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user){
        //用户添加
        userService.addUser(user);
        //返回目标对象
        return success("用户添加成功！！");
    }
    //用户管理的编辑按钮的修改弹出页面
    @RequestMapping("change")
    @ResponseBody
    public ResultInfo change(User user){
        //用户修改
        userService.changeUser(user);
        //返回目标对象
        return success("用户修改成功！！");
    }

    //用户管理的添加按钮的弹出页面
    @RequestMapping("addOrUpdatePage")
    public String addOrUpdatePage(Integer id, Model model){
        if (id != null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }

    //批量删除数据
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo dalete(Integer[] ids){
        userService.removeUserIds(ids);

        return success("批量删除成功！！！");
    }

}
