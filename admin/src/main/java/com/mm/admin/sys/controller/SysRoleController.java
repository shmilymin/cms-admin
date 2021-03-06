package com.mm.admin.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mm.admin.common.annotation.SysLog;
import com.mm.common.util.PageUtil;
import com.mm.common.util.R;
import com.mm.service.sys.entity.SysMenuEntity;
import com.mm.service.sys.entity.SysRoleEntity;
import com.mm.service.sys.entity.SysRoleMenuEntity;
import com.mm.service.sys.service.SysMenuService;
import com.mm.service.sys.service.SysRoleMenuService;
import com.mm.service.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 角色管理
 *
 * @author lwl
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 全部角色列表
     */
    @RequestMapping("/all")
    public R all() {
        return R.ok(sysRoleService.list());
    }

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return sysRoleService.queryPage(params);
    }

    /**
     * 获取角色菜单
     */
    @RequestMapping("/select")
    public R select(Long roleId) {
        List<SysRoleMenuEntity> rms = new ArrayList<>();
        if (roleId != null) {
            rms = sysRoleMenuService.list(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                    .select(SysRoleMenuEntity::getMenuId)
                    .eq(SysRoleMenuEntity::getRoleId, roleId));
        }
        // 查询目录
        List<Map<String, Object>> list = getMenu(0L, rms);
        return R.ok(list);
    }

    private List<Map<String, Object>> getMenu(Long pid, List<SysRoleMenuEntity> rms) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SysMenuEntity> ms = sysMenuService.list(Wrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getPid, pid)
                .orderByAsc(SysMenuEntity::getOrderNum));
        for (SysMenuEntity m : ms) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("title", m.getName());
            List<Map<String, Object>> menus = getMenu(m.getId(), rms);
            if (!menus.isEmpty()) {
                map.put("children", menus);
                // 是否初始展开
                map.put("spread", true);
            }
            // 是否选中
            if (map.get("children") == null && !rms.isEmpty()) {
                for (SysRoleMenuEntity rm : rms) {
                    if (m.getId().equals(rm.getMenuId())) {
                        map.put("checked", true);
                    }
                }
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @RequestMapping("/save_or_update")
    @PreAuthorize("hasAnyAuthority('sys:role:save', 'sys:role:update')")
    public R saveOrUpdate(@RequestBody SysRoleEntity role) {
        sysRoleService.saveOrUpdate(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('sys:role:delete')")
    public R delete(@RequestBody Long[] roleIds) {
        sysRoleService.removeByIdList(Arrays.asList(roleIds));
        return R.ok();
    }
}
