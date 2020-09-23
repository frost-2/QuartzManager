package com.frost2.quartz.common.util;

import com.frost2.quartz.common.bean.Code;
import com.frost2.quartz.common.customException.CustomException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 陈伟平
 * @date 2020-9-17 15:38:43
 */
public class QuartzUtil {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private static Scheduler scheduler;

    static {
        try {
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
    }

    /**
     * 传入:任务名称、触发器名称、任务描述、要执行的任务类、cron表达式创建定时任务，
     * 返回是否创建成功
     * <p>
     * 注：
     * 在创建任务时未设置jobGroup和triggerGroup,Job创建后其均为默认值:DEFAULT,
     * 因此新创建的任务的jobName和triggerName,均不能与之前任务的重复.
     *
     * @param jobName     任务名
     * @param triggerName 触发器名
     * @param description 对该任务的秒数(非必须)
     * @param jobClass    要执行的任务
     * @param cron        cron表达式
     * @return true:创建Job成功,false:创建Job失败
     */
    public static <T extends Job> boolean addJob(String jobName, String triggerName, String description,
                                                 Class<T> jobClass, String cron) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName)
                    .withDescription(description)
//                    .usingJobData("param1", "将参数")
//                    .usingJobData("param2", "传递到JOB任务中使用")
                    .build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            return scheduler.isStarted();
        } catch (Exception e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @param cron        cron表达式
     * @return true:修改Job成功,false:修改Job失败
     */
    public static Boolean rescheduleJob(String jobName, String triggerName, String cron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return false;
            }

            checkJobNameAndTriggerName(jobName, trigger);

            Date latestFireTime = new Date();
            if (!trigger.getCronExpression().equalsIgnoreCase(cron)) {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName)
                        .startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .build();
                //rescheduleJob()执行成功返回最近一次执行的时间，如果失败返回null
                latestFireTime = scheduler.rescheduleJob(triggerKey, trigger);
            }
            return null != latestFireTime;
        } catch (SchedulerException e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
    }

    /**
     * 根据jobName和triggerName删除该JOB
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return true:删除Job成功,false:删除Job失败
     */
    public static boolean removeJob(String jobName, String triggerName) {
        boolean flag;
        try {

            Trigger trigger = scheduler.getTrigger(new TriggerKey(triggerName));
            if (null == trigger) {
                return false;
            }

            checkJobNameAndTriggerName(jobName, trigger);

            TriggerKey triggerKey = trigger.getKey();
            scheduler.pauseTrigger(triggerKey);
            flag = scheduler.unscheduleJob(triggerKey);

            //删除trigger之后无需在删除job,因为相关的job如果不是持久的,则将被自动删除。下面这种写法flag=false
//            if (flag) {
//                flag = scheduler.deleteJob(JobKey.jobKey(jobName));
//            }
        } catch (SchedulerException e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
        return flag;
    }

    /**
     * 根据jobName和triggerName查询该JOB
     *
     * @param jobName     任务名称
     * @param triggerName 触发器名
     * @return 该Job相关信息[详见getJobInfo方法]
     */
    public static HashMap<String, String> getJob(String jobName, String triggerName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobName));
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(triggerName));

            if (null == jobDetail || null == trigger) {
                return new HashMap<>();
            }

            checkJobNameAndTriggerName(jobName, trigger);

            return getJobInfo(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
    }

    /**
     * 查询所有正在执行的JOB
     *
     * @return 该Job相关信息[详见getJobInfo方法]
     */
    public static List<HashMap<String, String>> getJobs() {
        List<HashMap<String, String>> list = new ArrayList<>();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {

                GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    //获取CronTrigger
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //获取trigger对应的JobDetail
                    JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());

                    list.add(getJobInfo(jobDetail, trigger));
                }
            }
        } catch (SchedulerException e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
        return list;
    }

    /**
     * @param cron cron表达式
     * @return 最近5次的执行时间
     */
    public static List<String> getRecentTriggerTime(String cron) {
        List<String> list = new ArrayList<>();
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            List<Date> dateList = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 5);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateList.forEach(date -> list.add(dateFormat.format(date)));
        } catch (ParseException e) {
            throw new CustomException(Code.EXECUTION_ERROR);
        }
        return list;
    }

    /**
     * 校验cron表达式是否正确
     *
     * @param cronExpression cron表达式
     * @return true:正确,false:不正确
     */
    @SuppressWarnings("all")
    public static boolean checkCronExpression(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * 获取该Job对应的相关信息
     */
    private static HashMap<String, String> getJobInfo(JobDetail jobDetail, CronTrigger trigger) {
        HashMap<String, String> map = new HashMap<>();
        map.put("jobName", jobDetail.getKey().getName());
        map.put("jobGroup", jobDetail.getKey().getGroup());
        map.put("corn", trigger.getCronExpression());
        map.put("triggerName", trigger.getKey().getName());
        map.put("description", jobDetail.getDescription());
        return map;
    }

    /**
     * 校验jobName和triggerName是否匹配
     * 如不匹配抛出自定义异常
     */
    private static void checkJobNameAndTriggerName(String jobName, Trigger trigger) {
        String name = trigger.getJobKey().getName();
        if (!name.equals(jobName)) {
            throw new CustomException(Code.PARAM_FORMAT_ERROR.getCode(), "jobName与triggerName不匹配");
        }
    }

}
