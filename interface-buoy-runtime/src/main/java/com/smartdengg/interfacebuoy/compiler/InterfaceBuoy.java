package com.smartdengg.interfacebuoy.compiler;

import android.util.Log;
import java.lang.reflect.Proxy;

/**
 * 创建时间:  2019/01/11 16:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:  combine dynamic proxy and reflection to delegate the interface reference
 */
public final class InterfaceBuoy {

  public static boolean loggable = false;

  private static final String TAG = "BUOY";

  public static <T> T wrap(T instance, Object value, String interfaceName, String desc) {

    try {
      Class<?> interfacee = Class.forName(interfaceName);
      if (Utils.validateInterface(interfacee)) {

        if (loggable) {
          Log.d(TAG, "BUOY INTERFACE: " + desc);
          Log.d(TAG, "VALUE = " + Strings.toString(value));
        }

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(interfacee.getClassLoader(), new Class[] { interfacee },
            new InvocationHandlerAdapter(instance));
      }
    } catch (Exception ignore) {

    }
    return instance;
  }
}
