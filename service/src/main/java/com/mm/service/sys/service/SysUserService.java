package com.mm.service.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.util.PageUtil;
import com.mm.service.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author lwl
 */
public interface SysUserService extends IService<SysUserEntity> {

    PageUtil queryPage(Map<String, Object> params);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

}
