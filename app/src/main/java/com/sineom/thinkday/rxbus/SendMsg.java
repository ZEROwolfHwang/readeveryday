package com.sineom.thinkday.rxbus;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/8/18 10:31
 * @des 预览保存当前页显示的内容
 * @updateAuthor ${Author}
 * @updataTIme 2016/8/18
 * @updataDes ${描述更新内容}
 */

public class SendMsg<T> {
    public int code;
    public T mT;


    public SendMsg(int code, T t) {
        this.code = code;
        mT = t;
    }

}
