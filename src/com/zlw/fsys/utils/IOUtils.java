package com.zlw.fsys.utils;

import com.zlw.fsys.exception.CloseIOException;

import java.io.Closeable;

/**
 * @author Ranger
 * @create 2019-08-25 22:56
 */
public class IOUtils {

    public static void closeIO(Closeable ... io){
        for (Closeable c : io){
            try {
                if (c!=null){
                    c.close();
                }
            } catch (Exception e) {
                e = new CloseIOException();
                String message = e.getMessage();
                System.err.println(message);
            }
        }
    }
}
