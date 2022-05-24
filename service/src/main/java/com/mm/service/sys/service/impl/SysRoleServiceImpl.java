package com.mm.service.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.util.Constant;
import com.mm.common.util.PageUtil;
import com.mm.common.util.Query;
import com.mm.service.sys.dao.SysRoleDao;
import com.mm.service.sys.entity.SysRoleEntity;
import com.mm.service.sys.entity.SysRoleMenuEntity;
import com.mm.service.sys.entity.SysUserRoleEntity;
import com.mm.service.sys.service.SysRoleMenuService;
import com.mm.service.sys.service.SysRoleService;
import com.mm.service.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;


/**
 * 角色
 *
 * @author lwl
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SysRoleEntity> qw = new LambdaQueryWrapper<>();
        qw.like(ObjectUtil.isNotNull(params.get("roleName")), SysRoleEntity::getRoleName, params.get("roleName"));
        qw.apply(Objects.nonNull(params.get(Constant.SQL_FILTER)), (String) params.get(Constant.SQL_FILTER));
        IPage<SysRoleEntity> page = this.page(new Query<SysRoleEntity>().getPage(params), qw);
        return new PageUtil(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(SysRoleEntity role) {
        super.saveOrUpdate(role);
        //保存角色与菜单关系
        List<Long> menuIds = new ArrayList<>();
        getMenuIdList(menuIds, role.getMenuList());
        sysRoleMenuService.saveOrUpdate(role.getId(), menuIds);
        return true;
    }

    private void getMenuIdList(List<Long> menuIds, List<Map<String, Object>> menuList) {
        for (Map<String, Object> map : menuList) {
            menuIds.add(Long.parseLong(map.get("id").toString()));
            if (map.get("children") != null) {
                getMenuIdList(menuIds, (List<Map<String, Object>>) map.get("children"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdList(Collection<? extends Serializable> roleIds) {
        //删除角色
        super.removeByIds(roleIds);

        //删除角色与菜单关联
        sysRoleMenuService.remove(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                .in(SysRoleMenuEntity::getRoleId, roleIds));

        //删除角色与用户关联
        sysUserRoleService.remove(Wrappers.<SysUserRoleEntity>lambdaQuery()
                .in(SysUserRoleEntity::getRoleId, roleIds));
        return true;
    }


}
