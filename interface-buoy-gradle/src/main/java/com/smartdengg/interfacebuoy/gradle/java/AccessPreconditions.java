package com.smartdengg.interfacebuoy.gradle.java;

import org.objectweb.asm.Opcodes;

/**
 * 创建时间:  2018/03/15 17:45 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class AccessPreconditions implements Opcodes {

  private AccessPreconditions() {
    throw new AssertionError("no instance");
  }

  static boolean isPrivate(int access) {
    return (access & ACC_PRIVATE) != 0;
  }

  static boolean isPublic(int access) {
    return (access & ACC_PUBLIC) != 0;
  }

  static boolean isStatic(int access) {
    return (access & ACC_STATIC) != 0;
  }

  static boolean isInterface(int access) {
    return (access & ACC_INTERFACE) != 0;
  }

  static boolean isAbstract(int access) {
    return (access & ACC_ABSTRACT) != 0;
  }
}
