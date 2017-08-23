package com.yiqile.kaka;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yq.httputils.service.IhttpService;
import com.yq.httputils.service.impl.HttpService;
import com.yq.httputils.callback.HttpCallback;
import com.yq.httputils.processor.impl.OkhttpProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.encryptiontutil.AESUtils;

import static util.encryptiontutil.RSAUtils.encryptByPublicKey;

public class MainActivity2 extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private String publicKey = "";
    private String aesKey;
    private Object seessionId;
    private Map<String, Object> reqParamMap;
    TextView tv_test;
    private IhttpService httpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HttpService.initHttpService(new OkhttpProcessor());

        tv_test = (TextView) findViewById(R.id.tv_test);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        HttpService.init(new OkhttpProcessor());
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8L+BIjwl9dtsDx2GvsA0mdG8ml6utRP2Epp9rZ9sVzZWDxCkEJVST/Hy49yOUHzCdN0PeAZ6cUjjRys8w05U0EoxKbZsJO4N9MComraiVeiBXaC1JwDX+z3/37LsrQw1MRgy0eJ6/ZEyLcTdusOWp9HdJYQt8zJWGOOxYk25RKQIDAQAB";
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                request(publicKey);
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_LONG).show();
                for (int i = 0; i < 5; i++) {
                    new Thread() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);
                            test2();
                            SystemClock.sleep(1000);
                        }
                    }.start();

                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

//    private void test2() {
//        reqParamMap = new HashMap<>();
//        reqParamMap.put("licenseId", 102);
//        String url = "https://phone.hnticai.com/game/getCurrentIssueInterest.json";
//        IHttpService httpService = new OkhttpProcessor.OkhttpRequestBuilder()
//                .url(url)
//                .params(reqParamMap)
//                .tag(this)
//                .build();
//        httpService.post(new HttpCallback() {
//            @Override
//            public void onSuccess(String body) {
//
//            }
//
//            @Override
//            public void onFailure(String e) {
//
//            }
//        });
//        HttpService.getServiceInstance().excute(httpService, new HttpCallback() {
//            @Override
//            public void onSuccess(String body) {
//                Log.i(TAG, "onSuccess: " + body);
//                tv_test.setText(body);
//            }
//
//            @Override
//            public void onFailure(String e) {
//                Log.i(TAG, "onFailure: " + e);
//            }
//        });
//    }

    private void test2() {
        reqParamMap = new HashMap<>();
        reqParamMap.put("licenseId", 102);
        String url = "https://phone.hnticai.com/game/getCurrentIssueInterest.json";
        httpService = new HttpService.ServiceBuilder()
                .url(url)
                .params(reqParamMap)
                .tag(this)
                .build();
        httpService.post(new HttpCallback() {
            @Override
            public void onSuccess(String body) {
                Log.i(TAG, "onSuccess: " + body);
                tv_test.setText(body);
            }

            @Override
            public void onFailure(String e) {
                Log.i(TAG, "onFailure: " + e);
            }
        });
    }

    private void getSessionId() {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceToken", "123456");
        aesKey = encryptByPublicKey(AESUtils.DEFAULT_KEY, publicKey);
        map.put("key", aesKey);
        httpService = new HttpService.ServiceBuilder()
                .url("http:192.168.1.181:8080/app/getSession")
                .params(reqParamMap)
                .tag(this)
                .build();
        httpService.post(new HttpCallback() {
            @Override
            public void onSuccess(String body) {
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    seessionId = jsonObject.optString("obj");
                    request(publicKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String e) {

            }
        });
    }

    private void request(String publicKey) {
        if (TextUtils.isEmpty(publicKey)) {
            Toast.makeText(this, "key 为空", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            JSONObject json = new JSONObject();
            json.put("userName", "admin");
            json.put("password", "admin");
            String data = AESUtils.encryptData(AESUtils.DEFAULT_KEY, json.toString());
            paramsMap.put("sessionId", seessionId);
            paramsMap.put("json", data);
            httpService = new HttpService.ServiceBuilder()
                    .url("http://192.168.1.181:8080/app/login")
                    .params(reqParamMap)
                    .tag(this)
                    .build();
            httpService.post(new HttpCallback() {
                @Override
                public void onSuccess(String body) {
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.optString("code").equals("406")) {
                            String key = jsonObject.optString("key");//获取公钥
                            setPublicKey(key);
                            getSessionId();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String e) {
                    Log.i(TAG, "onFailure: " + e);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPublicKey(String key) {
        this.publicKey = key;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
