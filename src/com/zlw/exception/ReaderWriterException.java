package com.zlw.exception;

import java.io.IOException;

/**
 * @author Ranger
 * @create 2019-08-27 10:35
 */
public class ReaderWriterException extends IOException {
    @Override
    public String getMessage() {
        return "读写流失败";
    }
}
