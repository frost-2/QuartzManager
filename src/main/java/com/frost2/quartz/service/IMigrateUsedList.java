package com.frost2.quartz.service;


import com.frost2.quartz.common.bean.ResultList;

/**
 * @author 陈伟平
 * @date 2020-9-19 15:34:56
 */
public interface IMigrateUsedList {

    ResultList migrateUsedList(String jobName, String triggerName, String cron);
}
