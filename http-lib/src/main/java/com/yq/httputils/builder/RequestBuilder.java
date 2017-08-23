package com.yq.httputils.builder;

import com.yq.httputils.service.IhttpService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 */
public abstract class RequestBuilder {

    public String url;
    public Object tag;
    public Map<String, String> headers;
    public Map<String, Object> params;

    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }


    public RequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public RequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

    public RequestBuilder params(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public abstract IhttpService build();
}
