package com.tokyonth.carnet.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.gson.Gson;
import com.tokyonth.carnet.BaseActivity;
import com.tokyonth.carnet.R;
import com.tokyonth.carnet.bean.UserBean;
import com.tokyonth.carnet.ui.widget.ClearEditText;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private ClearEditText et_name;
    private ClearEditText et_email;
    private ClearEditText et_pwd;
    private ClearEditText et_confirm_pwd;
    private LoadingButton reg_btn;
    private Toolbar toolbar;

    private static final int HANDLER = 1;
    private static final int REGISTER_SUCCESS = 130;
    private static final int HTTP_ERROR = 101;

    private OkHttpClient client = new OkHttpClient();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == HANDLER){
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息",ReturnMessage);
                UserBean userBean = new Gson().fromJson(ReturnMessage, UserBean.class);
                int code = Integer.valueOf(userBean.getCode());
                switch (code) {
                    case REGISTER_SUCCESS:
                        showToast("注册成功!");
                        reg_btn.loadingSuccessful();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                        break;
                    case HTTP_ERROR:
                        showToast("未知错误!");
                        reg_btn.loadingFailed();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);
        initView();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_name = (ClearEditText) findViewById(R.id.reg_account);
        et_email = (ClearEditText) findViewById(R.id.reg_email);
        et_pwd = (ClearEditText) findViewById(R.id.reg_pwd);
        et_confirm_pwd = (ClearEditText) findViewById(R.id.pwd_confirm);
        reg_btn = (LoadingButton) findViewById(R.id.btn_register);
        setSupportActionBar(toolbar);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register() {
        String username = et_name.getText().toString().trim();
        String password = et_pwd.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String confirm_pwd = et_confirm_pwd.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(confirm_pwd)) {
            if (!password.equals(confirm_pwd)) {
                showToast("两次输入的密码不一致!");
            } else {
                reg_btn.startLoading();
                RequestBody formBody = new FormBody.Builder()
                        .add("email",email)
                        .add("pwd",password)
                        .add("name",username)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://118.24.135.36:8080/TtrpUser/RegistServlet")
                        .post(formBody)
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                mHandler.obtainMessage(HANDLER, response.body().string()).sendToTarget();
                            } else {
                                throw new IOException("Unexpected code:" + response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else if (TextUtils.isEmpty(username)) {
            et_name.setShakeAnimation();
        } else if (TextUtils.isEmpty(email)) {
            et_email.setShakeAnimation();
        } else if (TextUtils.isEmpty(password)) {
            et_pwd.setShakeAnimation();
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            et_confirm_pwd.setShakeAnimation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
