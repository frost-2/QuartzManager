package com.frost2.quartz.controller;

import com.frost2.quartz.common.bean.ResultList;
import com.frost2.quartz.service.IMigrateUsedList;
import com.frost2.quartz.service.ITimedTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:20:32
 */
@RestController
@Api(tags = "定时任务接口")
@RequestMapping(value = "timed")
public class TimedTaskController {

    @Autowired
    ITimedTaskService timedTaskServiceImpl;
    @Autowired
    IMigrateUsedList migrateUsedListImpl;

    @ApiOperation(value = "迁移历史订单数据", httpMethod = "POST")
    @PostMapping(value = "/migrateUsedList")
    public ResultList migrateUsedList(@RequestParam String jobName,
                                      @RequestParam String triggerName,
                                      @RequestParam String cron) {
        return migrateUsedListImpl.migrateUsedList(jobName, triggerName, cron);
    }

    @ApiOperation(value = "查询所有定时任务", httpMethod = "GET")
    @GetMapping(value = "/job")
    public ResultList queryAllTask() {
        return timedTaskServiceImpl.queryAllTask();
    }

    @ApiOperation(value = "查询定时任务", httpMethod = "GET")
    @GetMapping(value = "/job/{jobName}/{triggerName}")
    public ResultList queryTimedTask(@PathVariable String jobName, @PathVariable String triggerName) {
        return timedTaskServiceImpl.queryTimedTask(jobName, triggerName);
    }

    @ApiOperation(value = "查询最近5次执行时间", httpMethod = "GET")
    @GetMapping(value = "/job/{cron}")
    public ResultList queryAllTask(@PathVariable String cron) {
        return timedTaskServiceImpl.getRecentTriggerTime(cron);
    }

    @ApiOperation(value = "删除定时任务", httpMethod = "DELETE")
    @DeleteMapping(value = "/job/{jobName}/{triggerName}")
    public ResultList delTimedTask(@PathVariable String jobName, @PathVariable String triggerName) {
        return timedTaskServiceImpl.delTimedTask(jobName, triggerName);
    }

    @ApiOperation(value = "修改定时任务执行时间", httpMethod = "POST")
    @PostMapping(value = "/job/{jobName}/{triggerName}/{cron}")
    public ResultList rescheduleJob(@PathVariable String jobName, @PathVariable String triggerName, @PathVariable String cron) {
        return timedTaskServiceImpl.rescheduleJob(jobName, triggerName, cron);
    }


}
