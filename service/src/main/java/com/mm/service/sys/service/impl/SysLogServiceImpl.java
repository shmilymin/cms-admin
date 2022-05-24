package com.mm.service.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.util.PageUtil;
import com.mm.common.util.Query;
import com.mm.service.sys.dao.SysLogDao;
import com.mm.service.sys.entity.SysLogEntity;
import com.mm.service.sys.service.SysLogService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 系统日志
 *
 * @author lwl
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SysLogEntity> qw = new LambdaQueryWrapper<>();
        qw.like(ObjectUtil.isNotNull(params.get("key")), SysLogEntity::getUsername, params.get("key"));
        IPage<SysLogEntity> page = this.page(new Query<SysLogEntity>().getPage(params), qw);
        return new PageUtil(page);
    }
}
