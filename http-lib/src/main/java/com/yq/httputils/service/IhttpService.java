package com.yq.httputils.service;

import com.yq.httputils.callback.HttpCallback;

/**
 * Created by yinqi on 2017/8/8.
 */

public interface IhttpService {

    void post(HttpCallback callback);

    void get(HttpCallback callback);

    void cancelRequestByTag(Object tag);
}
