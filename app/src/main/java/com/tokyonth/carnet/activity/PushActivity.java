package com.tokyonth.carnet.activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.mipush.BaseActivity;

public class PushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.message);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ListView listView = findViewById(R.id.push_msg_list);
        String[] companies = new String[]{"你好！"};
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.push_list_item, R.id.title, companies);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                switch (p3) {
                    case 0:

                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //1 = 注册失败(没网);1 = 广播还没注册
        if (BaseActivity.pushState == 1 && BaseActivity.broadcastNet_State == 1) {
            System.out.println("在首页注册广播");
            Message msg = BaseActivity.AppHandler.obtainMessage();
            msg.what = 2;
            BaseActivity.AppHandler.sendMessage(msg);
        }else{
            System.out.println("不用在首页注册广播");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //2 = 广播已注册
        if (BaseActivity.broadcastNet_State == 2) {
            System.out.println("在首页注销广播");
            Message msg = BaseActivity.AppHandler.obtainMessage();
            msg.what = 3;
            BaseActivity.AppHandler.sendMessage(msg);
        }else{
            System.out.println("不用在首页注销广播");
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



