package com.zlw.fsys.utils;

import javax.swing.*;

/**
 * @author Ranger
 * @create 2019-08-25 9:10
 */
public class ZfaceUtils {

    public static void addRootTextFirst (JTextArea textIn){
        textIn.append(Variates.dirStr);
    }

    public static void addRootText (JTextArea textIn){
        textIn.append("\n"+Variates.dirStr);
    }

}
