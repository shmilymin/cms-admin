package com.mm.gen.service;

import com.mm.gen.dao.GeneratorDao;
import com.mm.gen.utils.GenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author lwl
 */
@Service
public class GeneratorService {

    @Resource
    private GeneratorDao generatorDao;

    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorDao.queryColumns(tableName);
    }

    public void generatorCode(String[] tableNames, String mainPath, String packageName, String moduleName, String author) {
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GenUtil.generatorCode(table, columns, mainPath, packageName, moduleName, author);
        }
    }
}
