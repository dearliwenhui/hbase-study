package com.ab.jvm.reference;

import java.lang.ref.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-11-05 20:09
 **/
public class ReferenceTest {
    //当GC释放对象内存的时候，会将引用加入到引用队列ReferenceQueue
    private static ReferenceQueue<User> rq = new ReferenceQueue<>();

    private static void printQueue(String str) throws Exception {
        Reference<? extends User> obj ;
        while ((obj = rq.poll()) != null) {
            //打印ReferenceQueue中的Reference信息
            System.out.println("the gc Object reference == " + str + " = " + obj.get() + " hashcode of Reference Object =" + obj.hashCode());
            //通过反射获取Reference中的字段，SoftReference和WeakReference中的字段会被删除
            Field rereferent = Reference.class.getDeclaredField("referent");
            rereferent.setAccessible(true);
            Object result = rereferent.get(obj);
            System.out.println(result);
        }

    }

    private static void testSoftReference() throws Exception {
        List<SoftReference<User>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SoftReference<User> userSoftReference = new SoftReference<>(new User("soft " + i), rq);
            //从softReference中获取User
            System.out.println("now the user ==" + userSoftReference.get() + " SoftReference hashcode " + userSoftReference.hashCode());
            list.add(userSoftReference);
        }
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        printQueue("soft");
    }

    private static void testWeakReference() throws Exception {
        List<WeakReference<User>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("weak " + i);
            WeakReference<User> userWeakReference = new WeakReference<>(user, rq);
            //从weakReference中获取User
            System.out.println("now the user ==" + userWeakReference.get() + " WeakReference hashcode " + userWeakReference.hashCode());
            list.add(userWeakReference);
        }
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        printQueue("weak");
    }

    private static void testPhantomReference() throws Exception {
        List<PhantomReference<User>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("phantom " + i);
            PhantomReference<User> userPhantomReference = new PhantomReference<>(user, rq);
            //从weakReference中获取User
            System.out.println("now the user ==" + userPhantomReference.get() + " PhantomReference hashcode " + userPhantomReference.hashCode());
            list.add(userPhantomReference);
        }

        System.gc();
        TimeUnit.SECONDS.sleep(2);
        printQueue("phantom111");
        System.out.println("==========list is null");

        for (PhantomReference<User> userPhantomReference : list) {
            System.out.println(userPhantomReference.get());

        }
        System.gc();
        for (PhantomReference<User> userPhantomReference : list) {
            System.out.println(userPhantomReference.get());

        }
        TimeUnit.SECONDS.sleep(2);
        printQueue("phantom");
    }

    public static void main(String[] args) throws Exception {
//        testSoftReference();
//                testWeakReference();
        testPhantomReference();
    }




}
