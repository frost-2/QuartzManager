package com.frost2.quartz.common.bean;

import lombok.*;

/**
 * @author 陈伟平
 * @date 2019-09-03-下午 3:29
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResultList {

    private int code;
    private String msg;
    private Result result;

}
