package com.frost2.quartz.common.customException;

import com.frost2.quartz.common.bean.Code;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 陈伟平
 * @date 2020-9-23 17:31:39
 */
@ToString
@Getter
@Setter
public class CustomException extends RuntimeException {

    public CustomException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CustomException(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    private int code;
    private String msg;
}
