package com.yq.httputils.processor;

import com.yq.httputils.builder.RequestBuilder;
import com.yq.httputils.callback.HttpCallback;

/**
 * Created by yinqi on 2017/6/20.
 */

public interface IHttpProcessor {

    void post(RequestBuilder builder, HttpCallback callback);

    void get(RequestBuilder builder, HttpCallback callback);

    void cancelRequestByTag(Object tag);

}
