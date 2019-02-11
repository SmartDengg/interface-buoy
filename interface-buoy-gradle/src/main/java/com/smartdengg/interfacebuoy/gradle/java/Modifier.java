package com.smartdengg.interfacebuoy.gradle.java;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * 创建时间: 2018/03/21 23:00 <br>
 * 作者: dengwei <br>
 * 描述:
 */
public class Modifier extends ClassVisitor implements Opcodes {

  private Map<String, List<Method>> classNonEmptyMethodsMap;
  private List<Method> nonEmptyMethods;

  public Modifier(ClassVisitor classVisitor, Map<String, List<Method>> map) {
    super(Opcodes.ASM6, classVisitor);
    this.classNonEmptyMethodsMap = map;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);
    if (classNonEmptyMethodsMap != null) this.nonEmptyMethods = classNonEmptyMethodsMap.get(name);
  }

  @Override
  public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
      String[] exceptions) {

    final MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);

    if (nonEmptyMethods == null || nonEmptyMethods.size() == 0) return methodVisitor;

    for (Iterator<Method> iterator = nonEmptyMethods.iterator(); iterator.hasNext(); ) {
      Method method = iterator.next();
      if (method.contentsEq(access, name, desc)) {
        iterator.remove();
        return new ProxyWrapperMethodAdapter(api, methodVisitor, method);
      }
    }

    return methodVisitor;
  }

  static class ProxyWrapperMethodAdapter extends MethodVisitor implements Opcodes {

    private final InsnList instructions;

    ProxyWrapperMethodAdapter(int api, MethodVisitor mv, Method method) {
      super(api, mv);
      this.instructions = method.instructions;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

      if (opcode == INVOKEINTERFACE && Type.getReturnType(desc) == Type.VOID_TYPE) {
        for (ListIterator<AbstractInsnNode> iterator = instructions.iterator();
            iterator.hasNext(); ) {

          AbstractInsnNode node = iterator.next();

          if (node instanceof MethodInsnNode //
              && (node.getOpcode() == opcode
              && ((MethodInsnNode) node).owner.equals(owner)
              && ((MethodInsnNode) node).name.equals(name)
              && ((MethodInsnNode) node).desc.equals(desc))) {

            insertBeforeInvokeInterface(node, Type.getObjectType(owner).getClassName(),
                convertSignature(owner, name, desc));

            iterator.remove();
            break;
          }
        }
      }
      super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    private void insertBeforeInvokeInterface(AbstractInsnNode node, String className, String desc) {
      AbstractInsnNode previous = node.getPrevious();
      if (previous instanceof MethodInsnNode) {
        insertBeforeInvokeInterface(previous, className, desc);
      } else {
        super.visitLdcInsn(className);
        super.visitLdcInsn(desc);
        super.visitMethodInsn(INVOKESTATIC, //
            "com/smartdengg/interfacebuoy/compiler/InterfaceBuoy", //
            "wrap", //
            "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;",
            false);

        if (previous != null) {
          if (previous instanceof VarInsnNode) {
            mv.visitVarInsn(previous.getOpcode(), ((VarInsnNode) previous).var);
          } else if (previous instanceof InsnNode) {
            mv.visitInsn(previous.getOpcode());
          } else if (previous instanceof LdcInsnNode) {
            mv.visitLdcInsn(((LdcInsnNode) previous).cst);
          } else if (previous instanceof FieldInsnNode) {
            mv.visitFieldInsn(previous.getOpcode(), ((FieldInsnNode) previous).owner,
                ((FieldInsnNode) previous).name, ((FieldInsnNode) previous).desc);
          }
        }
      }
    }
  }

  private static String convertSignature(String owner, String name, String desc) {
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
