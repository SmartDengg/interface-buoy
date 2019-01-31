package com.smartdengg.interfacebuoy.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.smartdengg.interfacebuoy.compiler.InterfaceBuoy;

/**
 * 创建时间:  2019/01/31 18:02 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MainActivity extends Activity {

  Callback callback;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    InterfaceBuoy.loggable = true;

    setCallback(new Callback() {
      @Override public void on(View view) {
        /*no-op*/
      }
    });

    findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        callback.on(v);
      }
    });
  }

  private void setCallback(Callback callback) {
    this.callback = callback;
  }

  interface Callback {
    void on(View view);
  }
}
