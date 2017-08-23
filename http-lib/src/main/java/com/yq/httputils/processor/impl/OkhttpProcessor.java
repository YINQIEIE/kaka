package com.yq.httputils.processor.impl;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yq.httputils.builder.RequestBuilder;
import com.yq.httputils.callback.HttpCallback;
import com.yq.httputils.processor.IHttpProcessor;
import com.yq.httputils.utils.OkHttpClientUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yinqi on 2017/6/20.
 */

public class OkhttpProcessor implements IHttpProcessor {

    private static OkHttpClient client;
    private Handler handler;

    public OkhttpProcessor() {
        init();
    }

    private void init() {
        if (null == client)
            this.client = OkHttpClientUtil.getClient();
        if (null == handler)
            this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(RequestBuilder builder, HttpCallback callback) {
        post(builder.url, builder.params, builder.tag, callback);
    }

    @Override
    public void get(RequestBuilder builder, HttpCallback callback) {
        get(builder.url, builder.tag, callback);
    }

    @Override
    public void cancelRequestByTag(Object tag) {
        if (null == client) return;
        Log.i("cancel before", "cancelRequestByTag: ");
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                Log.i("cancel queuedCalls", "cancelRequestByTag: " + call.request().tag());
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                Log.i("cancel runningCalls", "cancelRequestByTag: " + call.request().tag());
                call.cancel();
            }
        }
    }

    public void post(String url, Map<String, Object> params, Object tag, final HttpCallback callback) {
//        JSONObject jsonObject = new JSONObject(params);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (null != params) {//生成表单参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                bodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
     /*for (String key : params.keySet())
      bodyBuilder.add(key, String.valueOf(params.get(key)));*/
        }
        FormBody body = bodyBuilder.build();
        Request request = new Request.Builder().url(url).tag(tag).post(body).build();
        client.newCall(request).enqueue(new MyCallback(callback));
    }

    public void get(String url, Object tag, HttpCallback callback) {
        Request request = new Request.Builder().url(url).tag(tag).get().build();
        client.newCall(request).enqueue(new MyCallback(callback));
    }

    /**
     * 请求回调
     * 回调处于子线程
     */
    private class MyCallback implements Callback {
        private HttpCallback callback;

        public MyCallback(HttpCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(e.getMessage());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (response.isSuccessful()) callback.onSuccess(response.body().string());
                        else callback.onFailure(response.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}