package com.tokyonth.carnet.mipush;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

public class MipushBroadcast extends PushMessageReceiver {

    private String TAG = "TOKYONTH -------->";
    //透传消息到达客户端时调用
    //作用：可通过参数message从而获得透传消息，具体请看官方SDK文档
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        //打印消息方便测试
        //System.out.println("透传消息到达了");
        //System.out.println("透传消息是"+message.toString());
        Log.d(TAG,"透传消息是" + message.toString());
    }

//通知消息到达客户端时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：通过参数message从而获得通知消息，具体请看官方SDK文档

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        //打印消息方便测试
       // System.out.println("通知消息到达了");
        //System.out.println("通知消息是" + message.toString());
        Log.d(TAG,"通知消息是" + message.toString());
    }

    //用户手动点击通知栏消息时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：1. 通过参数message从而获得通知消息，具体请看官方SDK文档
    //2. 设置用户点击消息后打开应用 or 网页 or 其他页面
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        //打印消息方便测试
        System.out.println("用户点击了通知消息");
        System.out.println("通知消息是" + message.toString());
        System.out.println("点击后,会进入应用" );
        Log.d(TAG,"");
    }


    //用来接收客户端向服务器发送命令后的响应结果。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
//        System.out.println(command );
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                //打印信息便于测试注册成功与否
//                System.out.println("注册成功");
            } else {
//                System.out.println("注册失败");
            }
        }
    }

    //用于接收客户端向服务器发送注册命令后的响应结果。
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        //System.out.println("开始注册"+command );
        Log.d(TAG,"开始注册" + command);

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                //打印日志：注册成功
                //System.out.println("推送服务注册成功");
                Log.d(TAG,"推送服务注册成功");
                //广播是否已经注册了,2代表已注册
                if(PushBaseActivity.broadcastNet_State == 2){
                    //通过回调注销广播
                    Message msg = PushBaseActivity.AppHandler.obtainMessage();
                    msg.what = 3;
                    PushBaseActivity.AppHandler.sendMessage(msg);
                    //设置状态量:3 = 推送服务注册成功;1 = 广播还未注册(已被注销)
                    PushBaseActivity.pushState = 3;
                    PushBaseActivity.broadcastNet_State =1 ;
                }else{
                    //System.out.println("结束");
                    Log.d(TAG,"结束");
                    PushBaseActivity.pushState = 3;
                    PushBaseActivity.broadcastNet_State =1 ;
                }
            } else {
                //打印日志：注册失败
                //System.out.println("注册失败");
                Log.d(TAG,"注册失败");
                PushBaseActivity.pushState = 2;
                //通过回调延时注册广播
                //System.out.println("回调延时注册广播");
                Log.d(TAG,"回调延时注册广播");
                Message msg = PushBaseActivity.AppHandler.obtainMessage();
                msg.what = 1;
                PushBaseActivity.AppHandler.sendMessage(msg);
            }
        } else {
            //System.out.println("其他情况"+message.getReason());
            Log.d(TAG,"其他情况");
        }

    }

}