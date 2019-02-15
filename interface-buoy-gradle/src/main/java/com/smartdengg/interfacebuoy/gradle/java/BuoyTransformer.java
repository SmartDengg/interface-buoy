package com.smartdengg.interfacebuoy.gradle.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static com.smartdengg.interfacebuoy.gradle.java.Utils.getOpcodeByType;

/**
 * 创建时间:  2019/02/12 17:31 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class BuoyTransformer implements Opcodes {

  private BuoyTransformer() {
    throw new AssertionError("no instance");
  }

  public static void transform(ClassNode classNode) {

    final List<MethodNode> newMethodNodes = new ArrayList<>();
    final Set<String> newMethodNameAndDesc = new HashSet<>();

    for (MethodNode methodNode : classNode.methods) {

      if (!Utils.validateMethod(methodNode)) continue;

      final InsnList methodInstructions = methodNode.instructions;
      if (methodInstructions.size() == 0) continue;

      for (ListIterator<AbstractInsnNode> iterator = methodInstructions.iterator();
          iterator.hasNext(); ) {
        final AbstractInsnNode node = iterator.next();

        if (node instanceof MethodInsnNode) {
          final MethodInsnNode targetInsn = (MethodInsnNode) node;

          if (node.getOpcode() == INVOKEINTERFACE
              && Type.getReturnType(targetInsn.desc) == Type.VOID_TYPE) {

            final String name = "buoy$" + targetInsn.name;
            final String desc = generateBuoyMethodDescriptor(targetInsn);
            final String full = name + ":" + desc;

            if (newMethodNameAndDesc.add(full)) {
              final MethodNode buoyMethod =
                  generateStaticBuoyMethodAndReturn(targetInsn, name, desc);
              newMethodNodes.add(buoyMethod);
            }
            final MethodInsnNode updateInsn =
                new MethodInsnNode(INVOKESTATIC, classNode.name, name, desc, false);
            methodInstructions.set(targetInsn, updateInsn);
          }
        }
      }
    }
    classNode.methods.addAll(newMethodNodes);
  }

  private static String generateBuoyMethodDescriptor(MethodInsnNode targetMethodInsn) {

    StringBuilder stringBuilder = new StringBuilder("(");
    stringBuilder.append(Type.getObjectType(targetMethodInsn.owner).getDescriptor());

    Type descriptor = Type.getMethodType(targetMethodInsn.desc);
    for (int i = 0; i < descriptor.getArgumentTypes().length; i++) {
      stringBuilder.append(descriptor.getArgumentTypes()[i].getDescriptor());
    }
    stringBuilder.append(")");
    stringBuilder.append(descriptor.getReturnType().getDescriptor());

    return stringBuilder.toString();
  }

  private static MethodNode generateStaticBuoyMethodAndReturn(MethodInsnNode targetMethodInsn,
      String name, String desc) {

    MethodNode buoyMethod = new MethodNode(ACC_STATIC, name, desc, null, null);
    buoyMethod.visitAnnotation("Lcom/smartdengg/interfacebuoy/compiler/annotations/Buoy;", false);

    int stack = 1;
    int locals = 2;

    //begin add method body
    InsnList insnList = new InsnList();
    insnList.add(new VarInsnNode(ALOAD, 0));
    insnList.add(new LdcInsnNode(Type.getObjectType(targetMethodInsn.owner).getClassName()));
    insnList.add(new LdcInsnNode(
        Utils.convertSignature(targetMethodInsn.owner, targetMethodInsn.name,
            targetMethodInsn.desc)));
    insnList.add(new MethodInsnNode(INVOKESTATIC, //
        "com/smartdengg/interfacebuoy/compiler/InterfaceBuoy", //
        "proxy", //
        "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", //
        false));
    final Type[] argumentTypes = Type.getArgumentTypes(targetMethodInsn.desc);
    for (Type argumentType : argumentTypes) {

      final int opcode = getOpcodeByType(argumentType);
      insnList.add(new VarInsnNode(opcode, stack++));

      locals++;

      if (opcode == DLOAD || opcode == LLOAD) {
        stack++;
        locals++;
      }
    }

    insnList.add(new MethodInsnNode(INVOKEINTERFACE, targetMethodInsn.owner, targetMethodInsn.name,
        targetMethodInsn.desc, targetMethodInsn.itf));
    buoyMethod.instructions.add(insnList);
    buoyMethod.visitInsn(RETURN);
    //end

    //Minimum required operand stack depth is 3
    stack = Math.max(stack, 3);
    buoyMethod.visitMaxs(stack, locals);

    return buoyMethod;
  }
}
