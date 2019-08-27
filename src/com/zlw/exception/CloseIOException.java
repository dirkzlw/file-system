package com.zlw.exception;

import java.io.IOException;

/**
 * @author Ranger
 * @create 2019-08-25 10:31
 */
public class CloseIOException extends IOException {
    @Override
    public String getMessage() {
        return "关闭流失败";
    }
}
