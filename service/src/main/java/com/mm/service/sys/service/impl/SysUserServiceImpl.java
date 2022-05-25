package com.mm.service.sys.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.util.Constant;
import com.mm.common.util.PageUtil;
import com.mm.common.util.Query;
import com.mm.service.sys.dao.SysUserDao;
import com.mm.service.sys.entity.SysUserEntity;
import com.mm.service.sys.entity.SysUserRoleEntity;
import com.mm.service.sys.service.SysUserRoleService;
import com.mm.service.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统用户
 *
 * @author lwl
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    final SysUserRoleService sysUserRoleService;

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");

        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StrUtil.isNotBlank(username), "username", username)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );
        for (SysUserEntity su : page.getRecords()) {
            su.setPassword(null);
            //获取用户所属的角色列表
            List<SysUserRoleEntity> urs = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery()
                    .select(SysUserRoleEntity::getRoleId)
                    .eq(SysUserRoleEntity::getUserId, su.getId()));
            if (!urs.isEmpty()) {
                su.setRoleIdList(urs.stream().map(e -> e.getRoleId()).collect(Collectors.toList()));
            }
        }
        return new PageUtil(page);
    }

}
