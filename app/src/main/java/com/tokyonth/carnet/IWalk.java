package com.tokyonth.carnet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

import com.tokyonth.carnet.activity.AboutActivity;
import com.tokyonth.carnet.activity.CloudActivity;
import com.tokyonth.carnet.activity.CurrencyActivity;
import com.tokyonth.carnet.activity.PushActivity;
import com.tokyonth.carnet.activity.SettingsPrenference;
import com.tokyonth.carnet.fragment.EmergencyWarning;
import com.tokyonth.carnet.fragment.HealthDetails;
import com.tokyonth.carnet.fragment.Monitoring;
import com.tokyonth.carnet.ui.BadgeActionProvider;
import com.tokyonth.carnet.ui.widget.CustomDialog;
import com.tokyonth.carnet.ui.widget.NetImageView;
import com.tokyonth.carnet.utils.SPUtils;

public class IWalk extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private BottomNavigationView navigation;
    private NetImageView imageView;

    private HealthDetails home;
    private EmergencyWarning dashboard;
    private Monitoring user;

    private CustomDialog customDialog;
    private BadgeActionProvider mActionProvider;
    private LinearLayout settings_click;
    private LinearLayout model_click;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.iwalk);
        new SPUtils(this,"app_conf");
        InitView();
        InitClick();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initFragmentHome();
        BootDisplay();
    }

    private void BootDisplay() {
        Boolean tag = (Boolean) SPUtils.getData("boot_display_dialog",false);
        if (!tag) {
            customDialog = new CustomDialog(this);
            customDialog.setTitle("提示");
            customDialog.setMessage("欢迎使用 i 行！");
            customDialog.setYesOnclickListener("确定", new CustomDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    customDialog.dismiss();
                }
            });
            customDialog.setNoOnclickListener("不再显示", new CustomDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    SPUtils.putData("boot_display_dialog", true);
                    customDialog.dismiss();
                }
            });
            customDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
        }
    }

    private void InitView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.nav_header_main, null);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (NetImageView) findViewById(R.id.main_pic);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        settings_click = (LinearLayout) findViewById(R.id.setting_click);
        model_click = (LinearLayout) findViewById(R.id.setting_model);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private void InitClick() {
        settings_click.setOnClickListener(this);
        model_click.setOnClickListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fab.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    initFragmentHome();
                    return true;
                case R.id.navigation_dashboard:
                    initFragmentDashboard();
                    return true;
                case R.id.navigation_notifications:
                    initFragmentUser();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_push);
        mActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mActionProvider.setOnClickListener(0, new BadgeActionProvider.OnClickListener() {
            @Override
            public void onClick(int what) {
                Intent intent = new Intent();
                intent.setClass(IWalk.this,PushActivity.class);
                Transition slide = TransitionInflater.from(IWalk.this).inflateTransition(R.transition.slide);
                getWindow().setEnterTransition(slide);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(IWalk.this).toBundle());
            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_subject_info) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_cloud_services) {
            Intent intent = new Intent();
            intent.setClass(IWalk.this,CloudActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_currency) {
            Intent intent = new Intent();
            intent.setClass(IWalk.this,CurrencyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_customer_service) {
            customDialog = new CustomDialog(this);
            customDialog.setTitle("提示");
            customDialog.setMessage("此操作将会拨打客服电话\n确定联系客服？");
            customDialog.setYesOnclickListener("确定", new CustomDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    customDialog.dismiss();
                }
            });
            customDialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    customDialog.dismiss();
                }
            });
            customDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent();
            intent.setClass(IWalk.this,AboutActivity.class);
            Transition slide = TransitionInflater.from(IWalk.this).inflateTransition(R.transition.slide);
            getWindow().setEnterTransition(slide);
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(IWalk.this).toBundle());
        }
        return true;
    }

    private void initFragmentHome(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (home == null){
            home = new HealthDetails();
            transaction.add(R.id.main_frame_layout, home);
        }
        HideFragment(transaction);
        transaction.show(home);
        transaction.commit();
    }

    private void initFragmentUser(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (user == null){
            user = new Monitoring();
            transaction.add(R.id.main_frame_layout,user);
        }
        HideFragment(transaction);
        transaction.show(user);
        transaction.commit();
    }

    private void initFragmentDashboard(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (dashboard == null){
            dashboard = new EmergencyWarning();
            transaction.add(R.id.main_frame_layout,dashboard);
        }
        HideFragment(transaction);
        transaction.show(dashboard);
        transaction.commit();
    }

    private void HideFragment(FragmentTransaction transaction){
        if(home != null){
            transaction.hide(home);
        }
        if(dashboard != null){
            transaction.hide(dashboard);
        }
        if(user != null){
            transaction.hide(user);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Transition slide = TransitionInflater.from(IWalk.this).inflateTransition(R.transition.slide);
        switch (v.getId()) {
            case R.id.setting_click:
                intent.setClass(IWalk.this,SettingsPrenference.class);
                getWindow().setEnterTransition(slide);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(IWalk.this).toBundle());
                break;
            case R.id.fab:
                imageView.setImageURL("https://source.unsplash.com/1080x600/?");
                break;
            case R.id.setting_model:

                break;
        }
    }
}
