package com.smartdengg.interfacebuoy.sample;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 创建时间:  2019/01/11 15:52 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class InvokeInterfaceTest {

  private Callback callback;

  private void m(Callback callback) {
    ProxyHandler.wrap(callback, Callback.class);
  }

  private void m0(Callback callback) {
    if (callback != null) {
      callback.on(this);
    }
  }

  private void m1(Callback callback) {
    callback.on(this);
  }

  private void m2(Callback callback) {
    callback.on(null);
  }

  private void m3(Callback callback) {
    callback.on(get1());
  }

  private void m4(Callback callback) {
    callback.on(get2());
  }

  private static void m5(Callback callback) {
    callback.on(null);
  }

  private static String m6(Callback callback) {
    return callback.get();
  }

  private static void m7(Callback callback) {
    callback.on("1");
  }

  private void m8() {
    callback.on("1");
  }

  private String get1() {
    return "";
  }

  private static String get2() {
    return "";
  }

  interface Callback {

    void on(Object object);

    String get();
  }

  public static final class ProxyHandler {

    static <T> T wrap(final T reference, Class<? extends T> interfacee) {

      if (interfacee.isInterface()) {
        return (T) Proxy.newProxyInstance(interfacee.getClassLoader(), new Class[] { interfacee },
            new InvocationHandler() {
              @Override public Object invoke(Object proxy, Method method, Object[] args)
                  throws Throwable {
                if (reference == null) return null;
                return method.invoke(reference, args);
              }
            });
      }
      return reference;
    }
  }
}
