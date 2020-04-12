package com.demo.iostream;

import java.io.File;

public class Example01 {

    public static void main(String[] args){

        File file = new File("d:\\temp","MSDOS.txt");

        System.out.println(file.getName().split("\\.")[0]+"是可读的吗："+file.canRead());
        System.out.println(file.getName().split("\\.")[0]+"是可写的吗："+file.canWrite());
        System.out.println(file.getName().split("\\.")[0]+"存在吗："+file.exists());
        System.out.println(file.getName().split("\\.")[0]+"长度是："+file.length()+"kb");
        System.out.println(file.getName().split("\\.")[0]+"绝对路径："+file.getAbsolutePath());
        System.out.println(file.getName().split("\\.")[0]+"父文件夹名称："+file.getParent());
        System.out.println(file.getName().split("\\.")[0]+"是文件吗："+file.isFile());
        System.out.println(file.getName().split("\\.")[0]+"是目录吗："+file.isDirectory());
        System.out.println(file.getName().split("\\.")[0]+"是否隐藏："+file.isHidden());
        System.out.println(file.getName().split("\\.")[0]+"最后修改时间："+file.lastModified());




    }

}
