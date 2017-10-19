package com.sinlov.java.ziputils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
 * Created by sinlov on 16/8/12.
 */
public class CopyFileUtils {

    private static String MESSAGE = "";

    /**
     * copy single file
     *
     * @param srcFileName  wait to copy
     * @param destFileName dest to path
     * @param overlay      is overlay
     * @return boolean
     */
    public static boolean singleFile(String srcFileName, String destFileName,
                                     boolean overlay) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            MESSAGE = "source：" + srcFileName + "not exists";
            return false;
        } else if (!srcFile.isFile()) {
            MESSAGE = "copy file error，source：" + srcFileName + "is not a file";
            return false;
        }

        File destFile = new File(destFileName);
        if (destFile.exists()) {
            if (overlay) {
                new File(destFileName).delete();
            }
        } else {
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    return false;
                }
            }
        }

        int byteread = 0;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * copy directory
     *
     * @param srcDirName  from
     * @param destDirName to
     * @param overlay     is overlay
     * @return boolean
     */
    public static boolean directory(String srcDirName, String destDirName,
                                    boolean overlay) {
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            MESSAGE = "copy directory failure: resource " + srcDirName + "not exists";
            return false;
        } else if (!srcDir.isDirectory()) {
            MESSAGE = "copy directory failure: " + srcDirName + "is not directory";
            return false;
        }

        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        if (destDir.exists()) {
            if (overlay) {
                new File(destDirName).delete();
            } else {
                MESSAGE = "copy directory failure: dest" + destDirName + "has exists";
                return false;
            }
        } else {
            System.out.println("dest dir not exist, create!");
            if (!destDir.mkdirs()) {
                System.out.println("copy directory failure: create dset folder failure");
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    flag = CopyFileUtils.singleFile(files[i].getAbsolutePath(),
                            destDirName + files[i].getName(), overlay);
                    if (!flag)
                        break;
                } else if (files[i].isDirectory()) {
                    flag = CopyFileUtils.directory(files[i].getAbsolutePath(),
                            destDirName + files[i].getName(), overlay);
                    if (!flag)
                        break;
                }
            }
        } else {
            MESSAGE = "copy files is null";
        }
        if (!flag) {
            MESSAGE = "copy from " + srcDirName + "to" + destDirName + "failure";
            return false;
        } else {
            return true;
        }
    }

    /**
     * Single thread fast copy, as fast as file bigger
     *
     * @param source from
     * @param target to
     */
    public static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
            inStream.close();
            outStream.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Monitoring the progress of replication
     *
     * @param source from
     * @param target to
     */
    public static void nioBufferCopy(File source, File target) {
        FileChannel in;
        FileChannel out;
        FileInputStream inStream;
        FileOutputStream outStream;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
            inStream.close();
            outStream.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * custom buffer stream copy
     *
     * @param source from
     * @param target to
     */
    private static void customBufferStreamCopy(File source, File target) {
        InputStream fis;
        OutputStream fos;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
