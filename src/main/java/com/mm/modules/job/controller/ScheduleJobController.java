package com.mm.modules.job.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.mm.common.annotation.SysLog;
import com.mm.common.utils.PageUtil;
import com.mm.modules.job.entity.ScheduleJobEntity;
import com.mm.modules.job.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 定时任务
 *
 * @author lwl
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 定时任务列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:schedule:list')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return scheduleJobService.queryPage(params);
    }

    /**
     * 保存修改定时任务
     */
    @SysLog("保存修改定时任务")
    @RequestMapping("/save_or_update")
    @PreAuthorize("hasAnyAuthority('sys:schedule:save', 'sys:schedule:update')")
    public R saveOrUpdate(@RequestBody ScheduleJobEntity scheduleJob) {
        scheduleJobService.saveOrUpdate(scheduleJob);
        return R.ok(null);
    }

    /**
     * 删除定时任务
     */
    @SysLog("删除定时任务")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('sys:schedule:delete')")
    public R delete(@RequestBody Long[] jobIds) {
        log.debug("delete jobIds:{}", Arrays.asList(jobIds));
        scheduleJobService.deleteBatch(jobIds);
        return R.ok(null);
    }

    /**
     * 立即执行任务
     */
    @SysLog("立即执行任务")
    @RequestMapping("/run")
    @PreAuthorize("hasAnyAuthority('sys:schedule:run')")
    public R run(@RequestBody Long[] jobIds) {
        log.debug("run jobIds:{}", Arrays.asList(jobIds));
        scheduleJobService.run(jobIds);
        return R.ok(null);
    }

    /**
     * 暂停定时任务
     */
    @SysLog("暂停定时任务")
    @RequestMapping("/pause")
    @PreAuthorize("hasAnyAuthority('sys:schedule:pause')")
    public R pause(@RequestBody Long[] jobIds) {
        log.debug("pause jobIds:{}", Arrays.asList(jobIds));
        scheduleJobService.pause(jobIds);

        return R.ok(null);
    }

    /**
     * 恢复定时任务
     */
    @SysLog("恢复定时任务")
    @RequestMapping("/resume")
    @PreAuthorize("hasAnyAuthority('sys:schedule:resume')")
    public R resume(@RequestBody Long[] jobIds) {
        log.debug("resume jobIds:{}", Arrays.asList(jobIds));
        scheduleJobService.resume(jobIds);

        return R.ok(null);
    }

}
