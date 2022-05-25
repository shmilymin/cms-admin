package com.mm.service.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.service.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户
 *
 * @author lwl
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    /**
     * 查询用户的所有权限
     *
     * @param userId
     * @return
     */
    List<String> queryAllPerms(Long userId);

}
