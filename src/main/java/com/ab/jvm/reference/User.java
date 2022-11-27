package com.ab.jvm.reference;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-11-05 20:25
 **/
public class User {
    //soft占用100k的内存
    //weak 和 Phantom 使用1k内存
    private byte[] bs = new byte[1024];
    private String userId;

    public User(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                '}';
    }

    //使用finalize()对PhantomReference有影响，需要进行两次GC，finalize()会将对象放到FinalReference中，使用Finalizer操作
/*    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("now finalize userId=" + userId);
    }*/
}
