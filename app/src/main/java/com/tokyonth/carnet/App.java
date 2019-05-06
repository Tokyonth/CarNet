package com.tokyonth.carnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;
import com.tokyonth.carnet.activity.RegisterActivity;
import com.tokyonth.carnet.bean.UserBean;
import com.tokyonth.carnet.ui.widget.ClearEditText;
import com.tokyonth.carnet.utils.SPUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class App extends BaseActivity implements View.OnClickListener {

    private LoadingButton load_btn;
    private TextView tv_no_account;
    private ClearEditText et_account;
    private ClearEditText et_password;
    private CheckBox pwd_cb;
    private Toolbar toolbar;

    private FABProgressCircle fabProgressCircle;
    private FloatingActionButton fab;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.user_login);
        new SPUtils(this,"user_per");
        initView();
        setSupportActionBar(toolbar);
        setTitle(null);
    }

    private void  initView() {
        load_btn = (LoadingButton) findViewById(R.id.btn_login);
        tv_no_account = (TextView) findViewById(R.id.tv_noacc);
        et_account = (ClearEditText) findViewById(R.id.act_main_et_account);
        et_password = (ClearEditText) findViewById(R.id.act_main_et_password);
        pwd_cb = (CheckBox) findViewById(R.id.act_main_cb);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        fab = (FloatingActionButton) findViewById(R.id.login_fab);

        load_btn.setOnClickListener(this);
        tv_no_account.setOnClickListener(this);
        pwd_cb.setOnClickListener(this);
        fabProgressCircle.setOnClickListener(this);
        fab.setOnClickListener(this);

        et_account.setText((String) SPUtils.getData("account",""));
        et_password.setText((String) SPUtils.getData("password",""));
        pwd_cb.setChecked((Boolean) SPUtils.getData("password_save",false));
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息",ReturnMessage);
                UserBean userBean = new Gson().fromJson(ReturnMessage, UserBean.class);
                int code = Integer.valueOf(userBean.getCode());
                final String name = userBean.getUserName();
               // SPUtils.putData("user_name",name);
                switch (code) {
                    case 120:
                        Toast.makeText(App.this,"登录成功",Toast.LENGTH_SHORT).show();
                        JumpTo(name);
                        break;
                    case 121:
                        Toast.makeText(App.this,"用户不存在",Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fabProgressCircle.hide();
                            }
                        }, 1500);
                        break;
                    case 122:
                        Toast.makeText(App.this,"用户密码错误",Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fabProgressCircle.hide();
                            }
                        }, 1500);
                        break;
                }
            }
        }
    };

    private void JumpTo(final String name) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(App.this,IWalk.class);
                intent.putExtra("Verification",true);
                intent.putExtra("User_Name",name);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }

    private void Register() {
        Intent intent = new Intent();
        intent.setClass(App.this,RegisterActivity.class);
        startActivity(intent);
    }

    private void SaveUser() {
        String acc = et_account.getText().toString().trim();
        SPUtils.putData("account",acc);
        if ((boolean)SPUtils.getData("password_save",false)) {
            SPUtils.putData("password",et_password.getText().toString().trim());
        } else {
            SPUtils.putData("password","");
        }
    }

    private void Login() {
        String username = et_account.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            //load_btn.startLoading();
            fabProgressCircle.show();
            Log.d("Fab标记","调用成功");
            SaveUser();
            RequestBody formBody = new FormBody.Builder()
                    .add("email",username)
                    .add("pwd",password)
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
        } else if (TextUtils.isEmpty(et_account.getText())) {
            et_account.setShakeAnimation();
        } else if (TextUtils.isEmpty(et_password.getText())) {
            et_password.setShakeAnimation();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_fab:
                Login();
                break;
            case R.id.tv_noacc:
                Register();
                break;
            case R.id.act_main_cb:
                if ((boolean)SPUtils.getData("password_save",false)) {
                    SPUtils.putData("password_save", false);
                    SPUtils.putData("password", "");
                } else {
                    SPUtils.putData("password_save", true);
                }
                break;
        }
    }
}
