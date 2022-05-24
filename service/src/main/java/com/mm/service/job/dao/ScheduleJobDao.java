package com.mm.service.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.service.job.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 *
 * @author lwl
 */
@Mapper
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {
}
