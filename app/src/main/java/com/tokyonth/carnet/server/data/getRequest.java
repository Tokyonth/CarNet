package com.tokyonth.carnet.server.data;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getRequest {

    private OkHttpClient client;
    private String url = "http://222.19.48.34:9999/admin/query";

    public getRequest() {

        final Request request = new Request.Builder()
                .get()
                .tag(this)
                .url(url)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("--------->","打印GET响应的数据：" + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
