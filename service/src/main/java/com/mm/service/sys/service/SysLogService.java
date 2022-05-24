package com.mm.service.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.common.util.PageUtil;
import com.mm.service.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 * @author lwl
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtil queryPage(Map<String, Object> params);

}
