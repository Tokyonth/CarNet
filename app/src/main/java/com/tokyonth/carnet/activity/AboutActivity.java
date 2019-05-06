package com.tokyonth.carnet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.tokyonth.carnet.R;
import com.tokyonth.carnet.adapter.ProjectList;
import com.tokyonth.carnet.bean.Project;

import java.io.File;
import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    private ArrayList arrayList;
    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        InitView();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        arrayList = new ArrayList<>();
        initList();
        ProjectList adapter = new ProjectList(this,R.layout.project_list,arrayList);
        listView.setAdapter(adapter);
    }

    private void initList() {
        arrayList.add(new Project("借鉴了CSDN论坛部分博主的博客\n在此不一一列出","https://blog.csdn.net"));
        arrayList.add(new Project("Iconfont阿里巴巴矢量图标库","https://www.iconfont.cn"));
        arrayList.add(new Project("MPAndroidChart","https://github.com/PhilJay/MPAndroidChart"));
        arrayList.add(new Project("ChartManager","https://github.com/WenWangAndroid/ChartManager"));
        arrayList.add(new Project("DxLoadingButton","https://github.com/StevenDXC/DxLoadingButton"));
        arrayList.add(new Project("OkHttp","https://github.com/square/okhttp"));
        arrayList.add(new Project("Gson","com.google.code.gson"));
        arrayList.add(new Project("Android Library","com.dev.android"));
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.project_list);
        setSupportActionBar(toolbar);
        setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_shared) {
            String path = getPackageResourcePath();
            File apkFile = new File(path);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
