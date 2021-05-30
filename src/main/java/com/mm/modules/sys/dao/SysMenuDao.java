package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单管理
 *
 * @author lwl
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

    @Select("SELECT m.perms FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.`id` = rm.`menu_id` " +
            "LEFT JOIN sys_user_role ur ON rm.`role_id` = ur.`role_id` " +
            "WHERE ur.`user_id` = #{userId} AND LENGTH(m.perms) > 0")
    List<String> getPermsByUserId(@Param("userId") Long userId);

}
