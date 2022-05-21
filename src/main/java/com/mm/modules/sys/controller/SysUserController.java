package com.mm.modules.sys.controller;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.mm.common.annotation.SysLog;
import com.mm.common.util.Assert;
import com.mm.common.util.PageUtil;
import com.mm.common.util.R;
import com.mm.common.util.SecurityUtil;
import com.mm.modules.sys.entity.SysUserEntity;
import com.mm.modules.sys.service.SysUserRoleService;
import com.mm.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Map;

/**
 * 系统用户
 *
 * @author lwl
 */
@Validated
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return sysUserService.queryPage(params);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok(getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(@NotBlank(message = "原密码不为能空") String password,
                      @NotBlank(message = "新密码不为能空") String newPassword) {
        SysUserEntity user = getUser();
        if (!SecurityUtil.matchesPwd(password, user.getPassword())) {
            return R.error("原密码不正确");
        }
        SysUserEntity uu = new SysUserEntity();
        uu.setId(user.getId());
        uu.setPassword(SecurityUtil.encodePwd(newPassword));
        if (sysUserService.updateById(uu)) {
            return R.ok();
        }
        return R.error("修改失败");
    }

    /**
     * 保存修改用户
     */
    @SysLog("保存修改用户")
    @RequestMapping("/save_or_update")
    @PreAuthorize("hasAnyAuthority('sys:user:save','sys:user:update')")
    public R save(@RequestBody SysUserEntity entity) {
        if (entity.getId() == null) {
            Assert.isBlank(entity.getPassword(), "密码不能为空");
            entity.setPassword(SecurityUtil.encodePwd(entity.getPassword()));
        } else {
            if (StrUtil.isBlank(entity.getPassword())) {
                entity.setPassword(null);
            } else {
                entity.setPassword(SecurityUtil.encodePwd(entity.getPassword()));
            }
        }
        if (sysUserService.saveOrUpdate(entity)) {
            //保存用户与角色关系
            sysUserRoleService.saveOrUpdate(entity.getId(), entity.getRoleIdList());
        }
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
    public R delete(@RequestBody Long[] ids) {
        if (ArrayUtil.contains(ids, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtil.contains(ids, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
