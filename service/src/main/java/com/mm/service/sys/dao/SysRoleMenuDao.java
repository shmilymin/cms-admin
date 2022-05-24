package com.mm.service.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.service.sys.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与菜单对应关系
 *
 * @author lwl
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {

}
