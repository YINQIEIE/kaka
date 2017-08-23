package com.yq.httputils;

import com.yq.httputils.callback.HttpCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yinqi on 2017/6/20.
 */

public abstract class HttpCallbackImpl<Result> implements HttpCallback {

    ReturnMessage<Result> returnMessage;

    @Override
    public void onSuccess(String response) {
//        Class<?> clazz = analysisClass(this);

    }

    public abstract void onSuccess(Result result);

    public static Class<?> analysisClass(Object obj) {

        Type genType = obj.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];

    }


}
