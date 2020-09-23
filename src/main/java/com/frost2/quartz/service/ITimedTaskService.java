package com.frost2.quartz.service;


import com.frost2.quartz.common.bean.ResultList;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:34:54
 */
public interface ITimedTaskService {

    ResultList queryTimedTask(String jobName, String triggerName);

    ResultList delTimedTask(String jobName, String triggerName);

    ResultList queryAllTask();

    ResultList getRecentTriggerTime(String cron);

    ResultList rescheduleJob(String jobName, String triggerName, String cron);
}
