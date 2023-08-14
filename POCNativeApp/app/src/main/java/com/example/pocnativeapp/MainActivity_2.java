package com.example.pocnativeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity_2 extends FlutterActivity {

    private static final String channel = "toJava";
    // 自行实现
    private static final String flutterUseJavaChannel = "flutterUseJava";

    MethodChannel methodChannel_toFlutter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlutterEngine flutterEngine = getFlutterEngine();
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine);

        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), channel).setMethodCallHandler(
                (methodCall, result) -> {
                    if (methodCall.method != null) {
                        result.success(toJava(methodCall.method));
                    } else {
                        result.notImplemented();
                    }
                }
        );

        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), flutterUseJavaChannel).setMethodCallHandler(
                (methodCall, result) -> {
                    if (methodCall.method != null) {
                        result.success(flutterUseJava(methodCall.method));
                    } else {
                        result.notImplemented();
                    }
                }
        );

        methodChannel_toFlutter = new MethodChannel(
                getFlutterEngine().getDartExecutor().getBinaryMessenger(), "toFlutterChannelName"
        );

    }

    public void btnClick(View view) {
        startActivity(
                FlutterActivity
                        .withCachedEngine("my_engine_id")
                        .build(this)
        );
//        invokeFlutterMethod_toAllFlutter();
    }

    public String toJava(String name) {
        System.out.println("toJava 传递的参数：" + name);
        invokeFlutterMethod_toAllFlutter();
        return "你好" + name;
    }

    public String flutterUseJava(String name) {
        System.out.println("flutterUseJava 传递的参数：" + name);
        invokeFlutterMethod_toAllFlutter();
        return "flutterUser Java name:" + name;
    }


    /**
     * Android调用Flutter
     */
    public void invokeFlutterMethod_toAllFlutter() {
        if (this.methodChannel_toFlutter != null) {
            this.methodChannel_toFlutter.invokeMethod("flutterMethod", "Android调用Flutter, 将参数传递给Flutter里面的一个方法", new MethodChannel.Result() {
                @Override
                public void success(Object o) {
                    Log.e("MainActivity", "invokeFlutterMethod_toAllFlutter success");
                }

                @Override
                public void error(String s, String s1, Object o) {
                    Log.e("MainActivity", "invokeFlutterMethod_toAllFlutter error");

                }

                @Override
                public void notImplemented() {
                    Log.e("MainActivity", "invokeFlutterMethod_toAllFlutter notImplemented");

                }
            });
        }
    }
}