package com.ab.jvm.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-11-06 14:22
 **/
public class App {

    private static ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private static List<PhantomReference> references = new ArrayList<>();
    private static List<DataObj> dataObjs = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 32; ++i) {
            DataObj obj = new DataObj();
            PhantomReference<Object> phantomReference = new PhantomReference<>(obj, referenceQueue);
            references.add(phantomReference);
            dataObjs.add(obj);
        }

        // 设置为null 让相关对象编程虚可达 被GC特殊对待一下
        dataObjs = null;

        System.gc();
        System.out.println("1111");
        TimeUnit.SECONDS.sleep(1);

        // 直接在主线程看
        Reference<?> reference = null;
        while ((reference = referenceQueue.poll()) != null) {
            System.out.println(reference);
            reference.clear();
        }
    }


}

class DataObj {
}