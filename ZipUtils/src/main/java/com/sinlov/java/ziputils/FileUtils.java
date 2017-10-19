package com.sinlov.java.ziputils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <pre>
 *     sinlov
 *
 *     /\__/\
 *    /`    '\
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!
 *    \  --  /
 *   /        \
 *  /          \
 * |            |
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * </pre>
 * Created by sinlov on 16/8/11.
 */
public class FileUtils {

    /**
     * 验证字符串是否为正确路径名正则表达式
     * <p>通过 path.matches(matches) 方法的返回值判断是否正确
     * <p>path 为路径字符串
     */
    public static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";
    private static final String INPUT_PATH_ERROR = "Your input path error ";
    private static File file = null;

    public static String getFileSuperFolder(String path) {
        checkInputPath(path);
        file = new File(path);
        if (file.isDirectory()) {
            return path;
        } else {
            return file.getParent();
        }
    }

    /**
     * 删除单个文件
     *
     * @param path Path
     * @return boolean true success or false failure
     */
    public static boolean deleteFile(String path) {
//        checkInputPath(path);
        file = new File(path);
        return file.isFile() && file.exists() && file.delete();
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return !file.isDirectory() && file.exists();
    }

    /**
     * 自动判断是否为文件路径，并根据路径删除指定的目录或文件，无论存在与否
     *
     * @param path full Path
     * @return boolean true is or false not
     */
    public static boolean deleteFolder(String path) {
//        checkInputPath(path);
        file = new File(path);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(path);
            } else {
                return deleteDirectory(path);
            }
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件 ,如果sPath不以文件分隔符结尾，自动添加文件分隔符
     * <p>如果dir对应的文件不存在，或者不是一个目录，则退出
     *
     * @param path full path
     * @return boolean 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String path) {
//        checkInputPath(path);
        File dirFile = new File(path);
        return deleteDirectoryInnerFiles(path) && dirFile.delete();
    }

    private static void checkInputPath(String path) {
        if (path.matches(matches)) {
            new Throwable(INPUT_PATH_ERROR).printStackTrace();
        }
    }

    /**
     * 删除目录下的文件
     * <br>如果path不以文件分隔符结尾，自动添加文件分隔符
     * <p>如果path对应的文件不存在，或者不是一个目录，则退出
     *
     * @param path full path
     * @return boolean 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectoryInnerFiles(String path) {
//        checkInputPath(path);
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        try {
            File[] files = dirFile.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (deleteFile(file.getAbsolutePath())) break;
                    } else {
                        if (deleteDirectory(file.getAbsolutePath())) break;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 以二进制读出文件内容
     *
     * @param file {@link File}
     * @return {@link Byte}[]
     */
    public static byte[] readFileBytes(File file) throws IOException {
        byte[] arrayOfByte = new byte[1024];
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        while (true) {
            int i = fis.read(arrayOfByte);
            if (i != -1) {
                localByteArrayOutputStream.write(arrayOfByte, 0, i);
            } else {
                return localByteArrayOutputStream.toByteArray();
            }
        }
    }
}
