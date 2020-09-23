package com.frost2.quartz.service.impl;

import com.frost2.quartz.common.bean.Code;
import com.frost2.quartz.common.bean.Result;
import com.frost2.quartz.common.bean.ResultList;
import com.frost2.quartz.common.util.QuartzUtil;
import com.frost2.quartz.common.util.ResultUtil;
import com.frost2.quartz.service.IMigrateUsedList;
import com.frost2.quartz.service.ITimedTaskService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author 陈伟平
 * @date 2020-9-19 09:24:07
 */
@Service
public class TimedTaskServiceImpl implements ITimedTaskService, IMigrateUsedList {

    /**
     * 定时将历史订单的数据迁移到备份数据库
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名称
     * @param cron        cron表达式
     */
    @Override
    public ResultList migrateUsedList(String jobName, String triggerName, String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return ResultUtil.execute(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        String description = "将90天以前的历史订单迁移到xiaoJi_bak";
        boolean isStart = QuartzUtil.addJob(jobName, triggerName, description, MigrateUsedList.class, cron);
        if (isStart) {
            return ResultUtil.execute(Code.SUCC);
        }
        return ResultUtil.execute(Code.FAIL);

    }

    /**
     * 查询正在运行的定时任务
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return JobDetail
     */
    @Override
    public ResultList queryTimedTask(String jobName, String triggerName) {
        HashMap<String, String> jobTask = QuartzUtil.getJob(jobName, triggerName);
        return ResultUtil.verifyQuery(jobTask);
    }

    /**
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return 是否删除成功
     */
    @Override
    public ResultList delTimedTask(String jobName, String triggerName) {
        boolean isDelJob = QuartzUtil.removeJob(jobName, triggerName);
        return ResultUtil.verifyDelete(isDelJob);
    }

    /**
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return 是否删除成功
     */
    @Override
    public ResultList rescheduleJob(String jobName, String triggerName,String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return ResultUtil.execute(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        boolean isModifyJob = QuartzUtil.rescheduleJob(jobName, triggerName,cron);
        return ResultUtil.verifyUpdate(isModifyJob);
    }

    /**
     * 查询所有正在运行的定时任务
     *
     * @return JobDetail
     */
    @Override
    public ResultList queryAllTask() {
        List<HashMap<String, String>> list = QuartzUtil.getJobs();
        return ResultUtil.verifyQuery(list, list.size());
    }

    /**
     * @param cron cron表达式
     * @return 最近5次的执行时间
     */
    @Override
    public ResultList getRecentTriggerTime(String cron) {
        if (!QuartzUtil.checkCronExpression(cron)) {
            return ResultUtil.execute(Code.PARAM_FORMAT_ERROR, "cron解析错误");
        }
        List<String> list = QuartzUtil.getRecentTriggerTime(cron);
        return new ResultList(Code.SUCC.getCode(), Code.SUCC.getMsg(), new Result<>(list.size(), list));
    }
}
