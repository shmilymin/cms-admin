package com.mm.service.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.service.job.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author lwl
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {

}
