package com.smartdengg.interfacebuoy.compiler;

/**
 * 创建时间:  2019/02/15 11:56 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class BuoySettings {

  public static boolean loggable = false;

  static final String TAG = "BuoySettings";

  private BuoySettings() {
    throw new AssertionError("no instance");
  }
}
