package com.mm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.modules.sys.entity.SysMenuEntity;

import java.util.List;


/**
 * 菜单管理
 *
 * @author lwl
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 根据用户获取全部权限
     *
     * @param userId
     * @return
     */
    List<String> getPermsByUserId(Long userId);

    /**
     * 获取用户菜单列表
     */
    List<SysMenuEntity> getUserMenuList(Long userId);

    /**
     * 删除
     */
    void delete(Long menuId);
}
