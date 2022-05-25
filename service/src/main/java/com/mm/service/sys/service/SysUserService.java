package com.mm.service.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.util.PageUtil;
import com.mm.service.sys.entity.SysUserEntity;

import java.util.Map;


/**
 * 系统用户
 *
 * @author lwl
 */
public interface SysUserService extends IService<SysUserEntity> {

    PageUtil queryPage(Map<String, Object> params);

}
