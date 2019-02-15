package com.smartdengg.interfacebuoy.compiler;

import android.util.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 创建时间:  2019/01/11 16:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:  Invocation handler
 */
final class InvocationHandlerAdapter implements InvocationHandler {

  private Object origin;

  InvocationHandlerAdapter(Object instance) {
    this.origin = instance;
  }

  @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (origin == null) {

      if (BuoySettings.loggable) {
        Log.e(BuoySettings.TAG, "ERROR: Attempt to invoke virtual method '"
            + method.toString()
            + " 'on a null object reference");
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length > 4) {
          for (int i = 4, n = stackTrace.length; i < n; i++) {
            Log.e(BuoySettings.TAG, stackTrace[i].toString());
          }
        }
    }

      return null;
    }

    if (BuoySettings.loggable) Log.d(BuoySettings.TAG, "VALUE: " + Strings.toString(args));

    return method.invoke(origin, args);
  }
}
