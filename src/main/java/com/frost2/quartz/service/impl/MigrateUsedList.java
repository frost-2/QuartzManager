package com.frost2.quartz.service.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:28:29
 */
public class MigrateUsedList implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("定时迁移历史订单");
    }
}
