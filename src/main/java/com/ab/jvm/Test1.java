package com.ab.jvm;

import java.util.ArrayList;


/**
 * 验证slot是复用的
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-10-30 22:51
 **/
public class Test1 {


    public static void main(String[] args) {
        //-Xms10M -Xmx10M
        //增加作用域表示当前bs数组运行完后，在作用域外可以被GC回收了
        {
            //占用1M内存
            byte[] bs = new byte[2 * 1024 * 1024];
            System.out.println(bs.length);
        }
        //实际情况是，因为 slot[1]还引用着bs对象,所以bs 对象并不会被回收

        //因为slot是复用的，所以加上 int a = 3; ，则slot[1]会被赋值成 a的值，此时bs经过gc后会被回收
        int a = 3;

        //JVM尽最大努力触发GC，不保证绝对成功
        System.gc();

        System.out.println("totalMemory:(M)"+Runtime.getRuntime().totalMemory()/1024.0/1024.0);
        System.out.println("freeMemory:(M)"+Runtime.getRuntime().freeMemory()/1024.0/1024.0);
        System.out.println("maxMemory:(M)"+Runtime.getRuntime().maxMemory()/1024.0/1024.0);
    }

}
