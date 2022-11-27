package com.ab.jvm;

import java.util.ArrayList;


/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-10-30 22:51
 **/
public class Test {
    //占用1M内存
    private byte[] bs = new byte[1024 * 1024];

    public static void main(String[] args) {

        ArrayList<Test> tests = new ArrayList<>();
        try {
            while (true) {
                tests.add(new Test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("totalMemory:(M)"+Runtime.getRuntime().totalMemory()/1024.0/1024.0);
        System.out.println("freeMemory:(M)"+Runtime.getRuntime().freeMemory()/1024.0/1024.0);
        System.out.println("maxMemory:(M)"+Runtime.getRuntime().maxMemory()/1024.0/1024.0);
    }

}
