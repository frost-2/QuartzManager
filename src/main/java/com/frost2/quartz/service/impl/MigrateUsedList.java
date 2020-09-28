package com.frost2.quartz.service.impl;

import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import com.frost2.quartz.common.bean.Constant;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.SQLException;
import java.util.List;


/**
 * @author 陈伟平
 * @date 2020-9-19 09:28:29
 */
public class MigrateUsedList implements Job {

    @Override
    public void execute(JobExecutionContext context) {
//        //也可以在创建JobDetail时使用usingJobData("migrateDayNum","90"),将迁移的天数传递进来。
//        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
//        Object migrateDayNum = mergedJobDataMap.get("migrateDayNum");

        List<Entity> list;
        try {
            DbUtil.setCaseInsensitiveGlobal(false);
            String sql = "select * from usedList where date_add(EndTime, interval " + Constant.MIGRATE_DAY_NUM + " day) < now()";
            Db testDb1 = Db.use("test_db1");
            list = testDb1.query(sql);
            if (null != list && !list.isEmpty()) {
                list.forEach(entity -> {
                    try {
                        testDb1.del(
                                Entity.create("usedList").set("UsedInCrement", entity.get("UsedInCrement"))
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    entity.setTableName("usedList_pastDue");
                });
                Db.use("test_db2").insert(list);
            } else {
                System.out.println("查询数据为空");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
