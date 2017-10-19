package com.sinlov.java.ziputils;

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
public final class PerformanceUtils {
    protected static long startTime;
    protected static long endTime;
    protected static long performanceTime;

    public static void Start(){
        startTime = System.currentTimeMillis();
    }

    public static long end(boolean isPrint){
        endTime = System.currentTimeMillis();
        if (endTime > startTime) {
            performanceTime = endTime - startTime;
        } else {
            performanceTime = 0;
        }
        if (isPrint) {
            System.out.println("Just use time: " + performanceTime + " ms");
        }
        return endTime;
    }
}
