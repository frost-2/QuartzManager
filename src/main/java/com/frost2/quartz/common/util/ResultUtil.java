package com.frost2.quartz.common.util;

import com.frost2.quartz.common.bean.Code;
import com.frost2.quartz.common.bean.Result;
import com.frost2.quartz.common.bean.ResultList;

import java.util.List;
import java.util.Map;

/**
 * @author 陈伟平
 * @date 2020-03-30-下午 3:21
 */
public class ResultUtil {

    /**
     * 增加行数如果为null或者0则表示删除失败
     *
     * @param row 行数
     */
    public static ResultList verifyDelete(Integer row) {
        if (null == row || row == 0) {
            return new ResultList(Code.DELETE_NULL.getCode(), Code.DELETE_NULL.getMsg(), null);
        } else {
            return new ResultList(Code.DELETE_SUCCESS.getCode(), Code.DELETE_SUCCESS.getMsg(), null);
        }
    }

    /**
     * 根据flag判断是否删除成功
     *
     * @param flag true:删除成功,false:删除失败
     */
    public static ResultList verifyDelete(boolean flag) {
        if (flag) {
            return new ResultList(Code.DELETE_SUCCESS.getCode(), Code.DELETE_SUCCESS.getMsg(), null);
        } else {
            return new ResultList(Code.DELETE_FAIL.getCode(), Code.DELETE_FAIL.getMsg(), null);
        }
    }

    /**
     * 根据flag判断是否更新成功
     *
     * @param flag true:更新成功,false:更新失败
     */
    public static ResultList verifyUpdate(boolean flag) {
        if (flag) {
            return new ResultList(Code.UPDATE_SUCCESS.getCode(), Code.UPDATE_SUCCESS.getMsg(), null);
        } else {
            return new ResultList(Code.UPDATE_NULL.getCode(), Code.UPDATE_NULL.getMsg(), null);
        }
    }

    /**
     * 更新行数如果为null或者0则表示更新失败
     *
     * @param row 行数
     */
    public static ResultList verifyUpdate(Integer row) {
        if (null == row || row == 0) {
            return new ResultList(Code.UPDATE_NULL.getCode(), Code.UPDATE_NULL.getMsg(), null);
        } else {
            return new ResultList(Code.UPDATE_SUCCESS.getCode(), Code.UPDATE_SUCCESS.getMsg(), null);
        }
    }

    /**
     * 添加行数如果为null或者0则表示更新失败
     *
     * @param row 行数
     */
    public static ResultList verifyAdd(Integer row) {
        if (null == row || row == 0) {
            return new ResultList(Code.ADD_FAIL.getCode(), Code.ADD_FAIL.getMsg(), null);
        } else {
            return new ResultList(Code.ADD_SUCCESS.getCode(), Code.ADD_SUCCESS.getMsg(), null);
        }
    }

    /**
     * 更新行数如果为null或者0则表示更新失败
     *
     * @param row 行数
     */
    public static ResultList verifyQuery(List<?> list, Integer row) {
        if (null == list || list.isEmpty()) {
            return new ResultList(Code.QUERY_NULL.getCode(), Code.QUERY_NULL.getMsg(), null);
        } else {
            if (null == row || row == 0) {
                return new ResultList(Code.EXECUTION_ERROR.getCode(), Code.EXECUTION_ERROR.getMsg(), null);
            } else {
                return new ResultList(Code.QUERY_SUCCESS.getCode(), Code.QUERY_SUCCESS.getMsg(), new Result<>(row, list));
            }
        }
    }

    /**
     * @param map 返回数据
     */
    public static ResultList verifyQuery(Map map) {
        if (null == map || map.isEmpty()) {
            return new ResultList(Code.QUERY_NULL.getCode(), Code.QUERY_NULL.getMsg(), null);
        } else {
            return new ResultList(Code.QUERY_SUCCESS.getCode(), Code.QUERY_SUCCESS.getMsg(), new Result<>(map.size(), map));
        }
    }

    /**
     * 返回code对应的响应码和响应信息
     *
     * @param code 响应码
     */
    public static ResultList execute(Code code) {
        return new ResultList(code.getCode(), code.getMsg(), null);
    }

    /**
     * 返回code对应的响应码和传递的响应信息
     *
     * @param code    响应码
     * @param respMsg 对应的响应信息
     */
    public static ResultList execute(Code code, String respMsg) {
        return new ResultList(code.getCode(), respMsg, null);
    }


}
