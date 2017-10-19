package com.sinlov.java.gradle.probe;

import java.util.concurrent.CountDownLatch;

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
 * Created by sinlov on 17/10/19.
 */
public class NativeOutOfMemory {

    private static final String string = "Thread OOM count => ";

    public static void threadOOM() {
        try {
            for (int i = 0; ; i++) {
                System.out.println(string + i);
                new Thread(new HoldThread()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class HoldThread extends Thread {
        CountDownLatch cdl = new CountDownLatch(1);

        public HoldThread() {
            this.setDaemon(true);
        }

        public void run() {
            try {
                cdl.await();
            } catch (InterruptedException e) {
            }
        }
    }
}
