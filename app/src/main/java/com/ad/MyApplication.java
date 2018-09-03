package com.ad;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initXutils();
    }

    private void initXutils() {
        x.Ext.init(this);//Xutils初始化
        x.Ext.setDebug(false);// 是否输出debug日志, 开启debug会影响性能
    }
}
