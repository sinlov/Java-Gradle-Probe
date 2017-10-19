package com.sinlov.java.ziputils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
public class ZipUtils {

    public static void zip(String sourceDir, String zipFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(zipFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ZipOutputStream zos = new ZipOutputStream(bos);
            File file = new File(sourceDir);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
            zos.closeEntry();
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File source, String basePath,
                                ZipOutputStream zos) {
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        try {
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    pathName = file.getPath().substring(basePath.length() + 1)
                            + "/";
                    zos.putNextEntry(new ZipEntry(pathName));
                    zipFile(file, basePath, zos);
                } else {
                    pathName = file.getPath().substring(basePath.length() + 1);
                    InputStream is = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    zos.putNextEntry(new ZipEntry(pathName));
                    while ((length = bis.read(buf)) > 0) {
                        zos.write(buf, 0, length);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unZip(String zipPath, String destDir) {
        if (!destDir.endsWith(File.separator)) {
            destDir = destDir + File.separator;
        }
        byte b[] = new byte[1024];
        int length;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(zipPath));
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(destDir + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    // 每次从最底层开始遍历的
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists())
                        loadFile.getParentFile().mkdirs();
                    OutputStream outputStream = new FileOutputStream(loadFile);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    while ((length = inputStream.read(b)) > 0)
                        outputStream.write(b, 0, length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
