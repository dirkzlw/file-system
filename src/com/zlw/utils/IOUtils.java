package com.zlw.utils;

import com.zlw.exception.CloseIOException;

import java.io.Closeable;
import java.io.IOException;

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
