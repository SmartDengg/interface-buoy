package com.smartdengg.interfacebuoy.compiler;

import android.util.Log;
import java.lang.reflect.Proxy;

/**
 * 创建时间:  2019/01/11 16:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:  combine dynamic proxy and reflection to delegate the interface reference
 */
public final class InterfaceBuoy {

  public static <T> T proxy(T instance, String interfaceName, String descriptor) {

    try {
      Class<?> interfacee = Class.forName(interfaceName);
      if (Utils.validateInterface(interfacee)) {

        if (BuoySettings.loggable) Log.d(BuoySettings.TAG, "WRAPPER INTERFACE: " + descriptor);

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(interfacee.getClassLoader(), new Class[] { interfacee },
            new InvocationHandlerAdapter(instance));
      }
    } catch (Exception ignore) {

    }
    return instance;
  }
}
