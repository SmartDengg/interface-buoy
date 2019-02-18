package com.smartdengg.interfacebuoy.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * 创建时间:  2019/01/31 18:02 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MainActivity extends Activity {

  private static final String TAG = MainActivity.class.getSimpleName();
  Callback callback;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Test null interface reference
    setCallback(new Callback() {
      @Override public void onClick(View view) {
        Log.d(TAG,"hello world");
      }
    });

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        callback.onClick(v);
      }
    });
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  interface Callback {

    void onClick(View view);
  }
}
