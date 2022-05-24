package com.mm.service.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.util.Constant;
import com.mm.service.sys.dao.SysMenuDao;
import com.mm.service.sys.entity.SysMenuEntity;
import com.mm.service.sys.entity.SysRoleMenuEntity;
import com.mm.service.sys.entity.SysUserRoleEntity;
import com.mm.service.sys.service.SysMenuService;
import com.mm.service.sys.service.SysRoleMenuService;
import com.mm.service.sys.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 菜单
 *
 * @author lwl
 */
@Service("sysMenuService")
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    final SysUserRoleService sysUserRoleService;
    final SysRoleMenuService sysRoleMenuService;

    @Override
    public List<String> getPermsByUserId(Long userId) {
        return baseMapper.getPermsByUserId(userId);
    }


    @Override
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        // 获取全部目录和菜单菜单
        List<SysMenuEntity> ms = list(Wrappers.<SysMenuEntity>lambdaQuery().in(SysMenuEntity::getType,
                        Arrays.asList(Constant.MenuType.CATALOG.getValue(), Constant.MenuType.MENU.getValue()))
                .orderByDesc(SysMenuEntity::getOrderNum));
        // 获取父菜单
        List<SysMenuEntity> pms = new ArrayList<>();
        if (userId == Constant.SUPER_ADMIN) {
            // 获取全部父菜单
            pms = ms.stream().filter(e -> Objects.equals(0L, e.getPid())).collect(Collectors.toList());
        } else {
            // 获取用户角色
            List<SysUserRoleEntity> urs = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery()
                    .eq(SysUserRoleEntity::getUserId, userId));
            if (urs.isEmpty()) {
                return pms;
            }
            List<Long> roleIds = urs.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
            // 获取用户菜单
            List<SysRoleMenuEntity> rms = sysRoleMenuService.list(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                    .in(SysRoleMenuEntity::getRoleId, roleIds));
            if (rms.isEmpty()) {
                return pms;
            }
            List<Long> menuIds = rms.stream().map(e -> e.getMenuId()).distinct().collect(Collectors.toList());
            // 获取用户父菜单
            pms = ms.stream().filter(e -> Objects.equals(0L, e.getPid()) && menuIds.contains(e.getId()))
                    .collect(Collectors.toList());
            // 获取用户子菜单
            ms = ms.stream().filter(e -> menuIds.contains(e.getId())).collect(Collectors.toList());
        }
        // 获取子菜单
        for (SysMenuEntity pm : pms) {
            pm.setChild(getMenu(pm, ms));
        }
        return pms;
    }

    /**
     * 递归获取菜单
     *
     * @param pm
     * @param ms
     * @return
     */
    private List<SysMenuEntity> getMenu(SysMenuEntity pm, List<SysMenuEntity> ms) {
        List<SysMenuEntity> lms = ms.stream().filter(e -> e.getPid().equals(pm.getId())).collect(Collectors.toList());
        if (lms.isEmpty()) {
            return new ArrayList<>();
        }
        for (SysMenuEntity lm : lms) {
            lm.setChild(getMenu(lm, ms));
        }
        return lms;
    }

    @Override
    public void delete(Long menuId) {
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        sysRoleMenuService.remove(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                .eq(SysRoleMenuEntity::getMenuId, menuId));
    }

}
