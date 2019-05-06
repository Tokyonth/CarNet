package com.tokyonth.carnet.server.user;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.tokyonth.carnet.bean.UserBean;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register {

    private OkHttpClient client = new OkHttpClient();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息",ReturnMessage);
                UserBean userBean = new Gson().fromJson(ReturnMessage, UserBean.class);
                String code = userBean.getCode();
                Log.i("Code", code);
            }

        }
    };


    public void postRequest(String username,String password)  {
        RequestBody formBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        final Request request = new Request.Builder()
                .url("http://118.24.135.36:8080/TtrpUser/LoginServlet")
                .post(formBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
