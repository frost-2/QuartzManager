package com.frost2.quartz.service.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author 陈伟平
 * @date 2020-09-30 4:11:00
 */
public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取我们设置的定时任务名称
        String name = context.getJobDetail().getKey().getName();
        System.out.println("name = " + name);
    }
}
