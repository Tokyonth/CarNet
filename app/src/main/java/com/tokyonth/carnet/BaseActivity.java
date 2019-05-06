package com.tokyonth.carnet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tokyonth.carnet.ui.widget.CustomDialog;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 100;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
    private CustomDialog customDialog;

    protected void getPermission() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }
    }

    // 提示用户该请求权限的弹出框
      private void showDialogTipUserRequestPermission() {
          customDialog = new CustomDialog(this);
          customDialog.setTitle("权限不可用");
          customDialog.setMessage("由于需要获取相关权限，为你存储个人信息，定位等；\n否则，您将无法正常使用");
          customDialog.setYesOnclickListener("立即开启", new CustomDialog.onYesOnclickListener() {
              @Override
              public void onYesClick() {
                  startRequestPermission();
                  customDialog.dismiss();
              }
          });
          customDialog.setNoOnclickListener("拒绝", new CustomDialog.onNoOnclickListener() {
              @Override
              public void onNoClick() {
                  finish();
                  customDialog.dismiss();
              }
          });
          customDialog.show();
      }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                        } else
                            finish();
                    } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {
        customDialog = new CustomDialog(this);
        customDialog.setTitle("权限不可用");
        customDialog.setMessage("请在-应用设置-权限-中，允许使用存储权限来保存用户数据,定位");
        customDialog.setYesOnclickListener("立即开启", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                // 跳转到应用设置界面
                goToAppSetting();
                customDialog.dismiss();
            }
        });
        customDialog.setNoOnclickListener("拒绝", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                finish();
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void bindService(Class<? extends Service> service, ServiceConnection serviceConnection) {
        bindService(new Intent(this, service), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    protected boolean isFinishingOrDestroyed() {
        return isFinishing() || isDestroyed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                    } else {
                    if (customDialog != null && customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}