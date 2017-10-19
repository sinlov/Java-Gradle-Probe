package com.sinlov.java.ziputils.cmd;

import com.sinlov.java.ziputils.CopyFileUtils;
import com.sinlov.java.ziputils.FileUtils;
import com.sinlov.java.ziputils.PerformanceUtils;
import com.sinlov.java.ziputils.ZipUtils;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Arrays;

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
 * Created by sinlov on 16/11/8.
 */
public class Main {
    private static boolean isVerbose = false;
    private static boolean isForce = false;

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "print help");
        options.addOption("v", "verbose", false, "print verbose");
        options.addOption("f", "force", false, "force");
        options.addOption("p", "path", true, "path");
        options.addOption("t", "tag", true, "tag file path");
        options.addOption("d", "directory", true, "tag directory path");
        options.addOption("c", "copy", false, "copy");
        options.addOption("z", "zip", false, "zip");
        options.addOption("x", "unzip", false, "unzip");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            int optionCount = 0;
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                //TODO set usage
                formatter.printHelp(" -v -x -p ./shell/ -t ./build/tag.zip", options);
                System.exit(0);
            }
            if (cmd.hasOption("v")) {
                Main.isVerbose = true;
            }
            if (cmd.hasOption("f")) {
                Main.isForce = true;
            }
            String path = null;
            if (cmd.hasOption("p")) {
                path = cmd.getOptionValue("path", "");
                verboseSysOut("path", path);
                optionCount++;
            }
            String tag = null;
            if (cmd.hasOption("t")) {
                tag = cmd.getOptionValue("tag", "");
                verboseSysOut("tag", tag);
                optionCount++;
            }
            String directory = null;
            if (cmd.hasOption("d")) {
                directory = cmd.getOptionValue("directory", "");
                verboseSysOut("directory", directory);
                optionCount++;
            }
            if (optionCount == 0) {
                System.out.println("Error args input please use -h for help");
                System.exit(-1);
            }
            if (isStringEmpty(path)) {
                System.err.println("You must use -p for path");
                System.exit(-1);
            }

            if (isStringEmpty(tag) && isStringEmpty(directory)) {
                System.err.println("You must use -t or -d for tag");
                System.exit(-1);
            }
            boolean isCopy = false;
            int cmdCount = 0;
            if (cmd.hasOption("c")) {
                verboseSysOut("cmd", "copy");
                isCopy = true;
                cmdCount++;
            }
            boolean isZip = false;
            if (cmd.hasOption("z")) {
                verboseSysOut("cmd", "zip");
                isZip = true;
                cmdCount++;
            }
            boolean isUnZip = false;
            if (cmd.hasOption("x")) {
                verboseSysOut("cmd", "unzip");
                isUnZip = true;
                cmdCount++;
            }
            if (cmdCount != 1) {
                System.err.println("Must has one cmd and Only use one cmd of -z -c -x");
                System.exit(-1);
            }

            if (isCopy) {
                PerformanceUtils.Start();
                File sourceFile = new File(path);
                if (isStringEmpty(directory)) {
                    System.err.println("need -d for out copy");
                    System.exit(-2);
                }
                File tagDir = new File(directory);
                if (sourceFile.isDirectory()) {
                    if (tagDir.exists()) {
                        if (isForce) {
                            verboseSysOut("copy", "start path: ", path);
                            CopyFileUtils.directory(path, directory, true);
                        } else {
                            System.err.println("tag directory " + directory + "is exists stop copy");
                            System.exit(-2);
                        }
                    } else {
                        verboseSysOut("copy", "start path: ", path);
                        CopyFileUtils.directory(path, directory, false);
                    }
                } else {
                    if (tagDir.exists()) {
                        if (isForce) {
                            verboseSysOut("copy", "start path: ", path);
                            CopyFileUtils.singleFile(path, directory, true);
                        } else {
                            System.err.println("tag file " + directory + " is exists stop copy");
                            System.exit(-2);
                        }
                    } else {
                        verboseSysOut("copy", "start path: ", path);
                        CopyFileUtils.singleFile(path, directory, false);
                    }
                }
                verboseSysOut("copy", "end path: ", path);
                PerformanceUtils.end(isVerbose);
                System.exit(0);
            }

            if (isUnZip) {
                PerformanceUtils.Start();
                if (isStringEmpty(directory)) {
                    System.err.println("need -d for out unzip");
                    System.exit(-2);
                }
                File tagDir = new File(directory);
                if (tagDir.exists()) {
                    if (isForce) {
                        FileUtils.deleteFolder(directory);
                    } else {
                        System.err.println("tag directory " + directory + " is exists stop unzip");
                        System.exit(-2);
                    }
                }
                verboseSysOut("unzip", "from path: ", path, "to path", directory);
                ZipUtils.unZip(path, directory);
                verboseSysOut("unzip", "end path: ", path);
                PerformanceUtils.end(isVerbose);
                System.exit(0);
            }

            if (isZip) {
                PerformanceUtils.Start();
                if (isStringEmpty(tag)) {
                    System.err.println("need -t for out zip");
                    System.exit(-2);
                }
                File tagDir = new File(tag);
                if (tagDir.exists()) {
                    if (isForce) {
                        FileUtils.deleteFile(tag);
                    } else {
                        System.err.println("tag file " + tag + " is exists stop zip");
                        System.exit(-2);
                    }
                }
                verboseSysOut("zip", "from path: ", path, "to path", tag);
                ZipUtils.zip(path, tag);
                verboseSysOut("zip", "end path: ", path);
                PerformanceUtils.end(isVerbose);
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Error args input please use -h for help");
            e.printStackTrace();
            System.exit(-2);
        }

    }

    private static void verboseSysOut(final String msg, String... args) {
        if (isVerbose) {
            System.out.println(msg + ": " + Arrays.toString(args));
        }
    }

    private static boolean isStringEmpty(String str) {
        return null == str || str.equals("");
    }
}
