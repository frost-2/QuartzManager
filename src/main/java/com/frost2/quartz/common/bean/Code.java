package com.frost2.quartz.common.bean;

import lombok.Getter;
import lombok.ToString;

/**
 * @author 陈伟平
 * @date 2020-9-23 17:31:56
 */
@ToString
@Getter
public enum Code {

    PARAM_FORMAT_ERROR(2004, "参数异常"),
    DELETE_NULL(4002, "数据删除为空"),
    DELETE_FAIL(4001, "数据删除失败"),
    DELETE_SUCCESS(4000, "数据删除成功"),
    UPDATE_NULL(5001, "更新数据为空"),
    UPDATE_SUCCESS(5000, "更新成功"),
    ADD_FAIL(5002, "数据添加失败"),
    ADD_SUCCESS(5000, "数据添加成功"),
    QUERY_NULL(2008, "数据查询为空"),
    QUERY_FAIL(2009, "数据查询失败"),
    CHECK_FAIL(6001, "接口检验失败"),
    EXECUTION_ERROR(6002, "接口执行失败"),
    QUERY_SUCCESS(2000, "数据查询成功"),
    CHANGE_LOCK(8000, "改变设备锁状态"),
    CONNECTION_ERROR(8004, "服务器连接异常"),
    SUCC(9000, "定时任务执行成功"),
    FAIL(9001, "定时任务执行失败");

    //枚举的属性字段必须是私有且不可变
    private final int code;
    private final String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
