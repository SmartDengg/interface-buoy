package com.smartdengg.interfacebuoy.sample;

import android.app.Application;
import com.smartdengg.interfacebuoy.compiler.BuoySettings;

/**
 * 创建时间:  2019/02/18 11:15 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    BuoySettings.loggable = true;
  }
}
