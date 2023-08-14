package com.example.pocnativeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends AppCompatActivity {
    MethodChannel methodChannel_toFlutter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine);


        methodChannel_toFlutter = new MethodChannel(
                flutterEngine.getDartExecutor().getBinaryMessenger(), "toFlutterChannelName"
        );
    }

    public void btnClick(View view) {
//        startActivity(
//                FlutterActivity
//                        .withCachedEngine("my_engine_id")
//                        .build(this)
//        );

        invokeFlutterMethod_toAllFlutter();
    }

    public void invokeFlutterMethod_toAllFlutter() {
        if (this.methodChannel_toFlutter != null) {
            this.methodChannel_toFlutter.invokeMethod("flutterMethod", "IAMAndroid", new MethodChannel.Result() {
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