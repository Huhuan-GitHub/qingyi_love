package com.neusoft.qingyi.util;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    /**
     * 判断指定路径下的文件夹是否存在
     * 如果存在返回ture，如果不存在但创建成功返回true，否则返回false
     *
     * @param path 指定路径下的文件夹
     * @return 是否存在？
     */
    public static boolean folderExists(String path) {
        File root = new File(path);
        if (!root.exists()) {
            boolean mkdir = root.mkdir();
            if (mkdir) {
                System.out.println("文件夹创建成功");
                return true;
            } else {
                System.out.println("文件夹创建失败");
                return false;
            }
        }
        return true;
    }

    /**
     * 将指定的输入流写入本地文件，返回相对路径
     *
     * @param filePath 文件路径+文件名
     * @param in       输入流
     *                 写入结果（成功：返回相对路径；失败：抛出写入失败异常）
     */
    public static void writePostsImg(String filePath, InputStream in) throws IOException {
        File file = new File(filePath);
        OutputStream out = Files.newOutputStream(file.toPath());
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.close();
    }
}
