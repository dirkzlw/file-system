package com.zlw.fsys.exception;

import java.io.IOException;

/**
 * @author Ranger
 * @create 2019-08-25 10:31
 */
public class CreateFileException extends IOException {
    @Override
    public String getMessage() {
        return "创建文件失败";
    }
}
