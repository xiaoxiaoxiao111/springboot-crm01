package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    /**
     * 通过角色id查询所有的角色信息
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(){
        return roleMapper.queryAllRoles();
    }
}
