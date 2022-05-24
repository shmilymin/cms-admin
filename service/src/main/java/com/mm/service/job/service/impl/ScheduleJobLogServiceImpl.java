package com.mm.service.job.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.util.PageUtil;
import com.mm.common.util.Query;
import com.mm.service.job.dao.ScheduleJobLogDao;
import com.mm.service.job.entity.ScheduleJobLogEntity;
import com.mm.service.job.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author lwl
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<ScheduleJobLogEntity> qw = new LambdaQueryWrapper<>();
        qw.like(ObjectUtil.isNotNull(params.get("jobId")), ScheduleJobLogEntity::getJobId, params.get("jobId"));
        qw.orderByDesc(ScheduleJobLogEntity::getCreateTime);
        IPage<ScheduleJobLogEntity> page = this.page(new Query<ScheduleJobLogEntity>().getPage(params), qw);
        return new PageUtil(page);
    }

}
