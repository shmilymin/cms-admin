package com.mm.admin.sys.controller;

import com.mm.admin.common.util.SecurityUtil;
import com.mm.service.sys.entity.SysUserEntity;

/**
 * Controller公共组件
 *
 * @author lwl
 */
public abstract class AbstractController {

    protected SysUserEntity getUser() {
        return SecurityUtil.getUserEntity();
    }

    protected Long getUserId() {
        return getUser().getId();
    }

}
