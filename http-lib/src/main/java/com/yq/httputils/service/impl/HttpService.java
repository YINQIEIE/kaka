package com.yq.httputils.service.impl;

import com.yq.httputils.builder.RequestBuilder;
import com.yq.httputils.callback.HttpCallback;
import com.yq.httputils.processor.IHttpProcessor;
import com.yq.httputils.service.IhttpService;

/**
 * Created by yinqi on 2017/6/20.
 * 请求实现类
 */

public class HttpService implements IhttpService {

    private static HttpService service;
    private RequestBuilder builder;
    private static IHttpProcessor processor;

    public static void initHttpService(IHttpProcessor mProcessor) {
        processor = mProcessor;
    }

    public HttpService(RequestBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void post(HttpCallback callback) {
        processor.post(builder, callback);
    }

    @Override
    public void get(HttpCallback callback) {
        processor.get(builder, callback);
    }

    @Override
    public void cancelRequestByTag(Object tag) {
        processor.cancelRequestByTag(tag);
    }

    /**
     * 请求参数 builder类
     * 通过其对象构建请求参数
     */
    public static class ServiceBuilder extends RequestBuilder {
        @Override
        public IhttpService build() {
            return new HttpService(this);
        }
    }
}
