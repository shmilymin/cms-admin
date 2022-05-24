package com.mm.service.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.service.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色管理
 *
 * @author lwl
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

}
