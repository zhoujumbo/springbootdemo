package com.demo.iostream;

import java.io.*;
import java.util.StringTokenizer;

public class Example02 {


    public static void main(String[] args) {

        int n = -1;
        byte[] a = new byte[1024];
        try {
//            File f = new File("D:\\temp","MSDOS.txt");
//            File f = new File("D:\\temp","index.html");
            File f = new File("D:\\temp", "a.txt");
            InputStream in = new FileInputStream(f);
            while ((n = in.read(a, 0, 1024)) != -1) {
                String s = new String(a, 0, n);
                System.out.println(s);
            }
            in.close();


        } catch (IOException e) {
            System.out.println("File read Error:" + e);
        }

    }

    //    @Test
    public void outputStream() {

        System.out.println("测试outputStream");
        byte[] a = "往事匆匆".getBytes();
        byte[] b = "how do you do".getBytes();
        File f = new File("D:\\temp", "a.txt");
        try {
            OutputStream out = new FileOutputStream("f");
            System.out.println(f.getName() + "的大小：" + f.length());
            out.write(a);
            out.close();
            out = new FileOutputStream(f, true);
            System.out.println(f.getName() + "的大小：" + f.length());
            out.write(b, 0, b.length);
            System.out.println(f.getName() + "的大小：" + f.length());
            out.close();

        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }

    //    @Test
    public void fileReadAndWrite() {

        System.out.println("测试FileReadAndWrite");
        File fr = new File("D:\\temp", "index.html");
        File fw = new File("D:\\temp", "a.txt");
        char[] c = new char[1024];
        try {
            Writer out = new FileWriter(fw, true);
            Reader in = new FileReader(fr);
            int n = -1;

            while ((n = in.read(c)) != -1) {
                out.write(c, 1, n);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            System.out.println("file eror:" + e);
        }
    }

    //    @Test
    public void bufferedReaderAndWriter() {

        System.out.println("测试BufferedReaderAnWriter");
        String s = "测试BufferedReaderAndWriter";
        Writer out = null;
        BufferedWriter buffOut = null;
        Reader in = null;
        BufferedReader buffIn = null;
        try {
            File f = new File("D:\\temp", "a.txt");
            out = new FileWriter(f, true);
            buffOut = new BufferedWriter(out);
            buffOut.newLine();
            buffOut.write(s, 0, s.length() - 1);
            buffOut.flush();
            buffOut.close();

            in = new FileReader(f);
            buffIn = new BufferedReader(in);
            String str = null;
            while ((str = buffIn.readLine()) != null) {
                StringTokenizer fenxi = new StringTokenizer(str);
                int count = fenxi.countTokens();
                str = str + " 句子中有字" + count;
                System.out.println(str);
            }
            buffIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
