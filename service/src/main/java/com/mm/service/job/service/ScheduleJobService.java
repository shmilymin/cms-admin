package com.mm.service.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.util.PageUtil;
import com.mm.service.job.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * 定时任务
 *
 * @author lwl
 */
public interface ScheduleJobService extends IService<ScheduleJobEntity> {

    PageUtil queryPage(Map<String, Object> params);

    /**
     * 批量删除定时任务
     */
    void deleteBatch(Long[] jobIds);

    /**
     * 立即执行
     */
    void run(Long[] jobIds);

    /**
     * 暂停运行
     */
    void pause(Long[] jobIds);

    /**
     * 恢复运行
     */
    void resume(Long[] jobIds);
}
