package com.mm.gen.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数据库接口
 *
 * @author lwl
 */
@Mapper
public interface GeneratorDao {

    /**
     * 查询表信息
     *
     * @param tableName
     * @return
     */
    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime " +
            " from information_schema.tables where table_schema = (select database()) and table_name = #{tableName} ")
    Map<String, String> queryTable(String tableName);

    /**
     * 获取表的列信息
     *
     * @param tableName
     * @return
     */
    @Select("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra " +
            " from information_schema.columns where table_name = #{tableName} and table_schema = (select database()) " +
            " order by ordinal_position")
    List<Map<String, String>> queryColumns(String tableName);
}
