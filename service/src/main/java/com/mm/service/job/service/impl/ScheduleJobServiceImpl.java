package com.mm.service.job.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.common.util.Constant;
import com.mm.common.util.PageUtil;
import com.mm.common.util.Query;
import com.mm.service.job.dao.ScheduleJobDao;
import com.mm.service.job.entity.ScheduleJobEntity;
import com.mm.service.job.service.ScheduleJobService;
import com.mm.service.job.util.ScheduleUtil;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 定时任务
 *
 * @author lwl
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {

    @Resource
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<ScheduleJobEntity> scheduleJobList = this.list();
        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler, scheduleJob.getId());
            //如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtil.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtil.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String beanName = (String) params.get("beanName");
        LambdaQueryWrapper<ScheduleJobEntity> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(beanName), ScheduleJobEntity::getBeanName, beanName);
        IPage<ScheduleJobEntity> page = this.page(new Query<ScheduleJobEntity>().getPage(params), qw);
        return new PageUtil(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(ScheduleJobEntity entity) {
        if (entity.getId() == null) {
            entity.setCreateTime(new Date());
            entity.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
            this.save(entity);
            ScheduleUtil.createScheduleJob(scheduler, entity);
        } else {
            ScheduleUtil.updateScheduleJob(scheduler, entity);
            this.updateById(entity);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.deleteScheduleJob(scheduler, jobId);
        }
        //删除数据
        this.removeByIds(Arrays.asList(jobIds));
    }

    private void updateBatch(Long[] jobIds, int status) {
        List<ScheduleJobEntity> list = new ArrayList<>();
        for (Long jobId : jobIds) {
            ScheduleJobEntity job = new ScheduleJobEntity();
            job.setId(jobId);
            job.setStatus(status);
            list.add(job);
        }
        if (!list.isEmpty()) {
            this.updateBatchById(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.run(scheduler, this.getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.pauseJob(scheduler, jobId);
        }

        updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.resumeJob(scheduler, jobId);
        }

        updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
    }

}
