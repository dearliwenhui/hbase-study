package com.ab.jvm;

import java.util.concurrent.TimeUnit;

/**
 * @description: 对象自救
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-11-08 22:55
 **/
public class HelpSelf {

    private static HelpSelf hs = null;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        //由于重写了finalize方法，所以创建对象的后，会自动将当前对象包装成：FinalReference
        //经历过一次GC后，FinalReference会将数据放到ReferenceQueue，同时会调用finalize()方法
        //让当前对象重新被引用，就自救成功
        hs = this;
        //如果没有对象自救，第二次GC后才会清理对象

    }

    public static void main(String[] args) throws InterruptedException {
        hs = new HelpSelf();
        System.out.println("hs  ==" + hs);

        hs = null;
        System.gc();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("first help self ==" + hs);

        //finalize()只会调用一次，第二次gc就被回收了
        hs = null;
        System.gc();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("second help self ==" + hs);

    }

}
