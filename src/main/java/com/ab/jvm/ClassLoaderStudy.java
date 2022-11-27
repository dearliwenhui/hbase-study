package com.ab.jvm;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-11-13 14:51
 **/
public class ClassLoaderStudy {
    /**
     * 因为如果类中存在初始化语句，就依次执行这些初始化语句
     * 所以
     *     private static ClassLoaderStudy classLoaderStudy = new ClassLoaderStudy();
     * 在
     *     private static int a = 0;
     *     private static int b;
     * 之前，则 getA() = 0， getB() = 1
     * 在之后，则 getA() = 1， getB() = 1
     * 因为会顺序执行，a有个赋值0的操作
     */
    private static ClassLoaderStudy classLoaderStudy = new ClassLoaderStudy();
    private static int a = 0;
    private static int b;

    private ClassLoaderStudy() {
        a++;
        b++;
        System.out.println("a = " + a + " b = " + b);
    }

    public static ClassLoaderStudy getInstance() {
        return classLoaderStudy;
    }

    public static int getA() {
        return a;
    }

    public static int getB() {
        return b;
    }

}
