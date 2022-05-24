package com.mm.service.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.service.sys.entity.SysRoleMenuEntity;

import java.util.List;


/**
 * 角色与菜单对应关系
 *
 * @author lwl
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

}
