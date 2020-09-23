package com.frost2.quartz.common.bean;

import lombok.*;

/**
 * @author 陈伟平
 * @date 2019-10-12-下午 3:47
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Result<E> {

    private Integer row;
    private E list;

}
