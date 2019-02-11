package com.smartdengg.interfacebuoy.compiler;

/**
 * 创建时间:  2019/01/27 18:17 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class Utils {

  private Utils() {
    throw new AssertionError("no instance");
  }

  static boolean validateInterface(Class<?> interfacee) {
    return interfacee != null && interfacee.isInterface() && interfacee.getInterfaces().length == 0;
  }
}
