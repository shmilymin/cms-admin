package com.mm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.util.PageUtil;
import com.mm.modules.sys.entity.SysRoleEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;


/**
 * 角色
 *
 * @author lwl
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtil queryPage(Map<String, Object> params);

    boolean removeByIdList(Collection<? extends Serializable> roleIds);
}
