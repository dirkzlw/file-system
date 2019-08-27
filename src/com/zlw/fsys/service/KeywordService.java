package com.zlw.fsys.service;

import com.zlw.fsys.exception.CreateFileException;
import com.zlw.fsys.exception.ReaderWriterException;
import com.zlw.fsys.utils.IOUtils;
import com.zlw.fsys.utils.Variates;
import com.zlw.fsys.utils.ZfaceUtils;
import com.zlw.fsys.view.ViFile;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ranger
 * @create 2019-08-25 9:08
 */
public class KeywordService {
    public static void dealWithKetword(JTextArea textIn, String command) {
        String[] cs = command.trim().split(" ");
        switch (cs[0]) {
            //输出时间
            case "date":
                if (cs.length == 1) {
                    textIn.append(new Date().toString());
                } else if (cs[1].length() > 1 && cs[1].substring(1).equals("f")) {
                    //格式化输出时间
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    textIn.append(sdf.format(date));
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //输出当前目录命令
            case "pwd":
                if (cs.length == 1) {
                    System.out.println("Variates.froot = " + Variates.froot);
                    textIn.append(Variates.froot.toString().replace("D:\\filedir", "/").replace("\\", "/").replace("//", "/"));
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //创建目录
            case "mkdir":
                if (cs.length == 2) {
                    for (String s : Variates.fne) {
                        if (cs[1].contains(s)) {
                            textIn.append("dir cann't contain /\\:*?\"<>|");
                        }
                    }
                    File newF = new File(Variates.froot + "\\" + cs[1]);
                    if (newF.exists()) {
                        textIn.append("the \'" + cs[1] + "\' exists");
                    } else {
                        try {
                            if (newF.mkdir()) {
                                textIn.append(newF.toString().replace("D:\\filedir\\", "/").replace("\\", "/") + " created successfully");
                            }
                        } catch (Exception e) {
                            e = new CreateFileException();
                            String message = e.getMessage();
                            System.err.println(message);
                        }
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //展示目录列表
            case "ll":
                if (cs.length == 1) {
                    for (String s : Variates.froot.list()) {
                        File f = new File(Variates.froot.toString() + "\\" + s);
                        if (f.isDirectory()) {
                            textIn.append("d--\t" + s + "\n");
                        } else {
                            textIn.append("f--\t" + s + "\n");
                        }
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootTextFirst(textIn);
                break;
            //切换目录
            case "cd":
                if (cs.length == 2) {
                    //返回主目录
                    if ("/".equals(cs[1])) {
                        System.out.println("返回主目录");
                        Variates.froot = new File("D:\\filedir");
                        Variates.dirStr = Variates.dirStr.replace(Variates.olddir, "/");
                        Variates.olddir = "/";
                        textIn.append("/\n");
                    } else if ("..".equals(cs[1])) {
                        //返回上级目录
                        if (!"D:\\filedir".equals(Variates.froot.toString())) {
                            System.out.println("Variates.froot.toString()" + Variates.froot.toString());
                            String[] dirs = Variates.froot.toString().replace("\\", "/").split("/");
                            String dpath = "D:";
                            for (int i = 1; i < dirs.length - 1; i++) {
                                dpath += "/" + dirs[i];
                            }
                            Variates.froot = new File(dpath);
                            System.out.println("dpath = " + dpath);
                            System.out.println("dirs[dirs.length-2] = " + dirs[dirs.length - 2]);
                            if ("filedir".equals(dirs[dirs.length - 2].toString())) {
                                System.out.println("true = " + true);
                                Variates.dirStr = Variates.dirStr.replace(Variates.olddir, "/");
                                Variates.olddir = "/";
                            } else {
                                Variates.dirStr = Variates.dirStr.replace(Variates.olddir, dirs[dirs.length - 2]);
                                Variates.olddir = dirs[dirs.length - 2];
                            }
                        }
                    } else {
                        //去下级目录
                        String[] dirs = Variates.froot.list();
                        boolean existFlag = false;
                        for (String dir : dirs) {
                            if (new File(Variates.froot.toString() + "\\" + dir).isDirectory() && dir.equals(cs[1])) {
                                Variates.froot = new File(Variates.froot + "/" + cs[1]);
                                Variates.dirStr = Variates.dirStr.replace(Variates.olddir, dir);
                                Variates.olddir = dir;
                                existFlag = true;
                            }
                        }
                        if (!existFlag) {
                            textIn.append("no such dir\n");
                        }
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootTextFirst(textIn);
                break;
            //创建文件
            case "touch":
                if (cs.length == 2) {
                    File newF = new File(Variates.froot + "\\" + cs[1]);
                    if (newF.exists()) {
                        textIn.append("the \'" + cs[1] + "\' exists");
                    } else {
                        try {
                            if (newF.createNewFile()) {
                                textIn.append(cs[1] + " created successfully");
                            }
                        } catch (Exception e) {
                            e = new CreateFileException();
                            String message = e.getMessage();
                            System.err.println(message);
                        }
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //删除目录/文件
            case "rm":
                if (cs.length == 3) {
                    if (cs[1].length() > 1 && cs[1].substring(1).equals("f")) {
                        //删除文件
                        File rmF = new File(Variates.froot + "\\" + cs[2]);
                        if (rmF.isFile()) {
                            rmDirOrFile(textIn, rmF, cs[2]);
                        } else {
                            textIn.append("no such file \'" + cs[2] + "\'");
                        }
                    } else if (cs[1].length() > 1 && cs[1].substring(1).equals("d")) {
                        //删除目录
                        File rmF = new File(Variates.froot + "\\" + cs[2]);
                        if (rmF.isDirectory()) {
                            rmDirOrFile(textIn, rmF, cs[2]);
                        } else {
                            textIn.append("no such dir \'" + cs[2] + "\'");
                        }
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //编辑文件
            case "vi":
                if (cs.length == 2) {
                    File vif = new File(Variates.froot + "\\" + cs[1]);
                    if (vif.exists() && vif.isFile()) {
                        textIn.append("please edit id");
                        textIn.setEditable(false);
                        new ViFile(vif, textIn);

                    } else {
                        textIn.append("no such file \'" + cs[1] + "\'");
                    }

                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //阅读文件
            case "cat":
                if(cs.length == 2){
                    File catf = new File(Variates.froot + "\\" + cs[1]);
                    if (catf.exists() && catf.isFile()) {
                        BufferedReader br = null;
                        try {
                            br = new BufferedReader(new FileReader(catf));
                            String line = "";
                            while ((line = br.readLine())!=null){
                                textIn.append(line+"\n");
                            }
                        } catch (Exception e){
                            e = new ReaderWriterException();
                            System.err.println(e.getMessage());
                        } finally {

                        }
                    } else {
                        textIn.append("no such file \'" + cs[1] + "\'");
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //复制文件
            case "cp":
                if (cs.length >= 2) {
                    File cpf = new File(Variates.froot + "\\" + cs[1]);
                    //复制文件
                    if (cpf.exists()) {
                        if (cs.length == 3) {
                            cpDirOrFile(textIn, cpf, cs[2]);
                        } else {
                            textIn.append("give a name to new file");
                        }
                    } else {
                        textIn.append("no such file \'" + cs[1] + "\'");
                    }
                } else {
                    textIn.append("param error");
                }
                ZfaceUtils.addRootText(textIn);
                break;
            //退出程序
            case "exit":
                System.exit(0);
                break;
            default:
                if (!"".equals(cs[0])) {
                    textIn.append("command error\n");
                }
                ZfaceUtils.addRootTextFirst(textIn);
                break;
        }
    }


    /**
     * 递归删除目录
     *
     * @param textIn
     * @param f
     * @return
     */
    private static void rmDirOrFile(JTextArea textIn, File f, String cs) {
        if (f.isFile()) {
            f.delete();
            textIn.append("file \'" + cs + "\' deletes successfully");
        } else if (f.isDirectory()) {
            String[] list = f.list();
            if (list.length == 0) {
                f.delete();
                return;
            } else {
                for (String s : list) {
                    rmDirOrFile(textIn, new File(f.toString() + "\\" + s), cs);
                }
            }
            f.delete();
            textIn.append("dir \'" + cs + "\' deletes successfully");
        }
    }

    /**
     * 递归复制文件
     *
     * @param textIn
     * @param f
     * @param cs
     */
    private static void cpDirOrFile(JTextArea textIn, File f, String cs) {
        File tarf = new File(Variates.froot + "\\" + cs);
        if (tarf.exists()) {
            textIn.append("the \'" + cs + "\' exists");
        } else {
            if(f.isFile()){
                BufferedReader br = null;
                BufferedWriter bw = null;
                try {
                    br = new BufferedReader(new FileReader(f));
                    bw = new BufferedWriter(new FileWriter(tarf));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        bw.write(line + "\n");
                        bw.flush();
                    }
                } catch (Exception e) {
                    e = new ReaderWriterException();
                    System.err.println(e.getMessage());
                } finally {
                    IOUtils.closeIO(br, bw);
                }
                textIn.append("cp successfully");
            }else if(f.isDirectory()){
                String[] list = f.list();
                for (String s : list) {

                }
            }
        }
    }
}
