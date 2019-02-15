package com.smartdengg.interfacebuoy.gradle.java;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * 创建时间:  2019/02/12 23:56 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class Utils implements Opcodes {

  private Utils() {
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

  static int getOpcodeByType(Type type) {
    if (type == Type.BOOLEAN_TYPE
        || type == Type.BYTE_TYPE
        || type == Type.CHAR_TYPE
        || type == Type.INT_TYPE
        || type == Type.SHORT_TYPE) {
      return ILOAD;
    } else if (type == Type.DOUBLE_TYPE) {
      return DLOAD;
    } else if (type == Type.FLOAT_TYPE) {
      return FLOAD;
    } else if (type == Type.LONG_TYPE) {
      return LLOAD;
    } else {
      return ALOAD;
    }
  }

  static boolean validateMethod(MethodNode methodNode) {
    final String methodName = methodNode.name;

    if (methodName.equals("<init>") || methodName.equals("<clinit>")) return false;
    if (methodNode.invisibleAnnotations == null) return true;

    for (AnnotationNode annotationNode : methodNode.invisibleAnnotations) {
      if (annotationNode.desc.equals("Lcom/smartdengg/interfacebuoy/compiler/annotations/Buoy;")) {
        return false;
      }
    }

    return true;
  }

  static String convertSignature(String owner, String name, String desc) {
    Type method = Type.getType(desc);
    Type objectType = Type.getObjectType(owner);
    StringBuilder sb = new StringBuilder();
    sb.append(objectType.getClassName()).append('.').append(name).append(':');
    sb.append('(');
    for (int i = 0; i < method.getArgumentTypes().length; i++) {
      sb.append(method.getArgumentTypes()[i].getClassName());
      if (i != method.getArgumentTypes().length - 1) {
        sb.append(",");
      }
    }
    sb.append(')');
    sb.append(method.getReturnType().getClassName());
    return sb.toString();
  }
}
