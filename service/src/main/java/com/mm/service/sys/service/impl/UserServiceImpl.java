package com.mm.service.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mm.service.sys.entity.SysUserEntity;
import com.mm.service.sys.service.SysMenuService;
import com.mm.service.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户
 *
 * @author lwl
 */
@Service("userService")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<SysUserEntity> users = sysUserService.list(Wrappers.<SysUserEntity>lambdaQuery()
                .eq(SysUserEntity::getUsername, s));
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        SysUserEntity user = users.get(0);
        List<String> permsList;
        if (user.getId() == 1) {
            permsList = sysMenuService.list().stream().filter(e -> StrUtil.isNotBlank(e.getPerms()))
                    .map(e -> e.getPerms()).collect(Collectors.toList());
        } else {
            permsList = sysMenuService.getPermsByUserId(user.getId());
        }
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            permsSet.addAll(Arrays.asList(perms.split(",")));
        }
        return new User(user.getUsername(), user.getPassword(), user.getStatus(), true,
                true, true,
                permsSet.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList()));
    }
}
