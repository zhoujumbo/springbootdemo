package com.java;/**
 * @Description： TODO
 * @Author: zhoujumbo
 * @Date: ${Time} ${Date}
 */

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName FileTest
 * Files类提供遍历文件和目录、监控文件变化、读取和设置文件权限、查看文件属性的方法，功能十分强大。
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/9/4
 * @Version 1.0
 */
public class FileTest {

    // 遍历文件和目录
    @Test
    public void findDir() {

        try {
            Files.list(Paths.get(".")).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.newDirectoryStream(Paths.get(".")).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test01() {
        //Files::isReularFile 找出目录中的文件
        try {
            Files.list(Paths.get(".")).filter(Files::isRegularFile).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {
        // file->file.isHidden() 找出隐藏文件
        final File[] files = new File(".").listFiles(file -> file.isHidden());
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    @Test
    public void test03() {
        // Files.newBufferedWriter 迅速创建一个BufferedWriter，可以使编码语法更简洁
        Path path = Paths.get(".\\test.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write("Hello World!  222");
            writer.newLine();
            writer.write("The quick brown fox jumped over the lazy dog");
            writer.newLine();
            writer.write("The lazy dog jumped over the quick brown fox");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bfr = Files.newBufferedReader(path)) {
            System.out.println(bfr.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test04() {
        // Files.write() 使用简介的语法写入内容到文件
        try {
            Files.write(Paths.get("D:\\test1.txt"), "Hello".getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test05() {
        // 读取文件内容
        try {

            String fileName = ".\\test.txt";

//            if (!FileUtil.isExistsPath(fileName)) {
//                System.out.println("文件不存在");
//                return;
//            }

            //读取文件
            Stream<String> stringStream = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split(" ")));

            List<String> words = stringStream.collect(Collectors.toList());
            System.out.println("words===>" + words);


            List<String> lineLists = Files
                    .lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .collect(Collectors.toList());

            //输出文件函数
            System.out.println("lineLists====" + lineLists.size());

            //输出每一行文件内容
            lineLists.stream().forEach(System.out::println);

            //统计单词的个数
            long uniqueWords = Files.lines(Paths.get(fileName), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();

            System.out.println("There are " + uniqueWords + " unique words in data.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Paths类实例
     * Paths类获取文件或文件目录路径可以使用采用多个字符串形式，也可以使用Path.get(D:\\ReviewIO\\URL)这种形式。
     * 返回的Path对象完全可以代替File类用于文件IO操作。
     */
    @Test
    public void test06() {

        //以当前路径作为Path对象
        Path p = Paths.get(".");
        //使用传入的字符串返回一个Path对象
        Path p2 = Paths.get("D", "ReviewIO", "URL");
        //对应的路径
        System.out.println("p对象的对应路径：" + p.toString());
        System.out.println("p2对象的对应路径：" + p2.toString());
        //路径数量是以路径名的数量作为标准
        System.out.println("p路径数量：" + p.getNameCount());
        System.out.println("p2路径数量：" + p2.getNameCount());
        //获取绝对路径
        System.out.println("p绝对路径：" + p.toAbsolutePath());
        System.out.println("p2绝对路径：" + p2.toAbsolutePath());
        //获取父路径
        System.out.println("p父路径：" + p.getParent());
        System.out.println("p2父路径：" + p2.getParent());
        //获取p2对象的文件名或者文件目录名
        System.out.println(p2.getFileName());
        //通过Path对象返回一个分隔符对象
        Spliterator<Path> split = p2.spliterator();
    }

    /**
     * 文件复制
     *
     * @throws IOException
     */
    @Test
    public void test07() throws IOException {
        // Files完成文件复制的方法，方法很简单。
        /**
         * 从文件复制到文件：Files.copy(Path source, Path target, CopyOption options);

         从输入流复制到文件：Files.copy(InputStream in, Path target, CopyOption options);

         从文件复制到输出流：Files.copy(Path source, OutputStream out);
         */
        Path source = Paths.get("F:", "Java经典练习题.pdf");
        Path dest = Paths.get("F:", "files.txt");
        File f = new File("F:\\ok.pdf");
        f.createNewFile();//如果f对象对应路径不存在就创建一个。
        System.out.println("source对象的文件路径：" + source);
        //复制文件
        Files.copy(source, new FileOutputStream(f));
        Files.copy(source, System.out);
        Files.copy(Paths.get("C://my.ini"), Paths.get("C://my2.ini"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(System.in, Paths.get("C://my3.ini"), StandardCopyOption.REPLACE_EXISTING);
        //写入内容到文件
        //个人觉得用起来不怎么方便。方法参数很多，尤其是Iterable<? extends CharSequence>参数，Iterable是个顶级接口，
        // 实现类几乎都是集合类，并且限制了类型通配符上限是CharSequence，这意味着要使用泛型为字符类型的集合类作为数据写入指定文件中，很麻烦。
        ArrayList<String> as = new ArrayList<>();
        as.add("A");
        as.add("B");
        as.add("C");
        Files.write(dest, as, Charset.forName("GBK"));

        /** 返回值为boolean的操作方法样例 */
        System.out.println(Files.isHidden(source));//文件是否隐藏
        System.out.println(Files.isExecutable(source));//文件是否可执行
        System.out.println(Files.isWritable(source));//文件是否可写
        //获取Paths对象对应的文件路径的文件储存
        FileStore f1 = Files.getFileStore(Paths.get("F:"));
        FileStore e = Files.getFileStore(Paths.get("E:"));
        System.out.println("F盘的总大小" + f1.getTotalSpace());
        System.out.println("F盘的可用大小" + f1.getUsableSpace());
        System.out.println("F盘的未分配空间" + f1.getUnallocatedSpace());
    }

    /**
     * 遍历整个文件夹
     */
    @Test
    public void test08() throws IOException {
        List<Path> result = new LinkedList<>();
        Files.walkFileTree(Paths.get("."), new FindJavaVisitor(result));
        result.forEach(System.out::println);
    }

    private static class FindJavaVisitor extends SimpleFileVisitor<Path> {
        private List<Path> result;

        public FindJavaVisitor(List<Path> result) {
            this.result = result;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            // java 文件
//            if(file.toString().endsWith(".java")){
//                result.add(file.getFileName());
//            }
//            return FileVisitResult.CONTINUE;

            // 将目录下面所有符合条件的图片删除掉：filePath.matches(".*_[1|2]{1}\\.(?i)(jpg|jpeg|gif|bmp|png)")
//            String filePath = file.toFile().getAbsolutePath();
//            if(filePath.matches(".*_[1|2]{1}\\.(?i)(jpg|jpeg|gif|bmp|png)")){
//                try {
//                    Files.deleteIfExists(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                result.add(file.getFileName());
//            } return FileVisitResult.CONTINUE;
            // 为目录下的所有图片生成指定大小的缩略图。a.jpg 则生成 a_1.jpg
            String filePath = file.toFile().getAbsolutePath();
            int width = 224;
            int height = 300;
            StringUtils.substringBeforeLast(filePath, ".");
            String newPath = StringUtils.substringBeforeLast(filePath, ".") + "_1."
                    + StringUtils.substringAfterLast(filePath, ".");
//            ImageUtils2.cut3(filePath, newPath, width, height);
            result.add(file.getFileName());
            return FileVisitResult.CONTINUE;
        }
    }

    /**
     * 创建文件或者文件夹
     * 注意创建目录和文件Files.createDirectories 和 Files.createFile不能混用，必须先有目录，才能在目录中创建文件
     */
    @Test
    public void test09() {
        try {
            Files.createDirectories(Paths.get("C://TEST"));
            if (!Files.exists(Paths.get("C://TEST")))
                Files.createFile(Paths.get("C://TEST/test.txt"));
//            Files.createDirectories(Paths.get("C://TEST/test2.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取和设置文件权限
     */
    @Test
    public void test10() throws IOException {
        Path profile = Paths.get("/home/digdeep/.profile");
        PosixFileAttributes attrs = Files.readAttributes(profile, PosixFileAttributes.class);// 读取文件的权限
        Set<PosixFilePermission> posixPermissions = attrs.permissions();
        posixPermissions.clear();
        String owner = attrs.owner().getName();
        String perms = PosixFilePermissions.toString(posixPermissions);
        System.out.format("%s %s%n", owner, perms);

        posixPermissions.add(PosixFilePermission.OWNER_READ);
        posixPermissions.add(PosixFilePermission.GROUP_READ);
        posixPermissions.add(PosixFilePermission.OTHERS_READ);
        posixPermissions.add(PosixFilePermission.OWNER_WRITE);

        Files.setPosixFilePermissions(profile, posixPermissions);    // 设置文件的权限
    }

    @Test
    public void test11() {
        try (
                BufferedReader reader = Files.newBufferedReader(Paths.get("C:\\Members.sql"), StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("C:\\Members3.txt"), StandardCharsets.UTF_8);
        ) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                /**
                 * 业务场景是，sql server导出数据时，会将 datatime 导成16进制的binary格式，形如：, CAST(0x0000A2A500FC2E4F AS DateTime))
                 *   所以上面的程序是将最后一个 datatime 字段导出的 , CAST(0x0000A2A500FC2E4F AS DateTime) 删除掉，
                 *   生成新的不含有datetime字段值的sql 脚本。用来导入到mysql中。
                 */
                if (str != null && str.indexOf(", CAST(0x") != -1 && str.indexOf("AS DateTime)") != -1) {
                    String newStr = str.substring(0, str.indexOf(", CAST(0x")) + ")";
                    writer.write(newStr);
                    writer.newLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************************
     * 将list写入
     */
    @Test
    public void test12() {
        List<String> lines = Arrays.asList("这是第一行", "这是第二行");
        writeFileToTarget("b.txt", lines);
        writeFileToDest("c.txt", lines);
        writeBufferFileToDest("e.txt", lines);
    }

    /**
     * 写入文件到/target/classes
     *
     * @param destFile
     */
    public static void writeFileToTarget(String destFile, List<String> lines) {
        try {
            Path path = Paths.get(destFile);
            //可以选择传入第三个OpenOptions参数,不传入默认CREATE和TRUNCATE_NEW,即如果文件不存在,创建该文件,如果存在,覆盖
//            Files.write(path, lines);
            //还可以传入第三个参数,选择是否创建文件,是否在同名文件后拼接等
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("已写入完成.path = " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件到指定绝对路径
     *
     * @param destPath
     */
    public static void writeFileToDest(String destPath, List<String> lines) {
        try {
            Path path = Paths.get(destPath);
//      if (!Files.exists(path)){
//        Files.createFile(path);
//      }
            //可以选择传入第三个OpenOptions参数,不传入默认CREATE和TRUNCATE_NEW,即如果文件不存在,创建该文件,如果存在,覆盖
//      Files.write(path, lines,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(path, lines);
            System.out.println("已写入完成.path = " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 字符缓冲流写入文件到指定绝对路径
     *
     * @param destPath
     */
    public static void writeBufferFileToDest(String destPath, List<String> lines) {
        try {
            Path path = Paths.get(destPath);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
            lines.stream().forEach(s -> {
                try {
                    bufferedWriter.write(s);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bufferedWriter.close();
            System.out.println("已写入完成.path = " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


/**
 * @Date: 2019/3/1 15:01
 * @Description:
 */
class CopyFiles {
    public static void main(String[] args) {
        String ref = "E:/apache-tomcat-8.5.31.zip";
        String dest = "E:/pache-tomcat-8.5.31-copy.zip";

        Instant s = Instant.now();
        copyFileByFiles(ref, dest);
        System.out.println("copyFileByFiles = " + ChronoUnit.MILLIS.between(s, Instant.now()));
        s = Instant.now();

        copyFileChannel(ref, dest);
        System.out.println("copyFileChannel = " + ChronoUnit.MILLIS.between(s, Instant.now()));
        s = Instant.now();

        copyFileUsingFileStreams(ref, dest);
        System.out.println("copyFileUsingFileStreams = " + ChronoUnit.MILLIS.between(s, Instant.now()));
    }

    /**
     * 使用java7的Files
     *
     * @param refPath
     * @param destPath
     */
    public static void copyFileByFiles(String refPath, String destPath) {
        Path old = Paths.get(refPath);
        Path copy = Paths.get(destPath);

        try {
            //默认不会覆盖同名文件
//      Files.copy(old,copy);
            //传入第三个参数,使其可以覆盖同名文件
            Files.copy(old, copy, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("拷贝完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Java NIO包括transferFrom方法,根据文档应该比文件流复制的速度更快
     *
     * @param refPath
     * @param destPath
     */
    public static void copyFileChannel(String refPath, String destPath) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(refPath).getChannel();
            outputChannel = new FileOutputStream(destPath).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            System.out.println("拷贝完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputChannel != null) {
                    inputChannel.close();
                }
                if (inputChannel != null) {
                    outputChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 原始的输入输出流复制法
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileStreams(String source, String dest) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            System.out.println("拷贝完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}