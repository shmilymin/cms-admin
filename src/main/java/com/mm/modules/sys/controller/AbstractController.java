package com.mm.modules.sys.controller;

import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.common.util.SecurityUtil;

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
