package com.smartdengg.interfacebuoy.gradle.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/**
 * 创建时间:  2019/01/04 15:43 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MethodInstCollector extends ClassVisitor implements Opcodes {

  private Map<String, List<Method>> classMethodsMap;
  private List<Method> methods;
  private boolean isInterface;

  public MethodInstCollector() {
    super(Opcodes.ASM6);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);
    this.isInterface = AccessPreconditions.isInterface(access);
    if (!isInterface) {
      /*init when not an interface */
      this.classMethodsMap = new HashMap<>();
      this.methods = new ArrayList<>();
      this.classMethodsMap.put(name, methods);
    }
  }

  @Override public MethodVisitor visitMethod(int access, String name, String desc, String signature,
      String[] exceptions) {

    if (isInterface || AccessPreconditions.isAbstract(access)) {
      return super.visitMethod(access, name, desc, signature, exceptions);
    }

    Method method = new Method();
    methods.add(method);
    return new MethodNodeAdapter(api, access, name, desc, signature, exceptions, method);
  }

  public Map<String, List<Method>> getMethods() {
    return classMethodsMap;
  }

  static class MethodNodeAdapter extends MethodNode {

    MethodNodeAdapter(int api, int access, String name, String desc, String signature,
        String[] exceptions, Method method) {
      super(api, access, name, desc, signature, exceptions);
      method.access = access;
      method.name = name;
      method.desc = desc;
      method.instructions = instructions;
    }
  }
}
