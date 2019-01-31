package com.smartdengg.interfacebuoy.compiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 创建时间:  2019/01/11 16:54 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
final class InvocationHandlerAdapter implements InvocationHandler {

  private Object origin;

  InvocationHandlerAdapter(Object instance) {
    this.origin = instance;
  }

  @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (origin == null) return null;
    return method.invoke(origin, args);
  }
}
