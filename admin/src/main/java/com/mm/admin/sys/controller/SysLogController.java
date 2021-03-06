package com.mm.admin.sys.controller;

import com.mm.common.util.PageUtil;
import com.mm.service.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 系统日志
 *
 * @author lwl
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:log:list')")
    public PageUtil list(@RequestParam Map<String, Object> params) {
        return sysLogService.queryPage(params);
    }

}
